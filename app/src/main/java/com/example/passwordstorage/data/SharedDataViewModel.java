package com.example.passwordstorage.data;

import static com.example.passwordstorage.NativeController.getRecords;

import androidx.lifecycle.ViewModel;

import com.example.passwordstorage.model.Category;
import com.example.passwordstorage.model.Record;

import java.util.ArrayList;

/*
    Дана Вью-модель є спільною для всіх компонентів. Зберігає в собі основні дані та функції роботи з ними

    Для підключення моделі слід використовувати    viewModel = new ViewModelProvider(requireActivity()).get(SharedDataViewModel.class);
                                        замість    viewModel = new ViewModelProvider(this).get(SharedDataViewModel.class);
    щоб уникнути конфліктів використання різними модулями.
 */
public class SharedDataViewModel extends ViewModel {

    private ArrayList<Record> records;

    private ArrayList<Category> categories;

    // Ініціалізація списку записів
    public void setRecords() {
        records = getRecords();
    }

    // Повертає назву запису за ідентифікатором
    public String getRecordTitleById(int index) { return records.get(index).getTitle(); }

    // Повертає текст запису за ідентифікатором
    public String getRecordTextById(int index) { return records.get(index).getText(); }

    // Повертає категорію запису за ідентифікатором
    public String getRecordCategoryById(int index) { return records.get(index).getCategory(); }

    // Повертає загальну кількість записів
    public int getRecordsCount() {
        return records.size();
    }

    // Тестова функція, що друкує дані (записи) отримані з С++
    public void printLogRecords() {
        for (Record record : records) {
            System.out.println("Title: " + record.getTitle());
            System.out.println("Text: " + record.getText());
            System.out.println("Category: " + record.getCategory());
            System.out.println();
        }
    }
}