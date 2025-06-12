
#include<bits/stdc++.h>

using namespace std;

void inversion(int a[],int n){
    int sum =0;
  for(int i=0; i<n; i++){
    for(int j=i+1; j<n; j++){
        if(a[i]>a[j]) sum++;
    }
  }
  cout<<sum<<endl;
}

int main(){
    int arr[]={-1,-2,-3,-4,-5};
    inversion(arr,5);
}