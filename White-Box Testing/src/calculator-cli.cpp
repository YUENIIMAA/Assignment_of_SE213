#include "calculator-cli.hpp"

stringstream ipt;
const char print = ';';
const char number = '8';
const char name = 'a';

class Variable
{
public:
    string name;
    double value;
    Variable(string n, double v) : name(n), value(v) {}
};

vector<Variable> var_table;

void set_value(string s, double d)
{
    for (unsigned int i = 0; i < var_table.size(); ++i)
    {
        if (var_table[i].name == s)
        {
            var_table[i].value = d;
            return;
        }
    }
    throw runtime_error("variable undefined");
}

double get_value(string s)
{
    for (unsigned int i = 0; i < var_table.size(); ++i)
    {
        if (var_table[i].name == s)
        {
            return var_table[i].value;
        }
    }
    return 0;
}

double define_name(string var, double val)
{
    var_table.push_back(Variable(var, val));
    return val;
}

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

void Token_stream::ignore(char c)
{
    if (full && c == buffer.kind)
    {
        full = false;
        return;
    }
    full = false;
    char ch = 0;
    while (ipt >> ch)
    {
        if (ch == c)
        {
            return;
        }
    }
}

Token_stream::Token_stream() : full(false), buffer(0) {}

void Token_stream::putback(Token t)
{
    if (full)
        throw runtime_error("putback() into a full buffer");
    buffer = t;
    full = true;
}

Token Token_stream::get()
{
    if (full)
    {
        full = false;
        return buffer;
    }

    char ch;
    ipt >> ch;

    switch (ch)
    {
    case ';':
    case '(':
    case ')':
    case '+':
    case '-':
    case '*':
    case '/':
    case '%':
    case '!':
        return Token(ch);
    case '.':
    case '0':
    case '1':
    case '2':
    case '3':
    case '4':
    case '5':
    case '6':
    case '7':
    case '8':
    case '9':
    {
        ipt.putback(ch);
        double val;
        ipt >> val;
        return Token(number, val);
    }
    default:
    {
        if (isalpha(ch))
        {
            string s;
            s += ch;
            while (ipt.get(ch) && isalpha(ch))
            {
                s += ch;
            }
            ipt.putback(ch);
            if (s == "ANS")
            {
                return Token(name, s);
            }
        }
        throw runtime_error("bad token");
    }
    }
}

Token_stream ts;

double expression();

double primary()
{
    Token t = ts.get();
    switch (t.kind)
    {
    case '(':
    {
        double d = expression();
        t = ts.get();
        if (t.kind != ')')
        {
            ts.putback(t);
            throw runtime_error("')' expected");
        }
        Token n = ts.get();
        while (n.kind == '!')
        {
            if (int(d) != d)
            {
                throw runtime_error("number should be int");
            }
            if (d < 0)
            {
                throw runtime_error("number should be above zero");
            }
            if (d == 0)
            {
                d = 1;
            }
            for (int i = int(d) - 1; i > 0; --i)
            {
                d = d * i;
            }
            n = ts.get();
        }
        ts.putback(n);
        return d;
    }
    case number:
    {
        Token n = ts.get();
        while (n.kind == '!')
        {
            if (int(t.value) != t.value)
            {
                throw runtime_error("number should be int");
            }
            if (t.value < 0)
            {
                throw runtime_error("number should be above zero");
            }
            if (t.value == 0)
            {
                t.value = 1;
            }
            for (int i = int(t.value) - 1; i > 0; --i)
            {
                t.value = t.value * i;
            }
            n = ts.get();
        }
        ts.putback(n);
        return t.value;
    }
    case '-':
        return -primary();
    case '+':
        return primary();
    case name:
    {
        double tmp = get_value("ANS");
        Token n = ts.get();
        while (n.kind == '!')
        {
            if (int(tmp) != tmp)
            {
                throw runtime_error("number should be int");
            }
            if (tmp < 0)
            {
                throw runtime_error("number should be above zero");
            }
            if (tmp == 0)
            {
                tmp = 1;
            }
            for (int i = int(tmp) - 1; i > 0; --i)
            {
                tmp = tmp * i;
            }
            n = ts.get();
        }
        ts.putback(n);
        return tmp;
    }
    default:
    {
        ts.putback(t);
        throw runtime_error("primary expected");
    }
    }
}

double term()
{
    double left = primary();
    Token t = ts.get();
    while (true)
    {
        switch (t.kind)
        {
        case '*':
            left *= primary();
            t = ts.get();
            break;
        case '/':
        {
            double d = primary();
            if (d == 0)
                throw runtime_error("divide by zero");
            left /= d;
            t = ts.get();
            break;
        }
        case '%':
        {
            double d = primary();
            int i1 = int(left);
            if (i1 != left)
            {
                throw runtime_error("left-hand operand of & not int");
            }
            int i2 = int(d);
            if (i2 != d)
            {
                throw runtime_error("right-hand operand of & not int");
            }
            if (i2 == 0)
            {
                throw runtime_error("divided by zero");
            }
            left = i1 % i2;
            t = ts.get();
            break;
        }
        default:
        {
            ts.putback(t);
            return left;
        }
        }
    }
}

double expression()
{
    double left = term();
    Token t = ts.get();

    while (true)
    {
        switch (t.kind)
        {
        case '+':
            left += term();
            t = ts.get();
            break;
        case '-':
            left -= term();
            t = ts.get();
            break;
        default:
            ts.putback(t);
            return left;
        }
    }
}

void clean_up_mess()
{
    ts.ignore(print);
}

string Calculator::calculate(string input)
{
    var_table.clear();
    ipt.clear();

    define_name("ANS", 0);
    ipt << input;

    double tmp;
    while (!ipt.eof())
    {
        try
        {
            Token t = ts.get();
            while (t.kind == print)
            {
                t = ts.get();
            }
            ts.putback(t);
            tmp = expression();
            t = ts.get();
            if (t.kind == ')')
            {
                ts.putback(t);
                throw runtime_error("'(' expected");
            }
            else
            {
                ts.putback(t);
            }
            set_value("ANS", tmp);
        }
        catch (exception &e)
        {
            clean_up_mess();
            if (!ipt.eof())
            {
                return e.what();
            }
        }
    }
    string retval;
    stringstream ss;
    ss << get_value("ANS");
    ss >> retval;
    return retval;
}
