package com.github.mvapp2sd;

import android.app.Application;

/**
 * Created by EternalPhane on 2015/7/4.
 * GitHub: https://github.com/EternalPhane
 */
public class MyApplication extends Application {
    private int set_num;
    private boolean set_bool[];
    private MyAppInfo AppInfo;

    @Override
    public void onCreate() {
        set_num = 3;
        set_bool = new boolean[set_num];
        for (int i = 0; i < set_num; i++) {
            set_bool[i] = false;
        }
        super.onCreate();
    }

    public int Get_set_num() {
        return set_num;
    }

    public boolean Get_set_bool(int index) {
        return (index < set_num && set_bool[index]);
    }

    public MyAppInfo GetAppInfo() {
        return AppInfo;
    }

    public void SetAppInfo(MyAppInfo AppInfo) {
        this.AppInfo = AppInfo;
    }

    public void Toggle_set_bool(int index) {
        if (index < set_num) {
            set_bool[index] = !set_bool[index];
        }
    }
}
