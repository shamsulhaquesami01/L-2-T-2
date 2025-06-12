#include <iostream>
#include <fstream>
using namespace std;
int main()
{
    try
    {
        ofstream out("Inventory", ios::app);
        if (!out)
            throw "Cannot open output file";
        out << "Radios " << 39 << endl;
        out << "Toasters " << 21 << endl;
        out << "Mixers " << 17 << endl;
        out.close();
    }
    catch (const char *message)
    {
        cout << message << endl;
        return 1;
    }
    try
    {
        ifstream in("Inventory");
        if (!in)
            throw "Cannot open input file";
        char item[20];
        int quantity;
        in >> item >> quantity;
        while (!in.eof())
        {
            cout << item << ' ' << quantity << endl;
            in >> item >> quantity;
        }
        in.close();
    }
    catch (const char *message)
    {
        cout << message << endl;
        return 1;
    }
    ifstream in("Inventory");
    ofstream out("Temp");
    char ch;
    in >> ch; // >> operator eats WS
    while (!in.eof())
    {
        out << ch;
        in >> ch;
    }
    in.close();
    out.close();
    return 0;
}