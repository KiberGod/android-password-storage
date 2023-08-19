package com.example.passwordstorage.data;

import static com.example.passwordstorage.NativeController.getCategories;
import static com.example.passwordstorage.NativeController.saveCategories;

import androidx.lifecycle.ViewModel;

import com.example.passwordstorage.model.Category;

import java.util.ArrayList;

/*
    Дана Вью-модель є спільною для всіх компонентів. Зберігає в собі дані про категорії та функції роботи з ними

    Для підключення моделі слід використовувати (для Activity)   viewModel = new ViewModelProvider(this).get(SharedCategoriesDataViewModel.class);
                                                (для Fragment)   viewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
                                                      замість    viewModel = new ViewModelProvider(this).get(SharedCategoriesDataViewModel.class);
    щоб уникнути конфліктів використання різними модулями.
 */
public class SharedCategoriesDataViewModel extends ViewModel {

    private ArrayList<Category> categories;


    // Ініціалізація списку категорій
    public void setCategories() {
        categories = getCategories();
    }

    // Повертає весь список категорій
    public ArrayList<Category> getAllCategories() { return categories; }

    // Повертає назву категорії за ідентифікатором (або попрожнэ значення, якщо такої категорії немає)
    public String getCategoryNameById(int index) {
        try {
            return categories.get(index).getName();
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
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
    public boolean checkCategoryNameUnique(String name) {
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
        saveCategories(categories);
    }
}
