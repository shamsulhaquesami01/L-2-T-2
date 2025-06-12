#include <iostream>
using namespace std;

int y;
class X {
public:
 int count;
}c;
namespace demo{
    int a;
}
namespace demoo
{
    int a;
    
} // namespace demo

int main () {
    X a[10];
   static int r;
    cout<<r<<endl;
using  namespace demo;
using namespace demoo;
demo::a=100;
cout<<demoo::a<<endl<<endl;
cout<<y<<endl;
}