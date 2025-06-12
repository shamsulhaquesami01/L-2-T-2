
#include <iostream>
using namespace std;

class myclass
{
    int x;

public:
    myclass() :x (0) {}
    ~myclass()
    {
        cout << "Destructing\n";
    }
    void setx(int n) { x = n; }
    int getx() { return x; }
};

void f (myclass& ob){
    ob.setx(50);
}

int main() {
    myclass *obj = new myclass();
    cout << obj->getx() << endl; 
    f(*obj);
    cout << obj->getx() << endl; 
    return 0;
    }

