package com.kibergod.passwordstorage.data;

import static com.kibergod.passwordstorage.NativeController.getSettings;
import static com.kibergod.passwordstorage.NativeController.saveSettings;

import androidx.lifecycle.ViewModel;

import com.kibergod.passwordstorage.model.Settings;

public class SharedSettingsDataViewModel extends ViewModel {

    private Settings settings;

    // Ініціалізація налаштуваннь
    public void setSettings() {
        settings = getSettings();
    }

    // Повертає значення налаштування activityProtection
    public boolean getActivityProtection() {
        return settings.getActivityProtection();
    }

    // Повертає значення налаштування inputPassClearing
    public boolean getInputCalcClearing() {
        return settings.getInputCalcClearing();
    }

    // Повертає пароль
    public String getPassword() {return settings.getPassword(); }

    // Повертає значення налаштування digitalOwner
    public boolean getDigitalOwner() { return settings.getDigitalOwner(); }

    // Повертає значення стану фільтру порядку сортування
    public boolean getFiltersSortMode() { return settings.getFiltersSortMode(); }

    // Повертає значення стану фільтру критерію сортування
    public int getFiltersSortParam() { return settings.getFiltersSortParam(); }

    // Переключення опції activityProtection
    public void editActivityProtection() {
        settings.resetActivityProtection();
        saveSettings(settings);
    }

    // Переключення опції inputCalcClearing
    public void editInputCalcClearing() {
        settings.resetInputCalcClearing();
        saveSettings(settings);
    }

    // Переключення опції digitalOwner
    public void editDigitalOwner() {
        settings.resetDigitalOwner();
        saveSettings(settings);
    }

    // Встановити налаштування за замовченням
    public void setDefaultSettings() {
        Settings newSettings = new Settings(settings.getPassword(), settings.getFiltersSortMode(), settings.getFiltersSortParam());
        settings = newSettings;
        saveSettings(settings);
    }

    /*
     *  Валідація пароля (перевірка на заборонені символи)
     *
     *  поверне     true - якщо все гаразд
     *             false - якщо знайшлись недопустимі символи
     */
    public boolean passwordValidation(String newPassword) {
        // Паттерн для дозволених символів (цифри, '.', '-', '+')
        String pattern = "^[0-9.\\-+]+$";

        return newPassword.matches(pattern);
    }

    // Зміна пароля
    public void editPassword(String newPassword) {
        settings.setPassword(newPassword);
        saveSettings(settings);
    }

    // Переключення опції filtersSortMode
    public void editFiltersSortMode(boolean filtersSortMode) {
        settings.setFiltersSortMode(filtersSortMode);
        saveSettings(settings);
    }

    // Переключення опції filtersSortParam
    public void editFiltersSortParam(int filtersSortParam) {
        settings.setFiltersSortParam(filtersSortParam);
        saveSettings(settings);
    }

    public int getFontSizeMain() { return settings.getFontSizeMain(); }
    public int getFontSizeInput() {return settings.getFontSizeInput(); }
    public int getFontSizeButtons() { return settings.getFontSizeButtons(); }
    public int getFontSizeLargeButtons() { return settings.getFontSizeLargeButtons(); }
    public int getFontSizeFieldCaptions() { return settings.getFontSizeFieldCaptions(); }
    public int getFontSizeOther() { return settings.getFontSizeOther(); }
    public int getFontSizeRssMain() { return settings.getFontSizeRssMain(); }
    public int getFontSizeRssSecondary() { return settings.getFontSizeRssSecondary(); }

    public boolean setFontSizeMain(int fontSizeMain) {
        if (validateEditFontSize(fontSizeMain)) {
            settings.setFontSizeMain(fontSizeMain);
            return true;
        }
        return false;
    }

    public boolean setFontSizeInput(int fontSizeInput) {
        if (validateEditFontSize(fontSizeInput)) {
            settings.setFontSizeInput(fontSizeInput);
            return true;
        }
        return false;
    }

    public boolean setFontSizeButtons(int fontSizeButtons) {
        if (validateEditFontSize(fontSizeButtons)) {
            settings.setFontSizeButtons(fontSizeButtons);
            return true;
        }
        return false;
    }

    public boolean setFontSizeLargeButtons(int fontSizeLargeButtons) {
        if (validateEditFontSize(fontSizeLargeButtons)) {
            settings.setFontSizeLargeButtons(fontSizeLargeButtons);
            return true;
        }
        return false;
    }

    public boolean setFontSizeFieldCaptions(int fontSizeFieldCaptions) {
        if (validateEditFontSize(fontSizeFieldCaptions)) {
            settings.setFontSizeFieldCaptions(fontSizeFieldCaptions);
            return true;
        }
        return false;
    }

    public boolean setFontSizeOther(int fontSizeOther) {
        if (validateEditFontSize(fontSizeOther)) {
            settings.setFontSizeOther(fontSizeOther);
            return true;
        }
        return false;
    }

    public boolean setFontSizeRssMain(int fontSizeRssMain) {
        if (validateEditFontSize(fontSizeRssMain)) {
            settings.setFontSizeRssMain(fontSizeRssMain);
            return true;
        }
        return false;
    }

    public boolean setFontSizeRssSecondary(int fontSizeRssSecondary) {
        if (validateEditFontSize(fontSizeRssSecondary)) {
            settings.setFontSizeRssSecondary(fontSizeRssSecondary);
            return true;
        }
        return false;
    }

    private boolean validateEditFontSize(int fontSize) {
        if (fontSize >= Settings.MIN_FONT_SIZE && fontSize <= Settings.MAX_FONT_SIZE) {
            return true;
        }
        return false;
    }
}
