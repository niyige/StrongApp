package com.oyy.strong.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * 屏幕密度计算工具
 * 计算公式 pixels = dips * (density / 160)
 */
public class DensityUtil {

    private static DensityUtil densityUtil;

    public static DensityUtil getInstance() {
        if (densityUtil == null) {
            densityUtil = new DensityUtil();
        }
        return densityUtil;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dipTopx(Context context, float d) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, d,
                dm);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int pxTodip(Context context, float d) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return (int) Math.ceil(((d * 160) / dm.densityDpi));
    }

    /**
     * 获取屏幕宽的像素
     *
     * @param mContext
     * @return
     */
    public static int getDisplayWidth(Activity mContext) {
        DisplayMetrics metrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * 获取屏幕高的像素
     *
     * @param mContext
     * @return
     */
    public static int getDisplayHeight(Activity mContext) {
        DisplayMetrics metrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

}
