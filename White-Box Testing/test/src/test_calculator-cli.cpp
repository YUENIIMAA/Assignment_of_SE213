#include "gmock/gmock.h"
#include "calculator-cli.hpp"

TEST(Number, readSingleNumber)
{
    Calculator calculator;

    EXPECT_EQ("0", calculator.calculate("0;"));
    EXPECT_EQ("1", calculator.calculate("1;"));
    EXPECT_EQ("2", calculator.calculate("2;"));
    EXPECT_EQ("3", calculator.calculate("3;"));
    EXPECT_EQ("4", calculator.calculate("4;"));
    EXPECT_EQ("5", calculator.calculate("5;"));
    EXPECT_EQ("6", calculator.calculate("6;"));
    EXPECT_EQ("7", calculator.calculate("7;"));
    EXPECT_EQ("8", calculator.calculate("8;"));
    EXPECT_EQ("9", calculator.calculate("9;"));
}

TEST(Number, readMultipleNumber)
{
    Calculator calculator;

    EXPECT_EQ("10", calculator.calculate("10;"));
    EXPECT_EQ("210", calculator.calculate("210;"));
    EXPECT_EQ("3210", calculator.calculate("3210;"));
    EXPECT_EQ("43210", calculator.calculate("43210;"));
}

TEST(Number, readNegativeNumber)
{
    Calculator calculator;

    EXPECT_EQ("-1", calculator.calculate("-1;"));
    EXPECT_EQ("-2", calculator.calculate("-2;"));
    EXPECT_EQ("-3", calculator.calculate("-3;"));
    EXPECT_EQ("-4", calculator.calculate("-4;"));
    EXPECT_EQ("-5", calculator.calculate("-5;"));
    EXPECT_EQ("-6", calculator.calculate("-6;"));
    EXPECT_EQ("-7", calculator.calculate("-7;"));
    EXPECT_EQ("-8", calculator.calculate("-8;"));
    EXPECT_EQ("-9", calculator.calculate("-9;"));
    EXPECT_EQ("-10", calculator.calculate("-10;"));
    EXPECT_EQ("-210", calculator.calculate("-210;"));
    EXPECT_EQ("-3210", calculator.calculate("-3210;"));
    EXPECT_EQ("-43210", calculator.calculate("-43210;"));
}

TEST(Number, readDecimalNumber)
{
    Calculator calculator;

    EXPECT_EQ("0.1", calculator.calculate("0.1;"));
    EXPECT_EQ("1", calculator.calculate("1.0;"));
    EXPECT_EQ("-0.1", calculator.calculate("-0.1;"));
    EXPECT_EQ("-1", calculator.calculate("-1.0;"));
}

TEST(Parentheses, parenthesesWithNumber)
{
    Calculator calculator;

    EXPECT_EQ("0", calculator.calculate("(0);"));
    EXPECT_EQ("1", calculator.calculate("(1);"));
    EXPECT_EQ("-1", calculator.calculate("(-1);"));
    EXPECT_EQ("43210", calculator.calculate("(43210);"));
    EXPECT_EQ("-43210", calculator.calculate("(-43210);"));
}

TEST(Operator, singleOperator)
{
    Calculator calculator;

    EXPECT_EQ("63", calculator.calculate("42+21;"));
    EXPECT_EQ("21", calculator.calculate("42-21;"));
    EXPECT_EQ("882", calculator.calculate("42*21;"));
    EXPECT_EQ("2", calculator.calculate("42/21;"));
    EXPECT_EQ("20.5", calculator.calculate("41/2;"));
    EXPECT_EQ("1", calculator.calculate("41%2;"));
    EXPECT_EQ("120", calculator.calculate("5!;"));
    EXPECT_EQ("1", calculator.calculate("0!;"));
    EXPECT_EQ("1", calculator.calculate("(1-1)!;"));
}

TEST(Operator, multipleOperator)
{
    Calculator calculator;

    EXPECT_EQ("36", calculator.calculate("11+12+13;"));
    EXPECT_EQ("11", calculator.calculate("36-12-13;"));
    EXPECT_EQ("24", calculator.calculate("2*3*4;"));
    EXPECT_EQ("2", calculator.calculate("24/4/3;"));
}

TEST(Operator, multipleOperatorMixed)
{
    Calculator calculator;

    EXPECT_EQ("7", calculator.calculate("1+2*3;"));
    EXPECT_EQ("-5", calculator.calculate("1-2*3;"));
    EXPECT_EQ("2.5", calculator.calculate("1+3/2;"));
    EXPECT_EQ("-0.5", calculator.calculate("1-3/2;"));
}

TEST(Operator, multipleOperatorMixedWithParentheses)
{
    Calculator calculator;

    EXPECT_EQ("9", calculator.calculate("(1+2)*3;"));
    EXPECT_EQ("-3", calculator.calculate("(1-2)*3;"));
    EXPECT_EQ("2", calculator.calculate("(1+3)/2;"));
    EXPECT_EQ("-1", calculator.calculate("(1-3)/2;"));
}

TEST(Variable, savingAnswer)
{
    Calculator calculator;

    EXPECT_EQ("1", calculator.calculate("1+1;ANS*3;ANS-5;"));
    EXPECT_EQ("1", calculator.calculate("0;ANS!;"));
}

TEST(Overall, generalTest)
{
    Calculator calculator;

    EXPECT_EQ("120", calculator.calculate("(1+2)*(3+4)/(6-5)/(23%3);(ANS-5.5)!;"));
}

TEST(Overall, errorTest)
{
    Calculator calculator;

    EXPECT_EQ("number should be int", calculator.calculate("5.1!;"));
    EXPECT_EQ("number should be int", calculator.calculate("(1-0.5)!;"));
    EXPECT_EQ("number should be int", calculator.calculate("0.5;ANS!;"));
    EXPECT_EQ("number should be above zero", calculator.calculate("(-1)!;"));
    EXPECT_EQ("number should be above zero", calculator.calculate("-1;ANS!;"));
    EXPECT_EQ("number should be above zero", calculator.calculate("(0-1)!;"));
    EXPECT_EQ("primary expected", calculator.calculate("();"));
    EXPECT_EQ("left-hand operand of & not int", calculator.calculate("5.1%2;"));
    EXPECT_EQ("right-hand operand of & not int", calculator.calculate("5%(2.1);"));
    EXPECT_EQ("divided by zero", calculator.calculate("5%0;"));
    EXPECT_EQ("divide by zero", calculator.calculate("1/0;"));
    EXPECT_EQ("bad token", calculator.calculate("x+1;"));
    EXPECT_EQ("'(' expected", calculator.calculate("(1));"));
    EXPECT_EQ("')' expected", calculator.calculate("((1);"));
}
