package com.autodownloadimages.utilites;

import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by bitu on 17/8/17.
 */

public class DeviceScreenModel {
    private static DeviceScreenModel deviceScreenModel;
    private Rect deviceRect;
    private float density;
    private float scaledDensity;
    private float xdpi;
    private int densityDpi;

    public DeviceScreenModel (DisplayMetrics displayMetrics) {
        this.deviceRect = new Rect(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        this.density = displayMetrics.density;
        this.xdpi = displayMetrics.xdpi;
        this.scaledDensity = displayMetrics.scaledDensity;
        this.densityDpi = displayMetrics.densityDpi;
    }

    public static DeviceScreenModel getInstance () {
        if (deviceScreenModel == null) {
            deviceScreenModel = new DeviceScreenModel(Resources.getSystem().getDisplayMetrics());
        }
        return deviceScreenModel;
    }

    public Rect getDeviceRect () {
        return deviceRect;
    }


    public float getDensity () {
        return density;
    }


    public float getScaledDensity () {
        return scaledDensity;
    }


    public int getDensityDpi () {
        return densityDpi;
    }

    public int convertDpToPixel (int dp) {
        return Math.round(dp * (xdpi / density));
    }

    public int getItemHeightWidth(){
        return (int) (deviceRect.width() * 0.50f);
    }

    public int getNavigationViewWidth() {
        return (int) (deviceRect.width() * 0.70f);
    }

    public int getWidth(float value){
        return (int) (deviceRect.width() * value);
    }

    public LinearLayout.LayoutParams getLinearLayoutParams(LinearLayout linearLayout, int height) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        layoutParams.width = height;
        layoutParams.height = height;
        return layoutParams;
    }

    public RelativeLayout.LayoutParams getRelativeLayoutParams(RelativeLayout linearLayout,
                                                               int height, int left, int top, int right, int bottom) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
        layoutParams.width = height;
        layoutParams.height = height;
        linearLayout.setPadding(left, top, right, bottom);
        return layoutParams;
    }
}
