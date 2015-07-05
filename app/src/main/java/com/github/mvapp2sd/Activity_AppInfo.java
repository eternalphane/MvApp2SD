package com.github.mvapp2sd;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;

/**
 * Created by EternalPhane on 2015/7/6.
 * GitHub: https://github.com/EternalPhane
 */
public class Activity_AppInfo extends Activity {
    private MyApplication app;
    private MyAppInfo AppInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appinfo);
        app = (MyApplication) getApplication();
        AppInfo = app.GetAppInfo();
        ((ImageView) findViewById(R.id.appinfo_icon)).setImageDrawable(AppInfo.GetIcon());
        ((TextView) findViewById(R.id.appinfo_app_name)).setText(AppInfo.GetAppName());
        ((TextView) findViewById(R.id.appinfo_package_name)).setText(AppInfo.GetPackageName());
        Map<String, String> Size = AppInfo.GetSize();
        ((TextView) findViewById(R.id.appinfo_size_total)).setText(AppInfo.GetTotalSize());
        ((TextView) findViewById(R.id.appinfo_size_package)).setText(Size.get("PackageSize"));
        ((TextView) findViewById(R.id.appinfo_size_data)).setText(Size.get("DataSize"));
        ((TextView) findViewById(R.id.appinfo_size_dalvik)).setText(Size.get("DalvikSize"));
    }
}
