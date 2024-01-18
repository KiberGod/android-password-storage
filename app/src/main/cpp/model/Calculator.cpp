//
// Created by kiber_god on 11.09.2023.
//
#include <cstring>
#include "Calculator.h"

Calculator::Calculator() {
    expression[0] = '\0';
}

Calculator::Calculator(const char* expression) {
    strncpy(this->expression, expression, MAX_NUMBER_LENGTH - 1);
    this->expression[MAX_NUMBER_LENGTH - 1] = '\0';
}

const char* Calculator::getExpression() const {
    return expression;
}
