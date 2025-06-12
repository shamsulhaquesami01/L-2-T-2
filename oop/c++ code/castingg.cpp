

#include <iostream>
using namespace std;

#include <iostream>
using namespace std;
struct mystruct
{
    int a = 5;
    char c = 'a';
};
class A
{
public:
    void display()
    {
        cout << "Class A" << endl;
    }
};
class B
{
public:
    void display()
    {
        cout << "Class B" << endl;
    }
};

class Animal
{
public:
    virtual void print()
    {
        cout << "Animal" << endl;
    }
};
class Dog : public Animal
{
public:
    void print()
    {
        cout << "Dog" << endl;
    }
};
class Cat : public Animal
{
public:
    void print()
    {
        cout << "Cat" << endl;
    }
};
class myClass
{
};

int main()
{
    // Animal *animal = new Animal;
    // Dog *dog = new Dog;
    // Cat *cat = new Cat;
    // animal->print();
    // animal = static_cast<Animal *>(dog);
    // animal->print();
    // animal = static_cast<Animal *>(cat);
    // animal->print();
    // Dog *d = static_cast<Dog *>(animal);
    // if (d == nullptr)
    // {
    //     cout << "Conversion failed" << endl;
    // }
    // else
    //     d->print();
    // myClass *x = dynamic_cast<myClass *>(animal);
    // return 0;

    int *p = new int(65);
    char *ch = reinterpret_cast<char *>(p);
    cout << *p << endl;
    cout << *ch << endl;
    cout << p << endl
         << endl;
    mystruct s;
    int *iptr = reinterpret_cast<int *>(&s);
    cout << *iptr << endl;
    iptr++;
    cout << *iptr << endl
         << endl;
          iptr++;
    cout << *iptr << endl
         << endl;
    B *b = new B();
    b->display();
    A *a = reinterpret_cast<A *>(b);
    a->display();
    return 0;
}