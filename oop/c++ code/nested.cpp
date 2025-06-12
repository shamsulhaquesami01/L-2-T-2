#include <iostream>
#include <string>
using namespace std;
class Outer {
int a;
public:
Outer(int x) { a = x; }
void show() { cout << "Outer: " << a << endl; }
void showInner(int x) {
Inner inner(x);
inner.show();
}
class Inner {
int b;
public:
Inner(int y) { b = y; }
void show() { cout << "Inner: " << b << endl; }
void showOuter(Outer &ob) { ob.show(); }
};
class Inner2 {
    public:
    void show(string str) {
    cout << str << endl;
    }
    };
    Inner2 inner2;
    };
    int main() {
        Outer outer(10);
        outer.show();
        outer.showInner(20);
        Outer::Inner inner(30);
        inner.show();
        inner.showOuter(outer);
        outer.inner2.show("Hello from Inner2!");
        return 0;
        }