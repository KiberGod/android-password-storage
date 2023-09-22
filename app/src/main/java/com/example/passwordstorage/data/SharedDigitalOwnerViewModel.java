package com.example.passwordstorage.data;

import static com.example.passwordstorage.NativeController.getDigitalOwner;
import static com.example.passwordstorage.NativeController.saveDigitalOwner;
import static com.example.passwordstorage.model.DigitalOwner.PASSIVE_MODE;

import androidx.lifecycle.ViewModel;

import com.example.passwordstorage.model.DigitalOwner;

public class SharedDigitalOwnerViewModel extends ViewModel {

    private DigitalOwner digitalOwner;

    // Ініціалізація "Цифрового власника"
    public void setDigitalOwner() { digitalOwner = getDigitalOwner(); }

    public void setMode(int mode ) {
        digitalOwner.setMode(mode);
        saveDigitalOwner(digitalOwner);
    }

    public void setPassiveMode() {
        digitalOwner.setMode(PASSIVE_MODE);
        saveDigitalOwner(digitalOwner);
    }

    public void setDaysNumber(int days) {
        digitalOwner.setNumberDaysBeforeTriggering(days);
        saveDigitalOwner(digitalOwner);
    }

    public int getDaysNumber() {
        return digitalOwner.getNumberDaysBeforeTriggering();
    }

    // Повертає true по співпадінню заданого режиму з встановленним
    public boolean getModeFlag(int mode) {
        if (digitalOwner.getMode() == mode) {
            return  true;
        } else {
            return false;
        }
    }

}
