#include <iostream>
using namespace std;
template <typename T>
class Gen
{
    T ob;

public:
    Gen(T o) { ob = o; }
    T getob() { return ob; }
    void showType();
};
template <class T>
void Gen<T>::showType()
{
    cout << "Type: " << typeid(ob).name() << endl;
}
int main(){
  
    Gen<int> x(90);
    Gen<string> y("hello world");
    x.showType();
    cout<<x.getob()<<endl;
 y.showType();
    cout<<y.getob()<<endl;

}