#include<iostream>
using namespace std;



class Father
{
    char name[20];

public:
int a;
    Father(char *fname) { strcpy(name, fname);  a =100;}
   void show() 
    {
        cout << " Father : " << name << endl;
    }
};
class Son : public Father
{
    char name[20];

public:
int a;

    Son(char *sname, char *fname) : Father(fname) { strcpy(name, sname); a=10; }
    int show_a(){
        return a;
    }
    void show()
    {
        cout << "Son : " << name << endl;
    }
};
int main()
{
    Father father("Rashid");
    Son son("Robin", "karim");
    Father *fp = new Son("sami", "unknown");
    son.show();
    father.show();
    fp->show();
}