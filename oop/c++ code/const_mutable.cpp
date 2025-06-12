#include <iostream>
#include <mutex>
using namespace std;
class Demo {
mutable int a;
int b;
public:
//Demo(){a = 0; b = 0;}
int getA() const {return a;}
int getB() const {return b;}
void setAB(int x, int y) const {
a = x;
// b = y;
}
};
int main() {
    const Demo d;
    d.setAB(10, 20);
    cout << "A: " << d.getA() << endl;
    cout << "B: " << d.getB() << endl;
    return 0;
    }