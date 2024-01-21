package com.kibergod.passwordstorage;

import static com.kibergod.passwordstorage.NativeController.initSecurityCore;

import java.util.Stack;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.kibergod.passwordstorage.data.SharedDigitalOwnerViewModel;
import com.kibergod.passwordstorage.data.SharedSettingsDataViewModel;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;
import com.kibergod.passwordstorage.ui.utils.ViewUtils;


public class MainActivity extends AppCompatActivity {
    private SharedSettingsDataViewModel sharedSettingsDataViewModel;
    private SharedDigitalOwnerViewModel sharedDigitalOwnerViewModel;

    private final char PLUS = '+';
    private final char MINUS = '-';
    private final char MULTIPLY = '×';
    private final char DIVIDE = '÷';
    private final char POINT = '.';
    private final char LEFT_PARENTHESIS = '(';
    private final char RIGHT_PARENTHESIS = ')';
    private StringBuilder expression;

    private final int MAX_PASSWORD_LEN = 13;
    private String password = "";

    private boolean needResizeTextFont = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Блокування переключення на темну тему
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Підключення основного С++ ядра
        initSecurityCore(this);

        sharedSettingsDataViewModel = new ViewModelProvider(this).get(SharedSettingsDataViewModel.class);
        sharedDigitalOwnerViewModel = new ViewModelProvider(this).get(SharedDigitalOwnerViewModel.class);
        sharedSettingsDataViewModel.setSettings();
        sharedDigitalOwnerViewModel.setDigitalOwner();

        expression = new StringBuilder();
        defineNeedResizeTextFont();
        setOnClickToCalcButton();
        setOnClickToClearButton();
        setOnClickToEraseButton();
        setOnClickToLeftParenthesisButton();
        setOnClickToRightParenthesisButton();
        setOnClickToPointButton();
        setOnClickToDivisionButton();
        setOnClickToMultiplicationButton();
        setOnClickToDifferenceButton();
        setOnClickToAmountButton();

