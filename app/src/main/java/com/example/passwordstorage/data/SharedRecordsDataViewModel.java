package com.example.passwordstorage.data;

import static com.example.passwordstorage.NativeController.getRecords;
import static com.example.passwordstorage.NativeController.saveRecords;

import androidx.lifecycle.ViewModel;

import com.example.passwordstorage.model.Record;

import java.util.ArrayList;

/*
    Дана Вью-модель є спільною для всіх компонентів. Зберігає в собі дані про записи та функції роботи з ними

    Для підключення моделі слід використовувати (для Activity)   viewModel = new ViewModelProvider(this).get(SharedRecordsDataViewModel.class);
                                                (для Fragment)   viewModel = new ViewModelProvider(requireActivity()).get(SharedRecordsDataViewModel.class);
                                                      замість    viewModel = new ViewModelProvider(this).get(SharedRecordsDataViewModel.class);
    щоб уникнути конфліктів використання різними модулями.
 */
public class SharedRecordsDataViewModel extends ViewModel {

    private ArrayList<Record> records;


    // Ініціалізація списку записів
    public void setRecords() {
        records = getRecords();
    }

    // Повертає назву запису за індексом у списку
    public String getRecordTitleByIndex(int index) { return records.get(index).getTitle(); }

    // Повертає текст запису за індексом у списку
    public String getRecordTextByIndex(int index) { return records.get(index).getText(); }

    // Повертає id категорії запису за індексом у списку
    public Integer getRecordCategory_idByIndex(int index) { return records.get(index).getCategoryId(); }

    // Повертає загальну кількість записів
    public int getRecordsCount() {
        return records.size();
    }

    // Повертає інформацію про закладку у заданого запису
    public boolean getBookmarkByIndex(int index) {
        return records.get(index).getBookmark();
    }

    // Логування записів, отриманих з С++
    public void printLogRecords() {
        System.out.println("----- BEGIN RECORD LOGS -----");
        for (Record record : records) {
            System.out.println("Title: " + record.getTitle());
            System.out.println("Text: " + record.getText());
            System.out.println("Category id: " + record.getCategoryId());
            System.out.println("Bookmark: " + record.getBookmark());
            System.out.println();
        }
        System.out.println("----- END RECORD LOGS -----");
    }

    // Перевірка нового запису на унікальність (за полем заголовку)
    public boolean checkRecordTitleUnique(String title) {
        return checkRecordTitleUnique(title, "");
    }

    /*
     *  Перевірка нового запису на унікальність (за полем заголовку) з використанням параметру для ігнорування
     *  певного рядка під час перевірки
     */
    public boolean checkRecordTitleUnique(String title, String strIgnore) {
        for (Record record : records) {
            if (record.getTitle().equals(title) && !record.getTitle().equals(strIgnore)) {
                return false;
            }
        }
        return true;
    }

    // Створення нового запису да додавання його до списку
    public void addRecord(String title, String text,  Integer category_id) {
        Record record = new Record(title, text, category_id);
        records.add(record);
        saveRecords(records);
    }

    // Редагування запису
    public void editRecord(int index, String newTitle, String newText,  Integer newCategory_id) {
        records.get(index).update(newTitle, newText, newCategory_id);
        saveRecords(records);
    }

    // Видалення запису
    public void deleteRecord(int index) {
        records.remove(index);
        saveRecords(records);
    }

    // Повертає кількість записів, що мають задану категорію
    public int getRecordCountByCategoryId(Integer category_id) {
        int recordCounter = 0;
        for (Record record: records) {
            if (record.getCategoryId().equals(category_id)) {
                recordCounter++;
            }
        }
        return recordCounter;
    }

    // Від`єднує вказану категорію від всіх записів, де вона міститься
    public void detachCategory(Integer category_id) {
        for (Record record: records) {
            if (record.getCategoryId().equals(category_id)) {
                record.setEmptyCategoryId();
            }
        }
    }

    // Редагування статусу закладки у записі
    public void editBookmarkInRecordByIndex(int index) {
        records.get(index).inversionBookmark();
        saveRecords(records);
    }
}
