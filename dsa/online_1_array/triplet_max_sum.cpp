#include<bits/stdc++.h>

using namespace std;

void inversion(int arr[],int n){
    
    int max =INT_MIN;
    int a,b,c;
      for(int i=0; i<n-2; i++){
         if(arr[i]+arr[i+1]+arr[i+2]>max) {
            max= arr[i]+arr[i+1]+arr[i+2];
            a=arr[i];
            b=arr[i+1];
            c=arr[i+2];
      }
    }
    cout<<a<<" "<<b<<" "<<c<<endl;

}

int main(){
    int arr[]={-1,-2,-3,-4,-5};
    inversion(arr,5);
}