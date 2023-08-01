package com.example.passwordstorage;

import android.content.Context;

/*
    Класс-контролер, що виступає посередником між java-кодом та нативними C++ методами
 */

public class NativeController {
    static {
        System.loadLibrary("security_core");
    }

    public static native String getKey();
    public static native void initSecurityCore(Context context);
}
