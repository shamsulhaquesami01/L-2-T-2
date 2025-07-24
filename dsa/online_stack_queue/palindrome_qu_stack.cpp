#include<iostream>
#include<queue>
#include<stack>
using namespace std;
int main(){
    int t; cin>>t; 
    while(t--)
    {
    bool pal =true;
    int n; cin>>n;
    char ch;
   queue<char> a;
   for(int i=0; i<n; i++){
     cin>>ch;
     a.push(ch);
   }
   stack<char> b;
   for(int i=0; i<n/2 ; i++) {
    b.push(a.front());
    a.pop();
   }
   if(n%2) a.pop();
   while(!a.empty()){
    int x = a.front();
    int y = b.top();
    a.pop(); b.pop();
    if(x != y ) {
        cout<<"False";
        pal = false;
    }
   }
   if(pal) {
    cout<<"True";
   }
}
}

// 10 20 30 40 50|
//50 40  30 20 10|
