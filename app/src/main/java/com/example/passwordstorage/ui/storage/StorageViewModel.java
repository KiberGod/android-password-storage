package com.example.passwordstorage.ui.storage;

import static com.example.passwordstorage.NativeController.getRecords;

import androidx.lifecycle.ViewModel;

import com.example.passwordstorage.Record;

import java.util.ArrayList;

/*
    Дана модель є спільною для всіх компонентів пакету storage.

    Для підключення моделі слід використовувати    viewModel = new ViewModelProvider(requireActivity()).get(StorageViewModel.class);
                                        замість    viewModel = new ViewModelProvider(this).get(StorageViewModel.class);
    щоб уникнути конфліктів використання різними модулями.
 */
public class StorageViewModel extends ViewModel {

    private ArrayList<Record> records;

    // Ініціалізація списку записів
    public void setRecords() {
        records = getRecords();
    }

    // Повертає назву запису за ідентифікатором
    public String getRecordTitleById(int index) { return records.get(index).getTitle(); }

    // Повертає текст запису за ідентифікатором
    public String getRecordTextById(int index) { return records.get(index).getText(); }

    // Повертає загальну кількість записів
    public int getRecordsCount() {
        return records.size();
    }

    // Тестова функція, що друкує дані (записи) отримані з С++
    public void printLogRecords() {
        System.out.println("+++++++++++++++++++++++++++++++ВВВВВВВВВВ");
        for (Record record : records) {
            System.out.println("Title: " + record.getTitle());
            System.out.println("Text: " + record.getText());
            System.out.println("Category: " + record.getCategory());
            System.out.println();
        }
    }
}