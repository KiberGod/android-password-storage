package com.kibergod.passwordstorage.data;

import static com.kibergod.passwordstorage.NativeController.getCalculator;

import androidx.lifecycle.ViewModel;

import com.kibergod.passwordstorage.NativeController;
import com.kibergod.passwordstorage.model.Calculator;

public class SharedCalculatorDataViewModel extends ViewModel {

    private Calculator calculator;

    // Ініціалізація даних калькулятора
    public void setCalculator() { calculator = getCalculator(); }

    public String getExpression() { return calculator.getExpression(); }

    public void saveCalculator(String expression) {
        Calculator newCalculator = new Calculator(expression);
        calculator = newCalculator;
        NativeController.saveCalculator(calculator);
    }
}
