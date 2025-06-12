#include <iostream>
using namespace std;
class Chudirbhai{
    int a;
    int b;
    public :
    Chudirbhai(int s, int m){
        a=s;
        b=m;
    }
};
class Figure
{
protected:
    double dim1, dim2;

public:
    Figure(double d1, double d2)
    {
        dim1 = d1;
        dim2 = d2;
    }

    virtual double area() = 0; // pure virtual function
   virtual void show (){
        cout<<"msin"<<endl;
    }
};
class Rectangle : public Figure
{
public:
    Rectangle(double d1, double d2) : Figure(d1, d2) {}

    double area()
    {
        return dim1 * dim2;
    }
    void show (){
        cout<<"rect"<<endl;
    }
};
class Triangle : public Figure
{
public:
    Triangle(double d1, double d2) : Figure(d1, d2) {}

    double area()
    {
        return dim1 * dim2 / 2;
    }
    void show (){
        cout<<"trig"<<endl;
    }
};

int main()
{
    Figure *p;
    Rectangle r(10, 7);
    Triangle t(10, 5);
    Chudirbhai x(3,2);
    Chudirbhai y =3,2;
    p = &r;
    cout << "Rectangle Area: " << p->area() << endl;
    p->show();

    p = &t;
    cout << "Triangle Area: " << p->area() << endl;
    p->show();
}
    
