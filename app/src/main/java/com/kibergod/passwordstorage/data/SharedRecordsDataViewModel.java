package com.kibergod.passwordstorage.data;

import static com.kibergod.passwordstorage.NativeController.getRecords;
import static com.kibergod.passwordstorage.NativeController.saveCategories;
import static com.kibergod.passwordstorage.NativeController.saveRecords;

import androidx.lifecycle.ViewModel;

import com.kibergod.passwordstorage.model.Record;

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

    // Повертає індекс запису за ідентифікатором
    private int getRecordIndexById(int id) {
        for (int index = 0; index < records.size(); index++) {
            if (records.get(index).getId() == id) {
                return index;
            }
        }
        return -1;
    }

    // Повертає ідентифікатор за індексом (повинно бути вирізано у наступних етапах)
    public int getRecordIdByIndex(int index) {
        return records.get(index).getId();
    }

    // Повертає назву запису за ідентифікатором у списку
    public String getRecordTitleById(int id) { return records.get(getRecordIndexById(id)).getTitle(); }

    // Повертає текст запису за ідентифікатором у списку
    public String getRecordTextById(int id) { return records.get(getRecordIndexById(id)).getText(); }

    // Повертає id категорії запису за ідентифікатором у списку
    public Integer getRecordCategory_idById(int id) { return records.get(getRecordIndexById(id)).getCategoryId(); }

    // Повертає загальну кількість записів
    public int getRecordsCount() {
        return records.size();
    }

    // Повертає інформацію про закладку у заданого запису
    public boolean getBookmarkById(int id) {
        return records.get(getRecordIndexById(id)).getBookmark();
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
        for (Record record : records) {
            if (record.getTitle().equals(title)) {
                return false;
            }
        }
        return true;
    }

    /*
     *  Перевірка нового запису на унікальність (за полем заголовку) з використанням параметру для ігнорування
     *  певного рядка під час перевірки
     */
    public boolean checkRecordTitleUnique(String title, int id) {
        Record selfRecord = records.get(getRecordIndexById(id));
        for (Record record : records) {
            if (!record.equals(selfRecord) && record.getTitle().equals(title)) {
                return false;
            }
        }
        return true;
    }

    // Створення нового запису да додавання його до списку
    public void addRecord(String title, String text,  Integer category_id, String icon_id, ArrayList<String> fieldNames, ArrayList<String> fieldValues) {
        Record record = new Record(generateNewId(), title, text, category_id, icon_id);
        record.setFields(fieldNames, fieldValues);
        records.add(record);
        saveRecords(records);
    }

    // Редагування запису
    public void editRecord(int id, String newTitle, String newText,  Integer newCategory_id, String newIcon_id, ArrayList<String> newFieldNames, ArrayList<String> newFieldValues) {
        records.get(getRecordIndexById(id)).update(newTitle, newText, newCategory_id, newIcon_id, newFieldNames, newFieldValues);
        records.get(getRecordIndexById(id)).setUpdated_at();
        saveRecords(records);
    }

    // Видалення запису
    public void deleteRecord(int id) {
        records.remove(getRecordIndexById(id));
        saveRecords(records);
    }

    // Повертає список записів, що мають задану категорію
    public ArrayList<Record> getRecordsByCategoryId(Integer category_id) {
        ArrayList<Record> dependentRecords = new ArrayList<>();
        for (Record record: records) {
            if (record.getCategoryId().equals(category_id)) {
                dependentRecords.add(record);
            }
        }
        return dependentRecords;
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
        saveRecords(records);
    }

    // Редагування статусу закладки у записі
    public void editBookmarkInRecordById(int id) {
        records.get(getRecordIndexById(id)).inversionBookmark();
        saveRecords(records);
    }

    // Редагування глобального значення захисту запису
    public void editToTalValueVisibilityInRecordById(int id) {
        records.get(getRecordIndexById(id)).inversionTotalValueVisibility();
        saveRecords(records);
    }

    // Повертає id іконки запису за ідентифікатором
    public String getRecordIconIdById(int id) { return records.get(getRecordIndexById(id)).getIconId(); }

    // Повертає дату створення запису за ідентифікатором
    public String getRecordCreated_atById(int id) { return records.get(getRecordIndexById(id)).getCreated_at();}

    // Повертає останню дату оновлення запису за ідентифікатором
    public String getRecordUpdated_atById(int id) { return records.get(getRecordIndexById(id)).getUpdated_at(); }

    // Повертає останню дату перегляду запису за ідентифікатором
    public String getRecordViewed_atById(int id) { return records.get(getRecordIndexById(id)).getViewed_at(); }

    // Повертає глобальне значення захисту запису за ідентифікатором
    public boolean getRecordToTalValueVisibilityById(int id) { return records.get(getRecordIndexById(id)).getTotalValueVisibility(); }

    // Оновлює останню дату перегляду запису за ідентифікатором
    public void updateRecordViewed_atById(int id) {
        records.get(getRecordIndexById(id)).setViewed_at();
        saveRecords(records);
    }

    /* Перевіряє, чи треба заміняти порожню іконку запису іконкою категорії (якщо така є)
     * true - іконка повинна бути замінена
     * false - іконку заміняти не треба або нічим
     */
    public boolean needSetCategoryIconById(int id) {
        if (records.get(getRecordIndexById(id)).getIconId().equals(Record.NULL_ICON_ID_VALUE) && !records.get(getRecordIndexById(id)).getCategoryId().equals(Record.NULL_CATEGORY_VALUE)) {
            return true;
        }
        return false;
    }

    // Повертає ім`я поля запису за ідентифікатором поля та записа
    public String getRecordFieldNameById(int id, int fieldIndex) {
        return records.get(getRecordIndexById(id)).getFields()[fieldIndex].getName();
    }

    // Повертає значення поля запису за ідентифікатором поля та записа
    public String getRecordFieldValueById(int id, int fieldIndex) {
        return records.get(getRecordIndexById(id)).getFields()[fieldIndex].getValue();
    }

    // Повертає захищене значення поля запису за ідентифікатором поля та записа
    public String getRecordFieldProtectedValueById(int id, int fieldIndex) {
        return records.get(getRecordIndexById(id)).getFields()[fieldIndex].getProtectedFieldValue();
    }

    // Повертає налаштування значення поля запису за ідентифікатором поля та записа
    public boolean getRecordFieldValueVisibilityById(int id, int fieldIndex) {
        return records.get(getRecordIndexById(id)).getFields()[fieldIndex].getValueVisibility();
    }

    // Редагування налаштування значення поля запису за ідентифікатором поля та записа
    public void setRecordFieldValueVisibilityById(int id, int fieldIndex) {
        records.get(getRecordIndexById(id)).getFields()[fieldIndex].inversionValueVisibility();
        saveRecords(records);
    }

    // Видалення активних даних
    public void dataDestroy() {
        records.clear();
    }

    // Безумовно конвертує отриману змінну у захищену форму
    public String getRecordProtectedValue(String value) {
        return Record.getProtectedValue(value);
    }

    // Повертає ідентифікатор запису за єкземпляром об`єкта запису
    public int getRecordIdByRecordObj(Record record) {
        for (int i=0; i<records.size(); i++) {
            if (records.get(i).equals(record)) {
                return records.get(i).getId();
            }
        }
        return -1;
    }

    // Функція створює новий ідентифікатор на основі існуючого найстаршого
    private int generateNewId() {
        if (records.size() != 0) {
            return records.get(records.size() - 1).getId() +1;
        } else {
            return 0;
        }
    }
}
