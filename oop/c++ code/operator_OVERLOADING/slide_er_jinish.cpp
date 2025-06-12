#include <iostream>
using namespace std;
class Coord
{
    int x, y;

public:
    Coord(int x = 0, int y = 0) : x(x), y(y) {}
    void show() const { cout << "(" << x << ", " << y << ")" << endl; }
    friend Coord operator+(const Coord &c1, const Coord &c2);
    friend Coord operator+(const Coord &c, int n);
    friend Coord operator+(int n, const Coord &c);
    friend Coord operator++(Coord &c);
    friend Coord operator++(Coord &c, int);
};
Coord operator+(const Coord &c1, const Coord &c2)
{
    return Coord(c1.x + c2.x, c1.y + c2.y);
}
Coord operator+(const Coord &c, int n)
{
    return Coord(c.x + n, c.y + n);
}
Coord operator+(int n, const Coord &c)
{
    return Coord(n + c.x, n + c.y);
}
Coord operator++(Coord &c)
{
    ++c.x;
    ++c.y;
    return c;
}
Coord operator++(Coord &c, int)
{
    ++c.x;
    ++c.y;
    return c;
}
int main()
{
    Coord c1(1, 2), c2(3, 4);
    Coord c3 = c1 + c2;
    c3.show();
    Coord c4 = c1 + 5;
    c4.show();
    Coord c5 = 5 + c1;
    c5.show();
    ++c1;
    c1.show();
    Coord x = c1++;
    c1.show();
    x.show();
    return 0;
}