
#include<iostream> 
using namespace std;
#define set1(x,y) x=y
inline void set2(int x, int y)
{
x = y; 
}
int main(){
int a = 3, b = 5;
cout << a << " " << b << endl; 
set2(a,b);
cout << a << " " << b << endl;
set1(a,b);
cout << a << " " << b << endl;
return 0; }
