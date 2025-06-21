#include <iostream>
#include <vector>
#include<stack>
using namespace std;


int main(){
   stack<int> a;
   int n =10;
   int c=0;
   a.push(n); c++;
   while(!a.empty()){
    int x = a.top();
    a.pop();
    if(x>2) {
        a.push(x-1); a.push(x-2);
        c+=2;
    }
   }
   cout<<c<<endl;
}