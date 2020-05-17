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

class Variable
{
public:
    string name;
    double value;
    Variable(string n, double v) : name(n), value(v) {}
};

class Token
{
public:
    char kind;
    double value;
    string name;
    Token(char ch) : kind(ch), value(0) {}
    Token(char ch, double val) : kind(ch), value(val) {}
    Token(char ch, string n) : kind(ch), name(n) {}
};

class Token_stream
{
public:
    Token_stream();
    Token get();
    void putback(Token t);
    void ignore(char c);

private:
    bool full{false};
    Token buffer;
};

#endif //CALCULATOR_CLI_HPP
