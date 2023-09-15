package com.example.passwordstorage.ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;

import com.example.passwordstorage.R;

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
                    editText.removeTextChangedListener(this);
                    byte[] bytes = charSequence.toString().getBytes();
                    if (bytes.length > max_value-1) {
                        String newText = new String(bytes, 0, max_value-1);
                        newText = newText.substring(0, newText.length() - 1);
                        editText.setText(newText);
                        editText.setSelection(newText.length());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    editText.addTextChangedListener(this);
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
}
