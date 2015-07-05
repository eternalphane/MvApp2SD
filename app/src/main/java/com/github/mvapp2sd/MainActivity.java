package com.github.mvapp2sd;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.io.DataOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.widget.*;


public class MainActivity extends Activity {
    private MyApplication app;
    private List<MyAppInfo> AppInfoList;
    private MyAdapter adapter;
    private boolean visMenuItem[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String apkRoot = "chmod 777 " + getPackageCodePath();
        Process process;
        DataOutputStream os;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(apkRoot + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            Log.d("Phone Link", "su root - the device is not rooted,  error message： " + e.getMessage());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (MyApplication) getApplication();
        initAppList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        visMenuItem = new boolean[menu.size()];
        for (int i = 0; i < menu.size(); i++) {
            visMenuItem[i] = menu.getItem(i).isVisible();
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setVisible(visMenuItem[i]);
        }
        menu.findItem(R.id.action_select).setTitle(adapter.flag_cbVisibility ?
                        R.string.action_select_cancel :
                        R.string.action_select
        ).setVisible(true);
        if (adapter.flag_cbVisibility) {
            for (MyAppInfo i : AppInfoList) {
                if (i.GetChecked()) {
                    menu.findItem(R.id.action_link).setEnabled(true);
                    break;
                }
            }
        }
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
                for (int i = 0; i < visMenuItem.length; i++) {
                    visMenuItem[i] = !visMenuItem[i];
                }
                if (!adapter.flag_cbVisibility) {
                    for (MyAppInfo myAppInfo : AppInfoList) {
                        myAppInfo.SetChecked(false);
                    }
                    setTitle(R.string.app_name);
                } else {
                    setTitle("Select...");
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, Activity_Settings.class));
                return true;
            case R.id.action_quit:
                app.onTerminate();
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
                    myAppInfo.SetSize(FileSize(appInfo.sourceDir), FileSize(appInfo.dataDir), DalvikCacheSize(appInfo.packageName));
                    list.add(myAppInfo);
                } catch (PackageManager.NameNotFoundException e) {
                    // do nothing
                }
            }
        }
        Collections.sort(list);
    }

    private long DalvikCacheSize(String name) {
        File folder = new File("/data/dalvik-cache/arm");
        File files[] = folder.listFiles();
        if (folder.exists()) {
            if (files == null) {
                Log.e("info", "2333\n");
                return 0;
            }
            for (File dalvik : files) {
                if (dalvik.getName().contains(name)) {
                    return FileSize(dalvik);
                }
            }
            return 0;
        } else {
            return 0;
        }
    }

    private long FileSize(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return 0;
        }
        return FileSize(file);
    }

    private long FileSize(File file) {
        if (file.isDirectory()) {
            long folderSize = 0;
            File subfiles[] = file.listFiles();
            if (subfiles == null) {
                return 0;
            }
            for (File subFile : subfiles) {
                folderSize += FileSize(subFile);
            }
            return folderSize;
        } else {
            return file.length();
        }
    }

    private void initAppList() {
        AppInfoList = new ArrayList<MyAppInfo>();
        //getAppInfoList(AppInfoList);
        adapter = new MyAdapter(this, AppInfoList);
        ListView lv = (ListView) findViewById(R.id.list_app);
        lv.setAdapter(adapter);
        AppListLoader loader = new AppListLoader();
        loader.execute();
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
            return !AppInfoList.isEmpty();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                adapter.notifyDataSetChanged();
                ((ListView) findViewById(R.id.list_app)).setOnItemClickListener(
                        new ListView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                app.SetAppInfo((MyAppInfo) adapter.getItem(position));
                                startActivity(new Intent(MainActivity.this, Activity_AppInfo.class));
                            }
                        }
                );
            } else {
                Toast.makeText(getApplicationContext(), "Cannot get application list. Killing...", Toast.LENGTH_SHORT).show();
                getApplication().onTerminate();
            }
        }
    }
}