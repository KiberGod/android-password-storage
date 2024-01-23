package com.kibergod.passwordstorage.ui.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;

public class Vibrator {

    public static void vibrate(@NotNull Context context) {
        vibrate(context, 100);
    }

    public static void vibrate(@NotNull Context context, int milliseconds) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
            ((android.os.Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(milliseconds);
        }
    }
}