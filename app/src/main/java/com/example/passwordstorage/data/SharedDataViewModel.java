package com.example.passwordstorage.data;

import static com.example.passwordstorage.NativeController.getCategories;
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

    // Повертає категорію запису за ідентифікатором (застаріле, у майбутніх версіях буде змінено)
    public String getRecordCategoryById(int index) { return records.get(index).getCategory(); }

    // Повертає загальну кількість записів
    public int getRecordsCount() {
        return records.size();
    }

    // Логування записів, отриманих з С++
    public void printLogRecords() {
        System.out.println("----- BEGIN RECORD LOGS -----");
        for (Record record : records) {
            System.out.println("Title: " + record.getTitle());
            System.out.println("Text: " + record.getText());
            System.out.println("Category: " + record.getCategory());
            System.out.println();
        }
        System.out.println("----- END RECORD LOGS -----");
    }


    // Повертає весь список категорій
    public ArrayList<Category> getAllCategories() { return categories; }

    // Повертає назву категорії за ідентифікатором
    public String getCategoryNameById(int index) { return categories.get(index).getName(); }

    // Ініціалізація списку категорій
    public void setCategories() {
        categories = getCategories();
    }

    // Повертає загальну кількість категоірй
    public int getCategoriesCount() {
        return categories.size();
    }

    // Логування категорій, отриманих з С++
    public void printLogCategories() {
        System.out.println("----- BEGIN CATEGORY LOGS -----");
        for (Category category : categories) {
            System.out.println("Name: " + category.getName());
            System.out.println();
        }
        System.out.println("----- END CATEGORY LOGS -----");
    }

    // Перевірка нової категорії на унікальність (за полем імені)
    public boolean checkNameUnique(String name) {
        for (Category category : categories) {
            if (category.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    // Створення новогої категорії да додавання її до списку
    public void addCategory(String name) {
        Category category = new Category(name);
        categories.add(category);
    }

}