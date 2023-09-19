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
    public boolean checkRecordTitleUnique(String title, int index) {
        Record selfRecord = records.get(index);
        for (Record record : records) {
            if (!record.equals(selfRecord) && record.getTitle().equals(title)) {
                return false;
            }
        }
        return true;
    }

    // Створення нового запису да додавання його до списку
    public void addRecord(String title, String text,  Integer category_id, int icon_id, ArrayList<String> fieldNames, ArrayList<String> fieldValues) {
        Record record = new Record(title, text, category_id, icon_id);
        record.setFields(fieldNames, fieldValues);
        records.add(record);
        saveRecords(records);
    }

    // Редагування запису
    public void editRecord(int index, String newTitle, String newText,  Integer newCategory_id, int newIcon_id, ArrayList<String> newFieldNames, ArrayList<String> newFieldValues) {
        records.get(index).update(newTitle, newText, newCategory_id, newIcon_id, newFieldNames, newFieldValues);
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

    // Повертає id іконки запису за індексом
    public int getRecordIconIdByIndex(int index) {
        return records.get(index).getIconId();
    }

    // Перевірка на наявність іконки запису
    public boolean isEmptyIconId(int index) {
        if (records.get(index).getIconId() == Record.NULL_ICON_ID_VALUE) {
            return true;
        } else {
            return false;
        }
    }

    /* Перевіряє, чи треба заміняти порожню іконку запису іконкою категорії (якщо така є)
     * true - іконка повинна бути замінена
     * false - іконку заміняти не треба або нічим
     */
    public boolean needSetCategoryIconByIndex(int index) {
        if (isEmptyIconId(index) && !records.get(index).getCategoryId().equals(Record.NULL_CATEGORY_VALUE)) {
            return true;
        } else {
            return false;
        }
    }

    // Повертає ім`я поля запису за ідентифікатором поля та записа
    public String getRecordFieldNameByIndex(int recordIndex, int fieldIndex) {
        return records.get(recordIndex).getFields()[fieldIndex].getName();
    }

    // Повертає значення поля запису за ідентифікатором поля та записа
    public String getRecordFieldValueByIndex(int recordIndex, int fieldIndex) {
        return records.get(recordIndex).getFields()[fieldIndex].getValue();
    }

    // Повертає захищене значення поля запису за ідентифікатором поля та записа
    public String getRecordFieldProtectedValueByIndex(int recordIndex, int fieldIndex) {
        return records.get(recordIndex).getFields()[fieldIndex].getProtectedgetValue();
    }

    // Повертає налаштування значення поля запису за ідентифікатором поля та записа
    public boolean getRecordFieldValueVisibilityByIndex(int recordIndex, int fieldIndex) {
        return records.get(recordIndex).getFields()[fieldIndex].getValueVisibility();
    }

    // Редагування налаштування значення поля запису за ідентифікатором поля та записа
    public void setRecordFieldValueVisibilityByIndex(int recordIndex, int fieldIndex) {
        records.get(recordIndex).getFields()[fieldIndex].inversionValueVisibility();
        saveRecords(records);
    }

}
