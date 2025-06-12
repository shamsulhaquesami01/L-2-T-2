#include <iostream>
using namespace std;
class StaticDemo {
static int a;
//static int b = 5;
 int b;
int n = 4, m = 5;
public:
static void increment() {a++; //b++;
    }
//static void increment() {a++;}
// static int getProduct(){ return n * m;}
int getProduct() { return n * m;}
static void display() {
cout << "A: " << a << " B: "  << endl;
}
};
int StaticDemo::a = 0;

int main() {
    const int a =100;
    a=40;
    StaticDemo sd;
    sd.display();
    sd.increment();
    StaticDemo::display();
    cout << sd.getProduct() << endl;
    return 0;
    }