package com.example.passwordstorage.data;

import static com.example.passwordstorage.NativeController.getCategories;
import static com.example.passwordstorage.NativeController.saveCategories;

import androidx.lifecycle.ViewModel;

import com.example.passwordstorage.model.Category;
import com.example.passwordstorage.model.Record;

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

    // Повертає всю категорію за id
    private Category getCategoryById(Integer id) {
        for (Category category : categories) {
            if (category.getId().equals(id)) {
                return category;
            }
        }
        return null;
    }

    // Повертає назву категорії за ідентифікатором (або попрожнє значення, якщо такої категорії немає)
    public String getCategoryNameById(Integer id) {
        Category category = getCategoryById(id);
        if (category != null) {
            return category.getName();
        } else {
            return "";
        }
    }

    // Повертає назву категорії за індексом
    public String getCategoryNameByIndex(int index) {
        return categories.get(index).getName();
    }

    // Повертає id іконки категорії за індексом
    public int getCategoryIconIdByIndex(int index) {
        return categories.get(index).getIconId();
    }

    // Повертає id по індексу
    public Integer getCategoryIdByIndex(int index) {return categories.get(index).getId(); }

    // Пошук id категорії за назвою
    public int getCategoryIdByName(String categoryName) {
        for (Category category : categories) {
            if (category.getName().equals(categoryName)) {
                return category.getId();
            }
        }
        return -1;
    }

    // Повертає загальну кількість категоірй
    public int getCategoriesCount() {
        return categories.size();
    }

    // Логування категорій, отриманих з С++
    public void printLogCategories() {
        System.out.println("----- BEGIN CATEGORY LOGS -----");
        for (Category category : categories) {
            System.out.println("Id: " + category.getId());
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

    /*
     *  Перевірка нової категорії на унікальність (за полем імені) з використанням параметру для ігнорування
     *  певного рядка під час перевірки
     */
    public boolean checkCategoryNameUnique(String name, int index) {
        Category selfCategory = categories.get(index);
        for (Category category : categories) {
            if (!category.equals(selfCategory) && category.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    // Створення новогої категорії да додавання її до списку
    public void addCategory(String name, int icon_id) {
        Category category = new Category(generateNewId(), name, icon_id);
        categories.add(category);
        saveCategories(categories);
    }

    // Редагування категорії
    public void editCategory(int index, String name, int icon_id) {
        categories.get(index).update(name, icon_id);
        saveCategories(categories);
    }

    // Видалення категорії
    public void deleteCategory(int index) {
        categories.remove(index);
        saveCategories(categories);
    }

    // Функція створює новий ідентифікатор на основі існуючого найстаршого
    private Integer generateNewId() {
        if (categories.size() != 0) {
            return categories.get(categories.size() - 1).getId() +1;
        } else {
            return 0;
        }
    }

    // Перевірка на наявність іконки категорії
    public boolean isEmptyIconId(int index) {
        if (categories.get(index).getIconId() == Category.NULL_ICON_ID_VALUE) {
            return true;
        } else {
            return false;
        }
    }
}
