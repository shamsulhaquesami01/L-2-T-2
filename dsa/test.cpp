
#include<bits/stdc++.h>
using namespace std;

int main(){
    int t; cin>>t;
    while(t--){
        long long int n,k;
        cin>>n>>k;
        long long int carry;
        long long int count =0;
        while(n!=0){
            if(n%2==0){
              if(k%2==0) carry =k;
              else carry = k-1;
              if(carry > n) {
                count++;
                break;
              }
             n=n-carry;
            }
            else{
                if(k%2==0) carry =k-1;
                else carry = k; 
                if(carry > n) {
                    count++;
                    break;
                  }
                n= n-carry;
            }
            count++;
        }
        cout<<count<<endl;
    }
}