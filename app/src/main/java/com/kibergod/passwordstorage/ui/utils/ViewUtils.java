package com.kibergod.passwordstorage.ui.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;

import org.jetbrains.annotations.NotNull;

public class ViewUtils {

    // Загальна функція встановлення обровника події натиснення будь-якого Dialog-компонента
    public static void setOnClickToDialog(@NotNull Dialog dialog, int buttonId, @NotNull Runnable action) {
        View button = dialog.findViewById(buttonId);
        setOnClickToObj(button, action);
    }

    // Загальна функція встановлення обровника події натиснення будь-якого View-компонента
    public static void setOnClickToView(@NotNull View view, int buttonId, @NotNull Runnable action) {
        View button = view.findViewById(buttonId);
        setOnClickToObj(button, action);
    }

    private static void setOnClickToObj(View button, @NotNull Runnable action) {
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    action.run();
                }
            });
        }
    }

    // Загальна функція встановлення обровника події довгого натиснення кнопки
    public static void setOnLongClickToView(@NotNull View view, int buttonId, @NotNull Runnable action) {
        View button = view.findViewById(buttonId);
        if (button != null) {
            button.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    action.run();
                    return true;
                }
            });
        }
    }

    // Згорнути / розгорнути картку-випадаюче меню
    public static void setOnClickToDropdownView(@NotNull View view, int idViewHead, int idViewBody, Runnable hideAction, Runnable showAction) {
        View viewBody = view.findViewById(idViewBody);
        viewBody.setVisibility(View.GONE);

        setOnClickToView(view, idViewHead, () ->{
            if (viewBody.getVisibility() == View.VISIBLE) {
                viewBody.setVisibility(View.GONE);
                if (hideAction != null) {
                    hideAction.run();
                }
            } else {
                viewBody.setVisibility(View.VISIBLE);
                if (showAction != null) {
                    showAction.run();
                }
            }
        });
    }

    public static void setOnClickToDropdownView(@NotNull View view, int idViewHead, int idViewBody) {
        setOnClickToDropdownView(view, idViewHead, idViewBody, null, null);
    }

    // Автоскролл на початок (верх) сторінки
    public static void setScrollToTop(@NotNull View view, int scrollId) {
        ScrollView scrollView = view.findViewById(scrollId);
        scrollView.smoothScrollTo(0, 0);
    }

    // Автоскролл у самий низ сторінки
    public static void setScrollToBottom(@NotNull View view, int scrollId) {
        ScrollView scrollView = view.findViewById(scrollId);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    // Функція встановлює обмеження у кількості символів для заданого поля вводу
    public static void setMaxLengthForInput(View view, int id, int max_value) {
        EditText editText = view.findViewById(id);
        setMaxLengthForInput(editText, max_value);
    }

    public static void setMaxLengthForInput(EditText editText, int max_value) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Нічого не робимо перед зміною тексту
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (charSequence.toString().getBytes().length > max_value-1) {
                        String originalString = charSequence.toString();
                        byte[] bytes = originalString.getBytes();

                        while (bytes.length > max_value - 1) {
                            int lastCharIndex = originalString.length() - 1;
                            originalString = originalString.substring(0, lastCharIndex);
                            bytes = originalString.getBytes();
                        }
                        editText.removeTextChangedListener(this);
                        editText.setText(originalString);
                        editText.setSelection(originalString.length());
                        editText.addTextChangedListener(this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Нічого не робимо після зміни тексту
            }
        });
    }

    public static ViewGroup.MarginLayoutParams getParamsForValidLine(Context context, ViewGroup.MarginLayoutParams params, int dp) {
        params.topMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        );
        if (dp == 0) {
            params.height = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dp,
                    context.getResources().getDisplayMetrics()
            );
        } else {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        return params;
    }
}