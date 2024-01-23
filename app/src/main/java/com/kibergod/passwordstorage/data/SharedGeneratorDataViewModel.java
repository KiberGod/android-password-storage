package com.kibergod.passwordstorage.data;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.kibergod.passwordstorage.model.generator.PasswordGenerator;

public class SharedGeneratorDataViewModel extends ViewModel {

    private PasswordGenerator passwordGenerator;

    // тимчасова тестова ініціалізація
    public void setPasswordGenerator(Context context) {
        passwordGenerator = PasswordGenerator.initSettings(context);
    }

    public int getMinPassLength() { return passwordGenerator.getMinLength(); }
    public int getMaxPassLength() { return passwordGenerator.getMaxLength(); }
    public int getPassLength() { return passwordGenerator.getLength(); }
    public String getPassword(Context context) {
        passwordGenerator.reduceSymbolsSetLength();
        PasswordGenerator.saveSettings(passwordGenerator, context);
        return passwordGenerator.generatePassword();
    }

    public void editPassLength(int newLength, Context context) {
        int oldLength = passwordGenerator.getLength();
        passwordGenerator.setLength(newLength);
        if (oldLength > newLength) {
            passwordGenerator.reduceSymbolsSetLength();
        } else if (oldLength < newLength) {
            passwordGenerator.increaseSymbolsSetLength();
        }
        PasswordGenerator.saveSettings(passwordGenerator, context);
    }

    public void editNotUseSymbols(String newNotUseSymbols, Context context) {
        passwordGenerator.setNotUseSymbols(newNotUseSymbols);
        PasswordGenerator.saveSettings(passwordGenerator, context);
    }

    public int getMaxNotUseSymbols() { return passwordGenerator.getMaxNotUseSymbols(); }
    public String getNotUseSymbols() { return passwordGenerator.getNotUseSymbols(); }
    public int getNumberTypes() { return passwordGenerator.getNumberTypes(); }

    public boolean getSymbolsSetUsageByIndex(int index) {
        return passwordGenerator.getSymbolSets()[index].isUsage();
    }

    public boolean getSymbolsSetRandomLenByIndex(int index) {
        return passwordGenerator.getSymbolSets()[index].isRandomLength();
    }

    public int getSymbolsSetLengthByIndex(int index) {
        return passwordGenerator.getSymbolSets()[index].getLength();
    }

    public void editSymbolSetUsageByIndex(int index, boolean usage, Context context) {
        passwordGenerator.getSymbolSets()[index].setUsage(usage);
        PasswordGenerator.saveSettings(passwordGenerator, context);
    }

    public void editSymbolSetRandomLengthByIndex(int index, boolean randomLength, Context context) {
        passwordGenerator.getSymbolSets()[index].setRandomLength(randomLength);
        PasswordGenerator.saveSettings(passwordGenerator, context);
    }

    public void editSymbolSetLengthByIndex(int index, int newLength, Context context) {
        passwordGenerator.getSymbolSets()[index].setLength(newLength);
        PasswordGenerator.saveSettings(passwordGenerator, context);
    }
}