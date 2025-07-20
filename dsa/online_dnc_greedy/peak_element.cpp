#include<iostream>
#include<vector>
using namespace std;

//o(logn)

int peak(int arr[], int l, int r, int n){
    if(r<=l) return arr[l];
    int mid = l+(r-l)/2;
    if((mid==0 || arr[mid] >= arr[mid-1]) &&
    (mid==(n-1)) || arr[ mid] >= arr[mid +1]){
        return mid;
    }
    if(mid>0 && arr[mid-1] > arr[mid]) return peak(arr,l,mid-1,n);
    return peak(arr,mid+1, r,n);
}

int main(){
    int arr[] = {2,3,1,10,5,8,9};
    cout<<peak(arr,0,6, 7);
}