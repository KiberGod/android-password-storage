package com.example.passwordstorage.data;

import androidx.lifecycle.ViewModel;

import com.example.passwordstorage.model.generator.PasswordGenerator;

public class SharedGeneratorDataViewModel extends ViewModel {

    private PasswordGenerator passwordGenerator;

    // тимчасова тестова ініціалізація
    public void testInit() {
        passwordGenerator = new PasswordGenerator();
    }


    public int getMinPassLength() { return passwordGenerator.getMinLength(); }
    public int getMaxPassLength() { return passwordGenerator.getMaxLength(); }
    public int getPassLength() { return passwordGenerator.getLength(); }

    public void editPassLength(int newLength) {
        int oldLength = passwordGenerator.getLength();
        passwordGenerator.setLength(newLength);
        if (oldLength > newLength) {
            passwordGenerator.reduceSymbolsSetLength();
        } else if (oldLength < newLength) {
            passwordGenerator.increaseSymbolsSetLength();
        }
    }

    public int getMaxNotUseSymbols() { return passwordGenerator.getMaxNotUseSymbols(); }
    public String getNotUseSymbols() { return passwordGenerator.getNotUseSymbols(); }
    public void editNotUseSymbols(String newNotUseSymbols) { passwordGenerator.setNotUseSymbols(newNotUseSymbols); }
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

    public void editSymbolSetUsageByIndex(int index) {
        passwordGenerator.getSymbolSets()[index].inversionUsage();
    }

    public void editSymbolSetRandomLengthByIndex(int index) {
        passwordGenerator.getSymbolSets()[index].inversionRandomLength();
    }

    public void editSymbolSetLengthByIndex(int index, int newLength) {
        passwordGenerator.getSymbolSets()[index].setLength(newLength);
        passwordGenerator.reduceSymbolsSetLength();
    }
}
