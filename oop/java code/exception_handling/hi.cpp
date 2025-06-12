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
Coord operator++(Coord &c){
    c.x++;
    c.y++;
    return c;
}

int main(){
    Coord xz(10,20);
    xz.show();
    ++(++(++xz));
    xz.show();
}
