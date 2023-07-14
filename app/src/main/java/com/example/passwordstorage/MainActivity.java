package com.example.passwordstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String number1 = "0";
    private String number2 = "";
    private Character operation = Character.MIN_VALUE;

    private final int MAX_NUMBER_LEN = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Функція встановлює математичну операцію
    public void setOperation(View view) {
        if (number1 != "0") {
            operation = ((Button) view).getText().charAt(0);
            printResult();
        }
    }

    // Функція додає цифри до чисел
    public void setDigit(View view) {
        String digit = String.valueOf(((Button) view).getText());
        if (operation == Character.MIN_VALUE && number1.length() < MAX_NUMBER_LEN) {
            number1 = checkFirstZero(number1, digit);
        } else if (number2.length() < MAX_NUMBER_LEN){
            number2 = checkFirstZero(number2, digit);
        }
        printResult();
    }

    // Друк введених чисел та операції
    private void printResult() {
        TextView resultPlace = (TextView)findViewById(R.id.resultPlace);
        if (operation == Character.MIN_VALUE) {
            resultPlace.setText(number1);
        } else {
            resultPlace.setText(number1 + operation + number2);
        }
    }

    // Видалення введених чисел та операції
    public void clearNumbers(View view) {
        operation = Character.MIN_VALUE;
        number1 = "0";
        number2 = "";
        printResult();
    }

    // Коректне видалення нуля на початку числа
    private String checkFirstZero(String number, String digit) {
        if (number == null || number.isEmpty()) {
            return digit;
        }

        if (number.charAt(0) == '0') {
            if (digit.charAt(0) == '0') {
                return number;
            } else {
                return digit;
            }
        }
        return number + digit;
    }
}