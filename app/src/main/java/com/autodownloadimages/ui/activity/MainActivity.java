package com.autodownloadimages.ui.activity;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.autodownloadimages.R;
import com.autodownloadimages.base.activity.BaseActivity;
import com.autodownloadimages.database.DatabaseHelper;
import com.autodownloadimages.model.MenuInfoModel;
import com.autodownloadimages.model.RecipeModel;
import com.autodownloadimages.utilites.ItemClickSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public DatabaseHelper mDBHelper;
    public SQLiteDatabase mDb;
    public List<RecipeModel> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private MainAdapter adapter;

    public List<RecipeModel> getList() {
        return list;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    public void initializeComponent() {

        initializeDataBase();
        initializeRecyclerView();

    }

    private void initializeRecyclerView() {
        final List<RecipeModel> list = getList();
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MainAdapter(this, list);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                RecipeModel model = list.get(position);
//                ((MainActivity)getActivity()).getSideMenuHelper()
//                        .addCategoryFragment(model);
            }
        });
    }

    private void initializeDataBase() {
        mDBHelper = new DatabaseHelper(this);
        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }
        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        if (mDBHelper.openDataBase()) {
//            list = mDBHelper.getMenuInfoDetail();
//            list.addAll(list);
            mDBHelper.close();
        }

        getCategory();
    }

    private void getCategory() {
        list.clear();
        DatabaseHelper mDBHelper = getDatabaseHelper();
        if (getDatabaseHelper().openDataBase()) {
            for (int i=0;i<4;i++){
                list.addAll(mDBHelper.getRecipes(i));
            }
            mDBHelper.close();
        }
    }

    public DatabaseHelper getDatabaseHelper() {
        return mDBHelper;
    }
}
