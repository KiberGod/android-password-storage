package com.example.passwordstorage.ui.create;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.lifecycle.ViewModel;

public class CreateViewModel extends ViewModel {

    // Функція встановлює обмеження у кількості символів для заданого поля вводу
    public void setMaxLengthForInput(View view, int id, int max_value) {
        EditText editText = view.findViewById(id);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Нічого не робимо перед зміною тексту
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    editText.removeTextChangedListener(this); // Убираем слушатель временно
                    byte[] bytes = charSequence.toString().getBytes();
                    if (bytes.length > max_value) {
                        String newText = new String(bytes, 0, max_value);
                        newText = newText.substring(0, newText.length() - 1);
                        editText.setText(newText);
                        editText.setSelection(newText.length());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    editText.addTextChangedListener(this); // Возвращаем слушатель обратно
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Нічого не робимо після зміни тексту
            }
        });
    }

}