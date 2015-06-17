package com.github.mvapp2sd;

import android.graphics.drawable.Drawable;

import java.text.Collator;
import java.util.Locale;

/**
 * Created by EternalPhane on 2015/6/13.
 * GitHub: https://github.com/EternalPhane
 */
public class MyAppInfo implements Comparable {
    private String AppName;
    private String PackageName;
    private Drawable Icon;
    private boolean IsChecked;

    public MyAppInfo() {
        IsChecked = false;
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

    public void SetChecked(boolean IsChecked) {
        this.IsChecked = IsChecked;
    }

    public static boolean IsChinese(String str) {
        char ch = str.charAt(0);
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(ch);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
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
}
