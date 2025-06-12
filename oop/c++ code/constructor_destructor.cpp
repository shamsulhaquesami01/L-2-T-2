#include<iostream>
using namespace std;
class sami {
    public:
    int x,y;
    sami(int a=10,int b=20); //deafult value dile ekhane dite hobe, declaration er ekhane
};

sami::sami(int a, int b){ //definition class er baire korle dafult value deya jabe na ekhane
    x=a;
    y=b;
   }

int main(){
    sami s(100,200);
     cout<<s.x<<" "<<s.y<<endl;

     sami s2;
     cout<<s2.x<<" "<<s2.y<<endl;
}