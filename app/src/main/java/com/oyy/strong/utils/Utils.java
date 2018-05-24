package com.oyy.strong.utils;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by
 * ouyangyi on 18/05/22.
 */
public class Utils {

    private static Utils instance;


    public static Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }

        return instance;
    }

    /**
     * 获取手机设备宽度
     */
    public int getWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        if (dm.widthPixels > dm.heightPixels) {
            return dm.heightPixels;
        } else {
            return dm.widthPixels;
        }
    }

    /**
     * 获取手机设备高度
     */
    public int getHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        if (dm.widthPixels > dm.heightPixels) {
            return dm.widthPixels;
        } else {
            return dm.heightPixels;
        }
    }

    /**
     * 去掉ScrollView 滑动到底部的阴影
     *
     * @param view
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void setOverScrollMode(ScrollView view) {
        if (Build.VERSION.SDK_INT > 8 && null != view) {
            view.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
    }
}
