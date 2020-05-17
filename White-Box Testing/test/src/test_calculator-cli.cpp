#include "gmock/gmock.h"
#include "calculator-cli.hpp"

TEST(BraveCoverage, TestCaseOne)
{
    Calculator calculator;

    EXPECT_EQ("0", calculator.calculate("0+0;"));
}
