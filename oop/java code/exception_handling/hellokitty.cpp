#include <iostream>
using namespace std;
void Xtest()
{
    try
    {
        throw "String";
    }
    catch (const char *s)
    {
        cout << "Caught a string: " << s << endl;
       throw; // throw s; -> ok & the same
    }
}
int main()
{
    try
    {
        Xtest();
    }
    catch (const char *s)
    {
        cout << "Caught a string in main: " << s << endl;
        throw;
    }
    catch(const char* t){
        cout << "Caught a string in main2: " << t << endl;
    }
    return 0;
}