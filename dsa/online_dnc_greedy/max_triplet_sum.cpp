#include<numeric>
#include<iostream>
#include<vector>
#include<algorithm>
using namespace std;

int maxTripletSum(int A[], int l, int r) {
    if (r - l + 1 < 3)
        return INT_MIN;  

    if (r - l + 1 == 3)
        return A[l] + A[l+1] + A[l+2];  

    int mid = (l + r) / 2;

    int leftMax = maxTripletSum(A, l, mid);
    int rightMax = maxTripletSum(A, mid+1, r);

    int crossMax = INT_MIN;
     int crossMax1 = INT_MIN;
      int crossMax2 = INT_MIN;
    if (mid-1 >= l && mid+1 <= r)
        crossMax = A[mid-1] + A[mid] + A[mid+1];
    if(mid-2 >= l ) crossMax1 = A[mid-2] + A[mid] + A[mid-1];
    if(mid+2 <=r ) crossMax2 = A[mid] + A[mid+1] + A[mid+2];
    int low = max(max(crossMax1,crossMax),crossMax2);
    if(leftMax> rightMax && leftMax && low) return leftMax;
    else if (rightMax>low) return rightMax;
    else return low;
}

int main(){
int arr[] ={ 3,-1,2,10,500,-14,-4,6,8};
cout<<maxTripletSum(arr,0,8);
}