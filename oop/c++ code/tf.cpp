#include<iostream>
#include<string>
using namespace std;


class Animal {
    int age;
    string x ;
public:
    Animal() { cout << "Constructing\n"; age = 10; x="sami"; }
    ~Animal() { cout << "Destructing\n"; }
    int get_age(){return age;}
    string get(){return x;}
};

Animal process(Animal a) {
    cout << "In process\n";
    Animal b;
    b = a;
    return b;
}

int main() {
    cout << "First line\n";
    Animal a;
    cout<<a.get_age()<<endl;
    cout << "Second line\n";
    Animal b = a;
    cout<<b.get_age()<<endl<<b.get()<<endl;
    cout << "Third line\n";
    Animal c = process(b);
    cout<<c.get_age()<<endl<<c.get()<<endl;;
    cout << "Return line\n";
    return 0;
}
