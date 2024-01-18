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
    static const int MAX_NUMBER_LENGTH = 200;

    char expression[MAX_NUMBER_LENGTH];

public:
    Calculator();
    Calculator(const char* expression);
    const char* getExpression() const;
};


#endif //PASSWORD_STORAGE_CALCULATOR_H
