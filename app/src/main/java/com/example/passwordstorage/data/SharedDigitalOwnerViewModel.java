package com.example.passwordstorage.data;

import static com.example.passwordstorage.NativeController.getDigitalOwner;

import androidx.lifecycle.ViewModel;

import com.example.passwordstorage.model.DigitalOwner;

public class SharedDigitalOwnerViewModel extends ViewModel {

    private DigitalOwner digitalOwner;

    // Ініціалізація "Цифрового власника"
    public void setDigitalOwner() { digitalOwner = getDigitalOwner(); }


}
