#include<iostream>
using namespace std;
class Obj{
   public:
   int x;
   Obj(){x=10;};
   Obj(int x ){this->x = x;};
   Obj(int z, int y){x = z+y;};
   ~Obj(){};
};
int main(){
   int arr1[] = {1,2,3} ;// size 3 and initialized 
   int arr2[4] = {1,2}; // rest are 0
   int arr3[5] ={}; // all zero
   int* arr4 = new int[5]; // un initialzed 5 size
   int* arr5 = new int[5](); // all zero
   // ##error##.       int* arr6 = new int[5]{1,2,3}; 

   Obj arr7[3]; // all initialized by default cons
   Obj arr8[3] = {Obj(1),Obj(2),Obj(4+5)};
   Obj special[3] = {1,2,34};
   // if only one argument in cons, can be passed as just vales

   Obj* arr9 = new Obj[3]; // all default cons
   // ##error##.         Obj* arr10 = new Obj[3]{new A(10),new A(10),new A(10)};
   // ##error##.         Obj* arr11 = new Obj[3]{A(1),A(2),A(3)};

   //not possible to initialize a newd array in c++
  // thats why to do new must have default cons

   arr9[0] =  Obj(1);
   arr9[1] =  Obj(45);
   arr9[2] = Obj(89);

   cout<<arr8[1].x<<endl;
}