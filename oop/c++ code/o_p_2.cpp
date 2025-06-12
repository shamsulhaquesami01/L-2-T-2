#include <iostream>
 using namespace std;
class Rectangle
{
    int *width, *height;

public:
    Rectangle(int, int);
    ~Rectangle();
    int area() { return (*width * *height); }
};
Rectangle::Rectangle(int a, int b)
{
    width = new int;
    height = new int;
    *width = a;
    *height = b;
}
Rectangle::~Rectangle()
{
    delete width;
    delete height;
}
int main()
{
    Rectangle recta(3, 4), rectb(5, 6);
    recta = rectb;
    cout << "recta area: " << recta.area() << endl;
    cout << "rectb area: " << rectb.area() << endl;
    return 0;
}