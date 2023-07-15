package com.example.passwordstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String number1 = "0";
    private String number2 = "";
    private Character operation = Character.MIN_VALUE;

    private final int MAX_NUMBER_LEN = 15;

    private float preResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Функція встановлює математичну операцію
    public void setOperation(View view) {
        if (number1 != "0" && number1.length() != 0) {
            if (operation != Character.MIN_VALUE) {
                setResult(view);
            }
            operation = ((Button) view).getText().charAt(0);
            printResult();
        }
    }

    // Функція додає цифри до чисел
    public void setDigit(View view) {
        String digit = String.valueOf(((Button) view).getText());
        if (operation == Character.MIN_VALUE && number1.length() < MAX_NUMBER_LEN) {
            number1 = checkFirstZero(number1, digit);
        } else if (operation != Character.MIN_VALUE && number2.length() < MAX_NUMBER_LEN){
            number2 = checkFirstZero(number2, digit);
            calculation();
        }
        printResult();
    }

    // Друк введених чисел та операції
    private void printResult() {
        TextView resultPlace = (TextView)findViewById(R.id.resultPlace);
        if (operation == Character.MIN_VALUE) {
            resultPlace.setText(number1);
        } else if (number2.length() == 0) {
            resultPlace.setText(number1 + '\n' + operation);
        } else {
            resultPlace.setText(number1 + '\n' + operation + '\n' + number2);
        }
    }

    // Натиснення на кнопку видалення усіх введених даних
    public void onClickClearButton(View view) {
        dataReset();
        printResult();
    }

    // Видалення введених чисел та операції
    private void dataReset() {
        operation = Character.MIN_VALUE;
        number1 = "0";
        number2 = "";
    }

    // Коректне видалення нуля на початку числа
    private String checkFirstZero(String number, String digit) {
        if (number == null || number.isEmpty()) {
            return digit;
        } else if (number.length() == 1) {
            if (number.charAt(0) == '0') {
                if (digit.charAt(0) == '0') {
                    return "0";
                } else {
                    return digit;
                }
            }
        }

        return number + digit;
    }

    // Функція калькуляції
    private void calculation() {
        try {
            switch (operation) {
                case '+':
                    preResult = Float.parseFloat(number1) + Float.parseFloat(number2);
                    break;
                case '-':
                    preResult = Float.parseFloat(number1) - Float.parseFloat(number2);
                    break;
                case '×':
                    preResult = Float.parseFloat(number1) * Float.parseFloat(number2);
                    break;
                case '÷':
                    preResult = Float.parseFloat(number1) / Float.parseFloat(number2);
                    break;
            }
            printPreResult(String.valueOf(preResult));
        } catch (NumberFormatException e) {
            printPreResult("Syntax error");
        }
    }

    // Функція виводить передчасний результат калькуляції
    private void printPreResult(String result) {
        TextView preResultPlace = (TextView)findViewById(R.id.previewResultPlace);
        if (!Float.isInfinite(preResult)) {
            preResultPlace.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
            preResultPlace.setText(result);
        } else {
            preResultPlace.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
            preResultPlace.setText("На нуль ділити не можна!");
        }
    }

    // Функція встановлює кінцевий результат
    public void setResult(View view) {
        if (!Float.isInfinite(preResult) && number2 != null && !number2.isEmpty()) {
            dataReset();
            number1 = String.valueOf(preResult);
            printResult();
        }
    }

    // Натиснення на кнопку видалення однієї цифри або операції
    public void onClickEraseButton(View view) {
        if (number2.length() > 0) {
            number2 = number2.substring(0, number2.length() - 1);
            if (number2.length() == 1 && number2.charAt(0) == '-') {
                number2 = "";
            }
        } else if (operation != Character.MIN_VALUE) {
            operation = Character.MIN_VALUE;
        } else if (number1.length() > 0) {
            number1 = number1.substring(0, number1.length() - 1);
            if (number1.length() == 1 && number1.charAt(0) == '-') {
                number1 = "";
            }
        }

        if (number1.length() > 0 && number2.length() > 0) {
            calculation();
        }
        printResult();
    }

    // Розрахунок відсотка
    public void calcPercentage(View view) {
        if (number2.length() != 0) {
            number2 = String.valueOf(Float.parseFloat(number2) / 100f);
            calculation();
        } else if (number1.length() != 0) {
            number1 = String.valueOf(Float.parseFloat(number1) / 100f);
            printPreResult(number1);
        }
        printResult();
    }

    // Функція встановлення плаваючої коми
    public void setFloatingComma(View view) {
        if (number2.length() != 0 && !number2.contains(".") && number2.length() < MAX_NUMBER_LEN) {
            number2 = number2 + ".";
        } else if (operation == Character.MIN_VALUE) {
            if (number1.length() != 0 && !number1.contains(".") && number1.length() < MAX_NUMBER_LEN) {
                number1 = number1 + ".";
            } else if (number1.length() == 0) {
                number1 = "0.";
            }
        }
        printResult();
    }

    // Функція зміни знака числа на протилежний
    public void inversionSignNumber(View view) {
        if (number2.length() != 0) {
            number2 = String.valueOf(Float.parseFloat(number2) * (-1));
            calculation();
        } else if (operation == Character.MIN_VALUE) {
            number1 = String.valueOf(Float.parseFloat(number1) * (-1));
        }
        printResult();
    }
}