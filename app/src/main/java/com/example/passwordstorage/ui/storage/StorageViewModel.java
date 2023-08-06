package com.example.passwordstorage.ui.storage;

import static com.example.passwordstorage.NativeController.getRecords;

import androidx.lifecycle.ViewModel;

import com.example.passwordstorage.Record;

import java.util.ArrayList;

public class StorageViewModel extends ViewModel {

    // Тестова функція, що друкує дані (записи) отримані з С++
    public void printLogRecords() {
        System.out.println("+++++++++++++++++++++++++++++++ВВВВВВВВВВ");
        ArrayList<Record> records = getRecords();
        for (Record record : records) {
            System.out.println("Title: " + record.getTitle());
            System.out.println("Text: " + record.getText());
            System.out.println("Category: " + record.getCategory());
            System.out.println();
        }
    }
}