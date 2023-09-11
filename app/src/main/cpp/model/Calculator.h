//
// Created by kiber_god on 11.09.2023.
//

#ifndef PASSWORD_STORAGE_CALCULATOR_H
#define PASSWORD_STORAGE_CALCULATOR_H

/*
 *   Даний клас містить певні дані, пов`язані з калькулятором та не повинен
 *   існувати більш ніж в одному екземплярі
 */
class Calculator {
private:
    static const int MAX_NUMBER_LENGTH = 15;

    char number1[MAX_NUMBER_LENGTH];
    char number2[MAX_NUMBER_LENGTH];
    char operation;

public:
    Calculator();
    Calculator(const char* number1, const char* number2, const char operation);
    const char* getNumber1() const;
    const char* getNumber2() const;
    const char getOperation() const;
};


#endif //PASSWORD_STORAGE_CALCULATOR_H
