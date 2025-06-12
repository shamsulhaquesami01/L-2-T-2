#include<iostream>
using namespace std;
class rectangle{
    int h,w;
    public:
    void set_value(int a,int b);
    int area(){
        return h*w;
    }
    rectangle(int a){
        h=a;
    }
};
void rectangle::set_value(int a,int b){
    h=a;
    w=b;
}
class myclass{
    int a;
    public:
    void sami(int f){
        cout<<f<<endl;
    }
};
int main(){
    myclass a;
    a.sami(45);
    rectangle r1;
    r1.set_value(3,4);
    cout<<r1.area()<<endl;
}