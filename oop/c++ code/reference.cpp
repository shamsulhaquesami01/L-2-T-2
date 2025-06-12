#include <iostream>
#include <cstdlib>
using namespace std;
class Array
{
    int size;
    char *p;

public:
    Array(int num);
    ~Array() { delete[] p; }
    char &put(int i);
    char get(int i);
};
Array::Array(int num)
{
    p = new char[num];
    if (!p)
    {
        cout << "Allocation error\n";
        exit(1);
    }
    size = num;
}
// Put something into the Array.
char &Array::put(int i)
{
    if (i < 0 || i >= size)
    {
        cout << "Bounds error!!!\n";
        exit(1);
    }
    return p[i]; 
}
    char Array ::get(int i)
    {
        if (i < 0 || i >= size)
        {
            cout << "Bounds error!!!\n";
            exit(1);
        }
        return p[i];
    }
    int main()
    {
        int x;
        int &ref=x; // create an independent reference
        x = 10; // these two statements
        ref = 10; // are functionally equivalent
        ref = 100;
        // this prints the number 100 twice cout << x << ’ ’ << ref << "\n";
        return 0;
    }