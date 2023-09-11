//
// Created by kiber_god on 11.09.2023.
//
#include <cstring>
#include "Calculator.h"

Calculator::Calculator() {
    strcpy(number1, "0");
    number2[0] = '\0';
    operation = 'n';
}

Calculator::Calculator(const char* number1, const char* number2, const char operation)
    : operation(operation) {
    strncpy(this->number1, number1, MAX_NUMBER_LENGTH - 1);
    this->number1[MAX_NUMBER_LENGTH - 1] = '\0';

    strncpy(this->number2, number2, MAX_NUMBER_LENGTH - 1);
    this->number2[MAX_NUMBER_LENGTH - 1] = '\0';
}

const char* Calculator::getNumber1() const {
    return number1;
}

const char* Calculator::getNumber2() const {
    return number2;
}

const char Calculator::getOperation() const {
    return operation;
}