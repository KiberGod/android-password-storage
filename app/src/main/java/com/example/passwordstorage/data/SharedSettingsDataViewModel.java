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
}
