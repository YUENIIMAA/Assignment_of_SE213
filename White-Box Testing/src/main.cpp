#include <boost/program_options.hpp>
namespace po = boost::program_options;

#include "calculator-cli.hpp"

using namespace std;

int main(int argc, char *argv[])
{
    Calculator calculator;
    string input;
    cin >> input;
    string answer = calculator.calculate(input);
    cout << "calculate() returns <" << answer << ">" << endl;
    return 0;
}
