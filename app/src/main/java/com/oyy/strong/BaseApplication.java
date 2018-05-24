package com.oyy.strong;

import android.app.Application;

/**
 * Created by
 * ouyangyi on 18/4/26.
 */

public class BaseApplication extends Application{

    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized BaseApplication getInstance() {
        if (instance == null)
            instance = new BaseApplication();
        return instance;
    }
}
