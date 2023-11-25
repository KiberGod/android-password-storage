package com.kibergod.passwordstorage.ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;

import com.kibergod.passwordstorage.R;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {
    // Функція встановлює обмеження у кількості символів для заданого поля вводу
    public void setMaxLengthForInput(View view, int id, int max_value) {
        EditText editText = view.findViewById(id);
        setMaxLengthForInput(editText, max_value);
    }

    public void setMaxLengthForInput(EditText editText, int max_value) {
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

    // Повертає текст відсутності категорії для кнопки з списком категорій
    public String setEmptyCategoryText() { return "Відсутня"; }

    // Функція парсить текстові значення з полів вводу
    public ArrayList<String> getStringsArray(ArrayList<EditText> editsArray) {
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (EditText editText : editsArray) {
            String text = editText.getText().toString();
            stringArrayList.add(text);
        }
        return stringArrayList;
    }

    // Повертає EditText для створення нового поля
    public EditText getEditText(Context context, String hint, int maxLength) {
        EditText editText = new EditText(context);
        editText.setHint(hint);
        editText.setTextColor(ContextCompat.getColor(context, R.color.white));
        editText.setHintTextColor(ContextCompat.getColor(context, R.color.gray_text));

        setMaxLengthForInput(editText, maxLength);

        return editText;
    }

    public ViewGroup.MarginLayoutParams getParamsForValidLine(Context context, ViewGroup.MarginLayoutParams params, int dp) {
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
