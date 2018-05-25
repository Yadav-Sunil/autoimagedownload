package com.autodownloadimages.base.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.autodownloadimages.base.fragment.BaseFragment;

/**
 * Created by bitu on 15/8/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private FragmentManager fm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getSupportFragmentManager();
        beforeSetContentView();
        setContentView(getLayoutResourceId());
        initializeComponent();
    }

    public FragmentManager getFm() {
        return fm;
    }

    public FragmentTransaction getNewFragmentTransaction() {
        return getFm().beginTransaction();
    }

    public void clearFragmentBackStack() {
        clearFragmentBackStack(0);
    }

    public Fragment getFragmentByTag(String tag) {
        return getFm().findFragmentByTag(tag);
    }

    public void clearFragmentBackStack(int pos) {
        if (fm.getBackStackEntryCount() > pos) {
            try {
                fm.popBackStackImmediate(fm.getBackStackEntryAt(pos).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } catch (IllegalStateException e) {
            }
        }
    }

    @Override
    public void onBackPressed() {
        Fragment baseFragment = getFm().findFragmentById(getFragmentContainerResourceId());
        if (baseFragment != null && baseFragment instanceof BaseFragment) {
            if (((BaseFragment) baseFragment).handleOnBackPress()) {
                return;
            }
        }
        super.onBackPressed();
    }

    public BaseFragment getLatestFragment() {

        Fragment fragment = fm.findFragmentById(getFragmentContainerResourceId());
        if (fragment != null && fragment instanceof BaseFragment) {
            return ((BaseFragment) fragment);
        }

        return null;
    }

    public void changeFragment(Fragment f, boolean addBackStack,
                               boolean clearAll, int pos, int enterAnim, int exitAnim,
                               int enterAnimBackStack, int exitAnimBackStack, boolean isReplace) {
        if (getFragmentContainerResourceId() == -1) return;
        if (clearAll) {
            clearFragmentBackStack(pos);
        }
        if (f != null) {
            try {
                FragmentTransaction ft = getNewFragmentTransaction();
                ft.setCustomAnimations(enterAnim, exitAnim, enterAnimBackStack, exitAnimBackStack);
                if (isReplace) {
                    ft.replace(getFragmentContainerResourceId(), f, f.getClass().getSimpleName());
                } else {
                    ft.add(getFragmentContainerResourceId(), f, f.getClass().getSimpleName());
                }
                if (addBackStack) {
                    ft.addToBackStack(f.getClass().getSimpleName());
                }
                ft.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void beforeSetContentView() {

    }

    public abstract int getLayoutResourceId();

    public int getFragmentContainerResourceId() {
        return -1;
    }

    public abstract void initializeComponent();

    public void onCreateViewFragment(BaseFragment baseFragment) {
    }


    public void onDestroyViewFragment(BaseFragment baseFragment) {
    }

    public boolean isValidString(String data) {
        return data != null && !data.trim().isEmpty();
    }

    public void printLog(String msg) {
        if (msg == null) return;
        Log.e(getClass().getSimpleName(), msg);
    }

    public synchronized void hideKeyboard() {
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        hideKeyboard(view);
    }

    public synchronized void hideKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
