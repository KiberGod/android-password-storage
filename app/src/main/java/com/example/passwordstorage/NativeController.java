package com.example.passwordstorage;

import android.content.Context;

import com.example.passwordstorage.model.Calculator;
import com.example.passwordstorage.model.Category;
import com.example.passwordstorage.model.DigitalOwner;
import com.example.passwordstorage.model.Record;
import com.example.passwordstorage.model.Settings;

import java.util.ArrayList;

/*
    Класс-контролер, що виступає посередником між java-кодом та нативними C++ методами
 */

public class NativeController {
    static {
        System.loadLibrary("security_core");
    }

    public static native void initSecurityCore(Context context);
    public static native ArrayList<Record> getRecords();
    public static native ArrayList<Category> getCategories();
    public static native Settings getSettings();
    public static native Calculator getCalculator();
    public static native DigitalOwner getDigitalOwner();
    public static native void saveCategories(ArrayList<Category> categories);
    public static native void saveRecords(ArrayList<Record> records);
    public static native void saveSettings(Settings settings);
    public static native void saveCalculator(Calculator calculator);
    public static native void saveDigitalOwner(DigitalOwner digitalOwner);

    public static native void destroyUserData();
    public static native void hideUserData();
    public static native void retrieveHiddenRecords();
    public static native void retrieveHiddenCategories();
}
