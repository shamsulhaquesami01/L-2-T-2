#include<iostream>
#include<vector>
using namespace std;

void merge(int arr[], int l, int mid, int r, int ans[]){
    for(int i=l; i<=mid; i++){
         int large =-1;
         for(int j=mid+1; j<=r; j++){
              if(arr[j]>arr[i]) {
              large =j;
              break;
              }
         }
          
            ans[i] = large;
        
    }

}

void nextGreater(int arr[], int ans[], int l , int r){
     if(r<=l) {
        ans[l]=-1;
        return;
     }
     int mid = (r+l)/2;

     nextGreater(arr,ans, l, mid);
     nextGreater(arr, ans, mid+1,r);


     merge(arr, l, mid, r, ans);
 


}

int main(){
    int arr[] = { 2, 1, 5, 3, 4};
    int ans[5];
    nextGreater(arr,ans,0,4);
   for(int i=0; i<5; i++) cout<<ans[i]<<" ";
}