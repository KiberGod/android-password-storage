package com.kibergod.passwordstorage.ui.pages;

import android.widget.EditText;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {
    // Функція парсить текстові значення з полів вводу
    public ArrayList<String> getStringsArray(ArrayList<EditText> editsArray) {
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (EditText editText : editsArray) {
            String text = editText.getText().toString();
            stringArrayList.add(text);
        }
        return stringArrayList;
    }
}