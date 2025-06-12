#include<iostream>
#include<exception>
using namespace std;
int divide(){
   int d =0;
   if(d==0){
    throw runtime_error("chudi");
   }
   int a = 10/d;
   return a;
}
int main(){
    try{
      cout<<divide()<<endl;
    }
    catch(exception& e){
        cout<<e.what()<<endl;
    }
      return 0;
}