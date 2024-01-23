package com.kibergod.passwordstorage.data;

import static com.kibergod.passwordstorage.NativeController.destroyUserData;
import static com.kibergod.passwordstorage.NativeController.getDigitalOwner;
import static com.kibergod.passwordstorage.NativeController.saveDigitalOwner;
import static com.kibergod.passwordstorage.model.DigitalOwner.DATA_DELETION_MODE;
import static com.kibergod.passwordstorage.model.DigitalOwner.DEFAULT_YEAR_TRIGGERING;
import static com.kibergod.passwordstorage.model.DigitalOwner.HIDE_MODE;
import static com.kibergod.passwordstorage.model.DigitalOwner.PASSIVE_MODE;
import static com.kibergod.passwordstorage.model.DigitalOwner.PROTECTED_MODE;

import androidx.lifecycle.ViewModel;

import com.kibergod.passwordstorage.model.DigitalOwner;

import java.util.Calendar;

public class SharedDigitalOwnerViewModel extends ViewModel {

    private DigitalOwner digitalOwner;

    // Ініціалізація "Цифрового власника"
    public void setDigitalOwner() { digitalOwner = getDigitalOwner(); }

    public void setMode(int mode ) {
        digitalOwner.setMode(mode);
        if (digitalOwner.getYearTriggering() == DEFAULT_YEAR_TRIGGERING) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 1);
            digitalOwner.setDate(
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.YEAR)
            );
        }
        saveDigitalOwner(digitalOwner);
    }

    public void setPassiveMode() {
        digitalOwner.setMode(PASSIVE_MODE);
        saveDigitalOwner(digitalOwner);
    }

    // Повертає true по співпадінню заданого режиму з встановленним
    public boolean getModeFlag(int mode) {
        if (digitalOwner.getMode() == mode) {
            return  true;
        } else {
            return false;
        }
    }

    public void editDate(int day, int month, int year) {
        digitalOwner.setDate(day, month, year);
        saveDigitalOwner(digitalOwner);
    }

    public long getDateMilliseconds() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, digitalOwner.getYearTriggering());
        calendar.set(Calendar.MONTH, digitalOwner.getMonthTriggering());
        calendar.set(Calendar.DAY_OF_MONTH, digitalOwner.getDayTriggering());
        return calendar.getTimeInMillis();
    }

    public boolean isHideMode(int mode) {
        if (mode == HIDE_MODE) {
            return true;
        } else  {
            return false;
        }
    }

    public boolean secureEntry(String inputPassword, String password, Boolean digitalOwnerStatus) {
        if (digitalOwnerStatus.equals(true) && digitalOwner.getMode() == HIDE_MODE) {          // для цього моду не є необхідним умова дати спрацювання
            return runHideMode(inputPassword, password);
        } else if (digitalOwnerStatus.equals(true) && timeToActivate()) {
            switch (digitalOwner.getMode()) {
                case PROTECTED_MODE:
                    return runProtectedMode(inputPassword, password);
                case DATA_DELETION_MODE:
                    return runDataDeletionMode(inputPassword, password);
                default:
                    return inputPassword.equals(password);
            }
        } else {
            // стандартний процес перевірки паролю
            return inputPassword.equals(password);
        }
    }

    /*
     * Функція порівнює поточну дату з датою активації дій, передбачених режимом цифрового власника
     *
     * Повертає:    false   -   якщо дата активації ще не настала (поточна дата менша за дату активації)
     *              true    -   якщо дата активації настала, або вже пройшла (поточна дата більша або дорівнює даті активації)
     */
    private boolean timeToActivate() {
        Calendar currentDate = Calendar.getInstance();
        Calendar targetDate = Calendar.getInstance();
        targetDate.set(
                digitalOwner.getYearTriggering(),
                digitalOwner.getMonthTriggering(),
                digitalOwner.getDayTriggering()
        );
        return currentDate.after(targetDate) || currentDate.equals(targetDate);
    }

    private boolean runHideMode(String inputPassword, String password) {
        StringBuilder newPassword = new StringBuilder(password).reverse();
        if (inputPassword.equals(newPassword.toString())) {
            setRetrieveRecords(true);
            digitalOwner.resetSettings();    // після відновлення даних Цифровий власник переводиться у пассивний режим шляхом скидання налаштуваннь
            saveDigitalOwner(digitalOwner);
            return true;
        } else {
            return inputPassword.equals(password);
        }
    }

    private boolean runProtectedMode(String inputPassword, String password) {
        StringBuilder newPassword = new StringBuilder(password).reverse();
        return inputPassword.equals(newPassword.toString());
    }

    private boolean runDataDeletionMode(String inputPassword, String password) {
        if (inputPassword.equals(password)) {
            destroyUserData();
            digitalOwner.resetSettings();    // після видалення даних Цифровий власник переводиться у пассивний режим шляхом скидання налаштуваннь
            saveDigitalOwner(digitalOwner);
            return true;
        } else {
            return false;
        }
    }

    public void setRetrieveRecords(boolean value) {
        DigitalOwner.retrieveRecords = value;
    }

    public boolean getRetrieveRecords() {
        return  DigitalOwner.retrieveRecords;
    }
}