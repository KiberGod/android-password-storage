package com.kibergod.passwordstorage.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KeyboardUtils {

    // Функція приховує клавіатуру вводу, якщо та відкрита
    public static void hideKeyboards(@NotNull Context context) {
        View view = getCurrentFocus(context);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Nullable
    private static View getCurrentFocus(@NotNull Context context) {
        if (context instanceof Activity) {
            return ((Activity) context).getCurrentFocus();
        }
        return null;
    }
}