        checkSetOldData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedSettingsDataViewModel.setSettings();
    }

    // Адаптація висоти поля виводу результату під маленькі екрани
    private void defineNeedResizeTextFont() {
        ConstraintLayout constraintLayout = findViewById(R.id.mainContainer);
        LinearLayout linearLayout = findViewById(R.id.resultLinear);
        LinearLayout linearLayout2 = findViewById(R.id.expressionLinear);
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        ViewTreeObserver.OnPreDrawListener preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (linearLayout.getHeight() + linearLayout2.getHeight() + tableLayout.getHeight() > constraintLayout.getHeight()-20) {
                    needResizeTextFont = true;

                    TextView resultPlace = findViewById(R.id.resultPlace);
                    resultPlace.setTextSize(TypedValue.COMPLEX_UNIT_PX, spToPx(40));
                    linearLayout.setPadding(linearLayout.getPaddingLeft(), linearLayout.getPaddingTop(), linearLayout.getPaddingRight(), 0);
                }
                constraintLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        };
        constraintLayout.getViewTreeObserver().addOnPreDrawListener(preDrawListener);
    }

    // Перевірка на необхідність встановити дані, введені у попередній сессії
    private void checkSetOldData() {
        if (!sharedSettingsDataViewModel.getInputCalcClearing()) {
            expression.setLength(0);
            expression.append(sharedSettingsDataViewModel.getCalcExpression());
            updateView();
        }
    }

    //Функція виконує накопичення послідовності символів, що формують собою пароль, порівнює його з ключем
    //для пропуску у сховище паролів, та автоматично знищує послідовність, якщо та занадто довга.
    private void checkPassword(Character symbol) {
        password += symbol;
        if (password.length() == MAX_PASSWORD_LEN) {
            password = "";
        } else if (sharedDigitalOwnerViewModel.secureEntry(password, sharedSettingsDataViewModel.getPassword(), sharedSettingsDataViewModel.getDigitalOwner())) {
            Intent homePage = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(homePage);
        }
    }

    // Автозміна розміру тексту
    private void adjustTextSize() {
        TextView resultPlace = findViewById(R.id.resultPlace);
        HorizontalScrollView resultScroll = findViewById(R.id.resultScroll);

        int scrollViewWidth = resultScroll.getWidth()-120;
        int textViewWidth = resultPlace.getWidth();

        if (scrollViewWidth > 0 && textViewWidth > 0) {
            float currentTextSize = resultPlace.getTextSize();
            float newTextSize = currentTextSize;

            while (textViewWidth > scrollViewWidth && newTextSize > spToPx(34)) {
                newTextSize--;

                resultPlace.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
                resultPlace.measure(0, 0);
                resultPlace.layout(0, 0, resultPlace.getMeasuredWidth(), resultPlace.getMeasuredHeight());

                textViewWidth = resultPlace.getWidth();
            }

            while (textViewWidth < scrollViewWidth && newTextSize < spToPx(80)) {
                newTextSize++;

                resultPlace.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
                resultPlace.measure(0, 0);
                resultPlace.layout(0, 0, resultPlace.getMeasuredWidth(), resultPlace.getMeasuredHeight());

                textViewWidth = resultPlace.getWidth();
            }
        }
    }

    private float spToPx(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    // Натиснення =
    private void setOnClickToCalcButton() {
        ViewUtils.setOnClickToView(getWindow().getDecorView().getRootView(), R.id.buttonResult, () -> {
            String result = calculate();
            TextView resultTextView = findViewById(R.id.resultPlace);
            TextView expressionTextView = findViewById(R.id.expressionPlace);
            if (result.equals("")) {
                if (expression.length() != 0) {
                    resultTextView.setText("Помилка");
                } else  {
                    resultTextView.setText("0");
                }
            } else {
                expressionTextView.setText(addSpaces(new StringBuilder(result)));
                expression.delete(0, expression.length());
                expression.append(result.replace(",", "."));
            }
        });
    }

    // Натиснення кнопки повного стертя виразу
    private void setOnClickToClearButton() {
        ViewUtils.setOnClickToView(getWindow().getDecorView().getRootView(), R.id.buttonClear, () -> {
            password = "";
            expression.setLength(0);
            updateView();
            HorizontalScrollView horizontalScrollView = findViewById(R.id.expressionScroll);
            horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            HorizontalScrollView horizontalScrollView2 = findViewById(R.id.resultScroll);
            horizontalScrollView2.fullScroll(View.FOCUS_RIGHT);
        });
    }

    // Натиснення кнопки стертя останнього символу
    private void setOnClickToEraseButton() {
        ViewUtils.setOnClickToView(getWindow().getDecorView().getRootView(), R.id.buttonErase, () -> {
            if (expression.length() != 0) {
                expression.setLength(expression.length()-1);
                updateView();
            }
        });
    }

    // Натиснення (
    private void setOnClickToLeftParenthesisButton() {
        ViewUtils.setOnClickToView(getWindow().getDecorView().getRootView(), R.id.buttonLeftParenthesis, () -> {
            if (expression.length() != 0) {
                char lastChar = expression.charAt(expression.length()-1);
                if (Character.isDigit(lastChar) || lastChar == RIGHT_PARENTHESIS) {
                    expression.append(MULTIPLY);
                } else if (lastChar == POINT) {
                    expression.setLength(expression.length()-1);
                    expression.append(MULTIPLY);
                }
            }
            addSymbol(LEFT_PARENTHESIS);
            checkPassword(LEFT_PARENTHESIS);
        });
    }

    // Натиснення )
    private void setOnClickToRightParenthesisButton() {
        ViewUtils.setOnClickToView(getWindow().getDecorView().getRootView(), R.id.buttonRightParenthesis, () -> {
            if (expression.length() != 0) {
                char lastChar = expression.charAt(expression.length()-1);
                if (lastChar == PLUS || lastChar == MINUS || lastChar == DIVIDE || lastChar == MULTIPLY || lastChar == POINT) {
                    expression.setLength(expression.length()-1);
                }
                addSymbol(RIGHT_PARENTHESIS);
            }
            checkPassword(RIGHT_PARENTHESIS);
        });
    }

    // Натиснення дробного знаку
    private void setOnClickToPointButton() {
        ViewUtils.setOnClickToView(getWindow().getDecorView().getRootView(), R.id.buttonPoint, () -> {
            if (expression.length() == 0) {
                expression.append('0');
                addSymbol(POINT);
            } else if (Character.isDigit(expression.charAt(expression.length()-1))) {
                boolean pointExists = false;
                for (int i = expression.length()-1; i > -1; i--) {
                    if (Character.isDigit(expression.charAt(i))) {
                        continue;
                    }

                    if (expression.charAt(i) == POINT) {
                        pointExists = true;
                    }
                    break;
                }
                if (!pointExists) {
                    addSymbol(POINT);
                }
            }
            checkPassword(POINT);
        });
    }

    // Натиснення -
    private void setOnClickToDifferenceButton() {
        ViewUtils.setOnClickToView(getWindow().getDecorView().getRootView(), R.id.buttonDifference, () -> {
            if (expression.length() != 0) {
                char lastChar = expression.charAt(expression.length()-1);
                if (lastChar == PLUS || lastChar == MINUS || lastChar == POINT) {
                    expression.setLength(expression.length()-1);
                } else if (lastChar == DIVIDE || lastChar == MULTIPLY) {
                    expression.append(LEFT_PARENTHESIS);
                }
            }
            addSymbol(MINUS);
            checkPassword(MINUS);
        });
    }

    // Натиснення ÷
    private void setOnClickToDivisionButton() {
        ViewUtils.setOnClickToView(getWindow().getDecorView().getRootView(), R.id.buttonDivision, () -> {
            replaceOperator(DIVIDE);
            updateView();
        });
    }

    // Натиснення ×
    private void setOnClickToMultiplicationButton() {
        ViewUtils.setOnClickToView(getWindow().getDecorView().getRootView(), R.id.buttonMultiplication, () -> {
            replaceOperator(MULTIPLY);
            updateView();
        });
    }

    // Натиснення +
    private void setOnClickToAmountButton() {
        ViewUtils.setOnClickToView(getWindow().getDecorView().getRootView(), R.id.buttonAmount, () -> {
            replaceOperator(PLUS);
            updateView();
            checkPassword(PLUS);
        });
    }

    // Стандартна автозаміна одного оператора на інший
    private void replaceOperator(Character operator) {
        if (expression.length() != 0) {
            char lastChar = expression.charAt(expression.length()-1);
            if (lastChar == PLUS || lastChar == MINUS || lastChar == DIVIDE || lastChar == MULTIPLY || lastChar == POINT) {
                expression.setLength(expression.length()-1);
            }

            if (lastChar != LEFT_PARENTHESIS && expression.charAt(expression.length()-1) != LEFT_PARENTHESIS) {
                expression.append(operator);
            }
        }
    }

    // Натиснення на цифрову кнопку
    public void setDigit(View view) {
        if (expression.length() != 0 && expression.charAt(expression.length()-1) == RIGHT_PARENTHESIS) {
            expression.append(MULTIPLY);
        }
        addSymbol(((TextView) view).getText().charAt(0));
        checkPassword(((TextView) view).getText().charAt(0));
    }

    // Додання символу до виразу
    private void addSymbol(Character symbol) {
        expression.append(symbol);
        updateView();
    }

    // Оновлення UI
    private void updateView() {
        sharedSettingsDataViewModel.setCalcExpression(expression.toString());

        TextView expressionTextView = findViewById(R.id.expressionPlace);
        expressionTextView.setText(addSpaces(expression).toString().replace(".", ","));

        TextView resultTextView = findViewById(R.id.resultPlace);
        resultTextView.setText(addSpaces(new StringBuilder(calculate())));

        scrollAnimation(R.id.expressionScroll, expressionTextView);
        scrollAnimation(R.id.resultScroll, resultTextView);
    }

    // Скролл довгих полів з невеликою анімацією
    private void scrollAnimation(int idScroll, TextView textView) {
        HorizontalScrollView horizontalScrollView = findViewById(idScroll);
        ViewTreeObserver vto = textView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        int scrollRange = horizontalScrollView.getChildAt(0).getMeasuredWidth() - horizontalScrollView.getMeasuredWidth();
                        horizontalScrollView.smoothScrollTo(scrollRange, 0);

                        if (idScroll == R.id.resultScroll && !needResizeTextFont) {
                            adjustTextSize();
                        }
                    }
                });
            }
        });
    }

    // Додання пробілів біля опреатоірв та у числах
    private StringBuilder addSpaces(StringBuilder input) {
        StringBuilder result = new StringBuilder(input);

        for (int i = 0; i < result.length(); i++) {
            char currentChar = result.charAt(i);
            if (currentChar == PLUS || currentChar == MINUS || currentChar == MULTIPLY || currentChar == DIVIDE) {
                if (i > 0 && !Character.isWhitespace(result.charAt(i - 1))) {
                    result.insert(i, ' ');
                    i++;
                }

                if (i < result.length() - 1 && !Character.isWhitespace(result.charAt(i + 1))) {
                    result.insert(i + 1, ' ');
                    i++;
                }
            }
        }

        for (int i = result.length() - 1; i >= 0; i--) {
            char currentChar = result.charAt(i);
            if (Character.isDigit(currentChar)) {
                int count = 0;
                i = getStartIntegerValuePosition(result, i);
                while (i >= 0 && (Character.isDigit(result.charAt(i)) || result.charAt(i) == '.')) {
                    if (count > 0 && count % 2 == 0 && i > 0 && Character.isDigit(result.charAt(i - 1))) {
                        result.insert(i, ' ');
                        i++;
                    }
                    count++;
                    i--;
                }
                i++;
            }
        }
        return result;
    }

    // повертає координату початку цілої частини дробного числа
    private int getStartIntegerValuePosition(StringBuilder result, int iter) {
        for (int i = iter; i >= 0; i--) {
            if (!Character.isDigit(result.charAt(i)) && result.charAt(i) != '.') {
                return iter;
            } else if (result.charAt(i) == '.') {
                return i-1;
            }
        }
        return iter;
    }

    // Загальна функція коррекції виразу перед розрахунком
    private String correctExpression(StringBuilder expression) {
        StringBuilder copiedExpression = new StringBuilder(expression);
        return correctParentheses(
                correctDiffOperator(
                        trimLastOperators(
                                copiedExpression
                        )
                ).toString()
        );
    }

    // Підрізання зайвих операторів у кінці виразу
    private StringBuilder trimLastOperators(StringBuilder expression) {
        for (int i = expression.length() - 1; i >= 0; i--) {
            if (Character.isDigit(expression.charAt(i))) {
                break;
            }
            expression.deleteCharAt(i);
        }
        return expression;
    }

    // Атодобудова нулів перед кожним оператором мінуса, що не має лівого числового сусіда (для подальшого корректного розрахунку)
    private StringBuilder correctDiffOperator(StringBuilder expression) {
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == MINUS) {
                if (i == 0 || !Character.isDigit(expression.charAt(i - 1))) {
                    expression.insert(i, '0');
                }

            }
        }
        return expression;
    }

    // Автодобудова відсутніх дужок у виразі
    private String correctParentheses(String expression) {
        char[] tokens = expression.toCharArray();

        int leftParenthesesCount = 0;
        int rightParenthesesCount = 0;

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == LEFT_PARENTHESIS) {
                leftParenthesesCount++;
            } else if (tokens[i] == RIGHT_PARENTHESIS) {
                rightParenthesesCount++;
            }
        }

        int diffParentheses = leftParenthesesCount - rightParenthesesCount;

        if (diffParentheses == 0) {
            return expression;
        } else if (diffParentheses > 0) {
            return expression + getMissingParentheses(leftParenthesesCount - rightParenthesesCount, RIGHT_PARENTHESIS);
        } else {
            return getMissingParentheses(rightParenthesesCount - leftParenthesesCount, LEFT_PARENTHESIS) + expression;
        }
    }

    // Повертає рядок з дужок, яких бракує у виразі
    private String getMissingParentheses(int count, char symbol) {
        StringBuilder missingParentheses = new StringBuilder();
        for (int i=0; i<count; i++) {
            missingParentheses.append(symbol);
        }
        return missingParentheses.toString();
    }

    // Розрахунок виразу
    public String calculate() {
        try {
            char[] tokens = correctExpression(expression).toCharArray();

            Stack<Double> values = new Stack<>();
            Stack<Character> operators = new Stack<>();

            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i] == ' ') {
                    continue;
                }

                if (Character.isDigit(tokens[i]) || tokens[i] == POINT) {
                    StringBuilder sbuf = new StringBuilder();
                    while (i < tokens.length && (Character.isDigit(tokens[i]) || tokens[i] == POINT)) {
                        sbuf.append(tokens[i++]);
                    }
                    i--;

                    values.push(Double.parseDouble(sbuf.toString()));
                } else if (tokens[i] == LEFT_PARENTHESIS) {
                    operators.push(tokens[i]);
                } else if (tokens[i] == RIGHT_PARENTHESIS) {
                    while (operators.peek() != LEFT_PARENTHESIS) {
                        values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                    }
                    operators.pop();
                } else if (isOperator(tokens[i])) {
                    while (!operators.empty() && hasPrecedence(tokens[i], operators.peek())) {
                        values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                    }
                    operators.push(tokens[i]);
                }
            }

            while (!operators.empty()) {
                values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
            }

            return correctResult(values.pop());
        } catch (Exception e) {
            return "";
        }
    }

    // Корекція результату перед виведенням
    private String correctResult(double result) {
        String resultString = String.format("%.4f", result);
        resultString = resultString.replace(",", ".");
        resultString = resultString.contains(".") ? resultString.replaceAll("0*$", "").replaceAll("\\.$", "") : resultString;
        return resultString.replace(".", ",");
    }

    // Якщо токен є оператором
    private boolean isOperator(char c) {
        return c == PLUS || c == MINUS || c == MULTIPLY || c == DIVIDE;
    }

    // Якщо другий оператор пріорітетніше
    private boolean hasPrecedence(char op1, char op2) {
        return (op2 != LEFT_PARENTHESIS && op2 != RIGHT_PARENTHESIS && getPrecedence(op1) <= getPrecedence(op2));
    }

    // Отримання пріорітету виконання операцій
    private int getPrecedence(char op) {
        switch (op) {
            case PLUS:
            case MINUS:
                return 1;
            case MULTIPLY:
            case DIVIDE:
                return 2;
            default:
                return -1;
        }
    }

    // Мат. ядро
    private double applyOperator(char operator, double b, double a) {
        switch (operator) {
            case PLUS:
                return a + b;
            case MINUS:
                return a - b;
            case MULTIPLY:
                return a * b;
            case DIVIDE:
                if (b == 0) {
                    throw new ArithmeticException("Cannot divide by zero");
                }
                return a / b;
            default:
                return 0;
        }
    }
}