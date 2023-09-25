package com.example.passwordstorage.data;

import static com.example.passwordstorage.NativeController.getDigitalOwner;
import static com.example.passwordstorage.NativeController.saveDigitalOwner;
import static com.example.passwordstorage.model.DigitalOwner.PASSIVE_MODE;

import androidx.lifecycle.ViewModel;

import com.example.passwordstorage.model.DigitalOwner;

import java.util.Calendar;

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
}
