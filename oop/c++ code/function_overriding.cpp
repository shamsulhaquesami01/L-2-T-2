#include <iostream>
using namespace std;

class Point
{
protected:
    double x;
    double y;

public:
    Point()
    {
        x = 0.0;
        y = 0.0;
    }
    Point(double x, double y);
    double area() { return 0; }
};
Point::Point(double x, double y)
{
    this->x = x;
    this->y = y;
}


class Circle : public Point
{
protected:
    double rad;

public:
    Circle() { rad = 0.0; }
    Circle(double x ,double y ,double r);
    double area();
};


Circle::Circle(double x, double y, double r) : Point(x, y)
{
    rad = r;
}
double Circle::area()
{
    return 3.14 * rad * rad;
}

class Cylinder : public Circle
{
    double height;

public:
    Cylinder() { height = 0.0; }
    Cylinder(double x, double y, double r, double h);
    double area();
};
Cylinder::Cylinder(double x, double y, double r, double h) : Circle(x, y, r)
{
    height = h;
}
double Cylinder::area()
{
    return 3.14 * rad * rad * height;
}

 int main()
{
    Point p(1.0, 1.0);
    Circle c(1.0, 1.0, 3.0);
    Cylinder cl(1.0, 1.0, 3.0, 2.0);
    cout << "The area of the point is: " << p.area() << endl;
    cout << "The area of the circle is: " << c.area() << endl;
    cout << "The area of the cylinder is: " << cl.area() << endl;
    return 0;
}