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
    public boolean getInputPassClearing() {
        return settings.getInputPassClearing();
    }

    // Переключення опції activityProtection
    public void editActivityProtection() {
        settings.resetActivityProtection();
        saveSettings(settings);
    }

    // Переключення опції inputPassClearing
    public void editInputPassClearing() {
        settings.resetInputPassClearing();
        saveSettings(settings);
    }
}
