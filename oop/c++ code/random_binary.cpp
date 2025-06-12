#include <iostream>
#include <fstream>
using namespace std;
void showState(ios::iostate state)
{
    if (state & ios::goodbit)
        cout << "goodbit is set" << endl;
    if (state & ios::eofbit)
        cout << "eofbit is set" << endl;
    if (state & ios::failbit)
        cout << "failbit is set" << endl;
    if (state & ios::badbit)
        cout << "badbit is set" << endl;
}
int main()
{
    char *str = new char[80];
    ifstream in;
    ofstream out;
    try
    {
        in.open("test.cpp");
        if (!in)
            throw "Cannot open input file";
        out.open("test2.cpp");
        if (!out)
            throw "Cannot open output file";
    }
    catch (const char *message)
    {
        cout << message << endl;
        return 1;
    }
    cout << "getPointer: " << in.tellg() << " putPointer: " << out.tellp() << endl;
    in.read(str, 80);
    out.write(str, 80);
    cout << "getPointer: " << in.tellg() << " putPointer: " << out.tellp() << endl;
    in.seekg(0, ios::end);
    out.seekp(0, ios::end);
    cout << "getPointer: " << in.tellg() << " putPointer: " << out.tellp() << endl;
    in.read(str, 80);
    cout << in.gcount() << " characters read" << endl;
    out.write(str, 80);
    cout << "getPointer: " << in.tellg() << " putPointer: " << out.tellp() << endl;
    ios::iostate state = in.rdstate();
    showState(state);
    in.clear();
    state = in.rdstate();
    showState(state);
    return 0;
}