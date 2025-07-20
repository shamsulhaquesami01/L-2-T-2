#include<iostream>
#include<vector>
using namespace std;

int odd(int arr[], int l , int r){
    if(r<=l) return arr[l];
    int mid = l+(r-l)/2;
    if(arr[mid] == arr [mid^1]){
        return odd(arr, mid+1, r);

    }
    return odd(arr,l,mid);
}

int main(){
    int arr[] = { 2,2,1,1,3,4,4,5,5};
    cout<<odd(arr,0,8);
}