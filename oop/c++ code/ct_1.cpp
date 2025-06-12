#include <iostream>
using namespace std;
class MyClass
{
private:
    int x;
    int y=100;

public:
    MyClass()
    {
        x = 0;
        cout << " Constructor " << endl;
    }
    ~myClass() { cout << " Destructor " << endl; }
    void setX(int val) { x = val; }
    int &getX() { return x; }
};
class A{
    int a;
    public:
    A(int x){}
    A(){}
    int get(){return a;};
};

inline void modifyValue(MyClass obj)
{
    obj.getX() = 100;
}
void f(int a=0, int b=0){
cout<<a<<" "<<b<<endl;
}
int main()
{
    A *p;
    p= new A[10];
    f();
    f(1);
    f(2,4);
    //for(int i=0; i<10; i++) cout<<p[i].get()<<endl;
    MyClass obj1;
    obj1.setX(50);
    int &ref = obj1.getX();
    ref = 75;
    modifyValue(obj1);
    //cout << obj1.getX() << endl;
    return 0;
}