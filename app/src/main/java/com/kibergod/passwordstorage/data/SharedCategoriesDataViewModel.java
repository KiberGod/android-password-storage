package com.kibergod.passwordstorage.data;

import static com.kibergod.passwordstorage.NativeController.saveCategories;

import androidx.lifecycle.ViewModel;

import com.kibergod.passwordstorage.NativeController;
import com.kibergod.passwordstorage.model.Category;
import com.kibergod.passwordstorage.model.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
        categories = NativeController.getCategories();
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

    // Повертає індекс категорії за ідентифікатором
    private int getCategoryIndexById(int id) {
        for (int index = 0; index < categories.size(); index++) {
            if (categories.get(index).getId().equals(id)) {
                return index;
            }
        }
        return -1;
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

    // Повертає назву категорії за ідентифікатором
    public String getCategoryNameById(int id) { return categories.get(getCategoryIndexById(id)).getName(); }

    // Повертає дату створення категорії за ідентифікатором
    public String getCategoryCreated_atById(int id) { return categories.get(getCategoryIndexById(id)).getCreated_at();}

    // Повертає останню дату оновлення категорії за ідентифікатором
    public String getCategoryUpdated_atById(int id) { return categories.get(getCategoryIndexById(id)).getUpdated_at(); }

    // Повертає останню дату перегляду категорії за ідентифікатором
    public String getCategoryViewed_atById(int id) { return categories.get(getCategoryIndexById(id)).getViewed_at(); }

    // Оновлює останню дату перегляду категорії за ідентифікатором
    public void updateCategoryViewed_atById(int id, int filtersParam, boolean filtersSorting) {
        categories.get(getCategoryIndexById(id)).setViewed_at();
        sortCategories(filtersParam, filtersSorting);
        saveCategories(categories);
    }

    // Пошук id категорії за назвою
    public int getCategoryIdByName(String categoryName) {
        for (Category category : categories) {
            if (category.getName().equals(categoryName)) {
                return category.getId();
            }
        }
        return -1;
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
    public boolean checkCategoryNameUnique(String name, int id) {
        Category selfCategory = categories.get(getCategoryIndexById(id));
        for (Category category : categories) {
            if (!category.equals(selfCategory) && category.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    // Створення новогої категорії да додавання її до списку
    public void addCategory(String name, String icon_id, int filtersParam, boolean filtersSorting) {
        Category category = new Category(generateNewId(), name, icon_id, new DateTime(), new DateTime(), new DateTime());
        categories.add(category);
        sortCategories(filtersParam, filtersSorting);
        saveCategories(categories);
    }

    // Редагування категорії
    public void editCategory(int id, String name, String icon_id, int filtersParam, boolean filtersSorting) {
        categories.get(getCategoryIndexById(id)).update(name, icon_id);
        categories.get(getCategoryIndexById(id)).setUpdated_at();
        sortCategories(filtersParam, filtersSorting);
        saveCategories(categories);
    }

    // Видалення категорії
    public void deleteCategory(int id) {
        categories.remove(getCategoryIndexById(id));
        saveCategories(categories);
    }

    // Функція створює новий ідентифікатор на основі існуючого найстаршого
    private Integer generateNewId() {
        int newId = -1;
        for (Category category: categories) {
            if (category.getId() > newId) {
                newId = category.getId();
            }
        }
        return newId + 1;
    }

    // Повертає id іконки за id категорії
    public String getCategoryIconIdById(int id) {
        return getCategoryById(id).getIconId();
    }

    // Безумовно повертає всі існуючі категорії
    public ArrayList<Category> getCategories() {
        return categories;
    }

    // Повертає всі категорії, що містять заданий пошуковий параметр у назві (з ігноруванням регістру)
    public ArrayList<Category> getCategories(String searchPram) {
        if (searchPram.equals("")) {
            return getCategories();
        }

        ArrayList<Category> foundCategories = new ArrayList<>();
        for (Category category: categories) {
            if (category.getName().toLowerCase().contains(searchPram.toLowerCase())) {
                foundCategories.add(category);
            }
        }
        return foundCategories;
    }

    /*
     * Сортування категорій
     *
     * filtersParam - критерій сортування
     *      1 - за датою редагування
     *      2 - за датою перегляду
     *      3 - за датою створення
     *
     *  filtersSorting - порядок сортування
     *      true  - від найстаріших
     *      false - від найновіших
     */
    public void sortCategories(int filtersParam, boolean filtersSorting) {
        Collections.sort(categories, new Comparator<Category>() {
            @Override
            public int compare(Category category1, Category category2) {
                long value1, value2;
                switch (filtersParam) {
                    case 1:
                        value1 = category1.getUpdated_atInMillis();
                        value2 = category2.getUpdated_atInMillis();
                        break;
                    case 2:
                        value1 = category1.getViewed_atInMillis();
                        value2 = category2.getViewed_atInMillis();
                        break;
                    case 3:
                    default:
                        value1 = category1.getCreated_atInMillis();
                        value2 = category2.getCreated_atInMillis();
                        break;
                }

                int result = Long.compare(value1, value2);
                return filtersSorting ? result : -result;
            }
        });
    }

    // Отримання часової мітки з урахуванням параметру фільтрації
    public String getCategoryAction_atById(int id, int filtersParam) {
        switch (filtersParam) {
            case 1:
                return categories.get(getCategoryIndexById(id)).getUpdated_at();
            case 2:
                return categories.get(getCategoryIndexById(id)).getViewed_at();
            case 3:
            default:
                return categories.get(getCategoryIndexById(id)).getCreated_at();
        }
    }
}