package com.example.passwordstorage.data;

import static com.example.passwordstorage.NativeController.getSettings;
import static com.example.passwordstorage.NativeController.saveSettings;

import androidx.lifecycle.ViewModel;

import com.example.passwordstorage.model.Settings;

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

    // Встановити налаштування за замовченням
    public void setDefaultSettings() {
        Settings newSettings = new Settings(settings.getPassword());
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
}