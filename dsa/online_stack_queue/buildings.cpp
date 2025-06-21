#include <iostream>
#include <vector>
#include<stack>
using namespace std;


int main(){
   stack<int> a;
   int n; cin>>n;
   int arr[n];
   for(int i=0; i<n; i++) cin>>arr[i];
    for(int i=0; i<n; i++) a.push(arr[i]);
    int x = a.top();
    a.pop();
    cout<<"visible buildings : "; 
    cout<<x<<" ";
    while(!a.empty()){
        int y = a.top();
        a.pop();
        if(y>x) {
            cout<<y<<" ";
            x=y;
        }
    }
}