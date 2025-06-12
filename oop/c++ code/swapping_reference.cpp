#include <iostream>
using namespace std;
void swapargs(int &x, int &y)
{
    int t;
    t = x;
    x = y;
    y = t;
}
int main()
{
    int i, j;
    i = 20;
    j = 40;
    cout <<"i ="<< i <<", "<< j << endl;
    swapargs(i, j);
    cout <<"i ="<< i <<", "<< j << endl;
    return 0;
}