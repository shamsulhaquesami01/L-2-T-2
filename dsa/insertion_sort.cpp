#include<iostream>

using namespace std;

int main(){
    int a[] ={4,24,1,4,6,5,23,56,56,5,2,2,2,2,4,9,0,-90};
    int n=sizeof(a)/a[0];
    for(int i=1; i<n; i++){
        int key = a[i];
        int j=i-1;
        while(j>=0 && a[j]>key){
            a[j+1]=a[j];
            j=j-1;
        }
        a[j+1]=key;
    }
    for(int i=0; i<n; i++) cout<<a[i]<<" ";
}