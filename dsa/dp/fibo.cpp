#include<iostream>
#include<vector>
using namespace std;
const int n=100;
vector<long long int> arr (n,-1);
long long int fibo(int n){
   if(n==0) return 0;
   if(n==1) return 1;
   if(arr[n]==-1){
    arr[n]= fibo(n-1)+fibo(n-2);
   }
   return arr[n];
}
int main(){
    int x;
    cin>>x;
    cout<<fibo(x)<<endl;
}