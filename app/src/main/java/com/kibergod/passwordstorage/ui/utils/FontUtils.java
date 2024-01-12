package com.kibergod.passwordstorage.ui.utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

public class FontUtils {
    public static void setFontSizeToView(@NotNull Context context, @NotNull View view, int viewId, float sizeInDp) {
        float scale = context.getResources().getDisplayMetrics().density;
        int sizeInPixels = (int) (sizeInDp * scale + 0.5f);

        View viewObj = view.findViewById(viewId);
        if (viewObj instanceof EditText) {
            setFontSizeToEditText((EditText) viewObj, sizeInPixels);
        } else if (viewObj instanceof TextView) {
            setFontSizeToTextView((TextView) viewObj, sizeInPixels);
        } else if (view instanceof Switch) {
            setFontSizeToSwitch((Switch) viewObj, sizeInPixels);
        } else if (view instanceof RadioButton) {
            setFontSizeToRadioButton((RadioButton) view, sizeInPixels);
        } else if (view instanceof Button) {
            setFontSizeToButton((Button) view, sizeInPixels);
        }
    }

    private static void setFontSizeToTextView(@NotNull TextView textView, int sizeInPixels) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeInPixels);
    }

    private static void setFontSizeToEditText(@NotNull EditText editText, int sizeInPixels) {
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeInPixels);
    }

    private static void setFontSizeToSwitch(@NotNull Switch switchView, int sizeInPixels) {
        switchView.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeInPixels);
    }

    private static void setFontSizeToButton(@NotNull Button buttonView, int sizeInPixels) {
        buttonView.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeInPixels);
    }

    private static void setFontSizeToRadioButton(@NotNull RadioButton radioButtonView, int sizeInPixels) {
        radioButtonView.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeInPixels);
    }
}
