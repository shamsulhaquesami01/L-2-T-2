#include<iostream>
using namespace std;
class hello {
    int *ptr;
    public:
    void setvalue(int a){
        ptr = new int;
        *ptr = a;
    }
    int area(){
        return *ptr * *ptr;
    }
    ~hello(){
        delete ptr;
        cout<<"deleted"<<endl;
    }

};

int main(){
    hello sami;
    sami.setvalue(100);
    cout<<sami.area()<<endl;
}