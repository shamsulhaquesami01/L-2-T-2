#include <iostream>
using namespace std;

class MyClass {
private:
    int x;

public:
    MyClass() {
        x = 0;
        cout << "Constructor" << endl;
    }

    ~MyClass() {
        cout << "Destructor" << endl;
    }

    void setX(int val) {
        x = val;
    }

    int& getX() {
        return x;
    }
};

inline void modifyValue(MyClass &obj) {
    obj.getX() = 100;
}

int main() {
    MyClass obj1;
    obj1.setX(50);
    int& ref = obj1.getX();
    ref = 75;
    modifyValue(obj1);
    cout << obj1.getX() << endl;
    return 0;
}