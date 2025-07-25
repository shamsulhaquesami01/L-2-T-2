#include<iostream>
#include<vector>
using namespace std;



int countUnique(vector<int>& arr, int l, int r) {
   if(r-l<=1) return 1;
   int mid = l +(r-l)/2;
  int left = countUnique(arr,l,mid);
   int right = countUnique(arr,mid,r);
if(arr[mid]==arr[mid+1]) return left+right-1;
else return left+right;

}
int countUniqueMain(vector<int>& arr) {
    return countUnique(arr, 0, arr.size());
}

int main(){
    int arr[] = {1, 2,2,2,2, 3, 4,4,4, 5};
 vector<int> a(arr, arr + sizeof(arr) / sizeof(arr[0]));
    cout<<countUniqueMain(a)<<endl;
}