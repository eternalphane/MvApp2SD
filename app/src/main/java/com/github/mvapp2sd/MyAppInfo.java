package com.github.mvapp2sd;

import android.graphics.drawable.Drawable;

import java.text.Collator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by EternalPhane on 2015/6/13.
 * GitHub: https://github.com/EternalPhane
 */
public class MyAppInfo implements Comparable {
    private String AppName;
    private String PackageName;
    private Drawable Icon;
    private AppSizeInfo Size;
    private boolean IsChecked;

    public MyAppInfo() {
        IsChecked = false;
        Size = new AppSizeInfo();
    }

    public MyAppInfo(MyAppInfo AppInfo) {
        this.AppName = AppInfo.AppName;
        this.PackageName = AppInfo.PackageName;
        this.Icon = AppInfo.Icon;
        this.Size = AppInfo.Size;
        this.IsChecked = AppInfo.IsChecked;
    }

    public String GetAppName() {
        return AppName;
    }

    public String GetPackageName() {
        return PackageName;
    }

    public Drawable GetIcon() {
        return Icon;
    }

    public Map<String, String> GetSize() {
        Map<String, String> result = new HashMap<String, String>();
        Map<String, Long> rawSize = Size.GetSize();
        result.put("PackageSize", FormatSize(rawSize.get("PackageSize")));
        result.put("DataSize", FormatSize(rawSize.get("DataSize")));
        result.put("DalvikSize", FormatSize(rawSize.get("DalvikSize")));
        return result;
    }

    public String GetTotalSize() {
        Long rawTotalSize, rawPackageSize = new Long(0), rawDataSize = new Long(0), rawDalvikSize = new Long(0);
        Map<String, Long> rawSize = Size.GetSize();
        rawTotalSize = new Long(rawSize.get("PackageSize") + rawSize.get("DataSize") + rawSize.get("DalvikSize"));
        return FormatSize(rawTotalSize);
    }

    public boolean GetChecked() {
        return IsChecked;
    }

    public void SetAppName(String AppName) {
        this.AppName = AppName;
    }

    public void SetPackageName(String PackageName) {
        this.PackageName = PackageName;
    }

    public void SetIcon(Drawable Icon) {
        this.Icon = Icon;
    }

    public void SetSize(long PackageSize, long DataSize, long DalvikSize) {
        Size.SetSize(PackageSize, DataSize, DalvikSize);
    }

    public void SetChecked(boolean IsChecked) {
        this.IsChecked = IsChecked;
    }

    @Override
    public int compareTo(Object another) {
        MyAppInfo myAppInfo = (MyAppInfo) another;
        String anotherAppName = myAppInfo.GetAppName();
        if (IsChinese(anotherAppName)) {
            if (IsChinese(this.AppName)) {
                return Collator.getInstance(Locale.CHINESE).compare(this.AppName, anotherAppName);
            }
            return -1;
        }
        if (IsChinese(this.AppName)) {
            return 1;
        }
        return this.AppName.compareToIgnoreCase(anotherAppName);
    }

    private static boolean IsChinese(String str) {
        char ch = str.charAt(0);
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(ch);
        return (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION);
    }

    private String FormatSize(Long rawSize) {
        int i;
        String level[] = new String[4];
        double rawSz = rawSize;
        String Size;
        level[0] = " B";
        level[1] = " KB";
        level[2] = " MB";
        level[3] = " GB";
        for (i = 0; i < 4 && rawSz > 1024; i++) {
            rawSz /= 1024;
        }
        Size = String.format("%.2f", rawSz) + level[i];
        return Size;
    }

    private class AppSizeInfo {
        private long PackageSize;
        private long DataSize;
        private long DalvikSize;

        public AppSizeInfo() {
            PackageSize = DataSize = DalvikSize = 0;
        }

        public AppSizeInfo(AppSizeInfo Size) {
            this.PackageSize = Size.PackageSize;
            this.DataSize = Size.DataSize;
            this.DalvikSize = Size.DalvikSize;
        }

        public void SetSize(long PackageSize, long DataSize, long DalvikSize) {
            this.PackageSize = PackageSize;
            this.DataSize = DataSize;
            this.DalvikSize = DalvikSize;
        }

        public Map<String, Long> GetSize() {
            Map<String, Long> result = new HashMap<String, Long>();
            result.put("PackageSize", this.PackageSize);
            result.put("DataSize", this.DataSize);
            result.put("DalvikSize", this.DalvikSize);
            return result;
        }
    }
}
