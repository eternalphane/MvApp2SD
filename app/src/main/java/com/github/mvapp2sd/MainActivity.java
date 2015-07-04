package com.github.mvapp2sd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.widget.*;


public class MainActivity extends Activity {
    private MyApplication app;
    private List<MyAppInfo> AppInfoList;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (MyApplication) getApplication();
        initAppList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_refresh:
                AppInfoList.clear();
                adapter.notifyDataSetChanged();
                AppListLoader loader = new AppListLoader();
                loader.execute();
                break;
            case R.id.action_select:
                adapter.flag_cbVisibility = !adapter.flag_cbVisibility;
                if (!adapter.flag_cbVisibility) {
                    for (MyAppInfo myAppInfo : AppInfoList) {
                        myAppInfo.SetChecked(false);
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, Activity_Settings.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        return false;
    }

    private void getAppInfoList(List<MyAppInfo> list) {
        PackageManager pm = this.getPackageManager();
        List<PackageInfo> packs = pm.getInstalledPackages(0);
        for (PackageInfo info : packs) {
            MyAppInfo myAppInfo = new MyAppInfo();
            if ((app.Get_set_bool(0)) || (info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                try {
                    ApplicationInfo appInfo = pm.getApplicationInfo(info.packageName, PackageManager.GET_META_DATA);
                    myAppInfo.SetIcon(appInfo.loadIcon(pm));
                    myAppInfo.SetAppName(pm.getApplicationLabel(appInfo).toString());
                    myAppInfo.SetPackageName(appInfo.packageName);
                    list.add(myAppInfo);
                } catch (PackageManager.NameNotFoundException e) {
                    // do nothing
                }
            }
        }
        Collections.sort(list);
    }

    private void initAppList() {
        AppInfoList = new ArrayList<MyAppInfo>();
        getAppInfoList(AppInfoList);
        adapter = new MyAdapter(this, AppInfoList);
        ListView lv = (ListView) findViewById(R.id.list_app);
        lv.setAdapter(adapter);
    }

    private class AppListLoader extends AsyncTask<Object, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            // do nothing
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            try {
                getAppInfoList(AppInfoList);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Unknown error occured!", Toast.LENGTH_SHORT).show();
                System.exit(1);
            }
            if (AppInfoList.isEmpty()) {
                return false;
            } else {
                return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getApplicationContext(), "Cannot get application list. Killing...", Toast.LENGTH_SHORT).show();
                getApplication().onTerminate();
            }
        }
    }
}