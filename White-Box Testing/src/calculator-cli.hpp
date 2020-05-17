#ifndef CALCULATOR_CLI_HPP
#define CALCULATOR_CLI_HPP

#include <iostream>
#include <stdexcept>
#include <fstream>
#include <vector>
#include <ctype.h>
#include <sstream>

using namespace std;

class Calculator
{
public:
    Calculator(){};
    ~Calculator(){};

    string calculate(string input);
};

#endif //CALCULATOR_CLI_HPP
