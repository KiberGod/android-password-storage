package com.kibergod.passwordstorage.data;

import static com.kibergod.passwordstorage.NativeController.getCalculator;

import androidx.lifecycle.ViewModel;

import com.kibergod.passwordstorage.NativeController;
import com.kibergod.passwordstorage.model.Calculator;

public class SharedCalculatorDataViewModel extends ViewModel {

    private Calculator calculator;

    // Ініціалізація даних калькулятора
    public void setCalculator() { calculator = getCalculator(); }

    public String getNumber1() { return calculator.getNumber1(); }
    public String getNumber2() { return calculator.getNumber2(); }
    public Character getOperation() { return calculator.getOperation(); }

    public void saveCalculator(String number1, String number2, Character operation) {
        Calculator newCalculator = new Calculator(number1, number2, operation);
        calculator = newCalculator;
        NativeController.saveCalculator(calculator);
    }
}
