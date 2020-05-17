#include "calculator-cli.cpp"
#include <gtest/gtest.h>

TEST(CalculatorTest, Add)
{
    ASSERT_EQ("0", calculate("0+0;"));
}

int main(int argc, char **argv)
{
    testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}
