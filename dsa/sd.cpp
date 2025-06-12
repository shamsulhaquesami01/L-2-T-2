#include<iostream>
using namespace std;
class rectangle{
    int h,w;
    public:
    void set_value(int a,int b);
    int area(){
        return h*w;
    }
};
void rectangle::set_value(int a,int b){
    h=a;
    w=b;
}
int main(){
    rectangle r1;
    r1.set_value(3,4);
    cout<<r1.area()<<endl;
}