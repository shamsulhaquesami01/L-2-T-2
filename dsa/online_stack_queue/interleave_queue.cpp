#include<iostream>
#include<queue>
using namespace std;
int main(){
    int t; cin>>t; while(t--){
    int n; cin>>n;
   int arr[n];
   for(int i=0; i<n; i++) cin>>arr[i];
   queue<int> a;
   for(int i=0; i<n; i++) a.push(arr[i]);
   queue<int>b;
   for(int i=0; i<n/2; i++){
    int x = a.front();
    a.pop();
    b.push(x);
   }
   int c=b.size();
   while( c--){
     a.push(b.front());
     b.pop();
     a.push(a.front());
     a.pop();
    }
    for(int i=0; i<n; i++){
      cout<<a.front()<<" ";
      a.pop();
    }
}
}