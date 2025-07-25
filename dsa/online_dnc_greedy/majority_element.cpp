#include<iostream>
#include<vector>
using namespace std;
int count(int arr[], int l ,int r, int elem){
    int c =0;
    for(int i=l; i<=r; i++){
        if(arr[i]==elem) c++;
    }
    return c;
}


int findmajority(int arr[], int l,int r){

    if(r<=l) return arr[l];
    int mid = l+(r-l)/2;
    int left= findmajority(arr,l, mid);
    int right = findmajority(arr, mid+1, r);

    if(left== right) return left;

    int c_left = count(arr,l,r,left);
    int c_right = count(arr, l, r, right);

    if(c_left>c_right) return left;
    else return right;

}

int main() {
    int arr[] = {3, 3, 4, 2, 4, 4, 2, 4, 4};
    int n = sizeof(arr) / sizeof(arr[0]);

    int majority = findmajority(arr, 0, n - 1);
    if (majority != -1) {
        cout << "Majority element: " << majority << endl;
    } else {
        cout << "No majority element" << endl;
    }

    return 0;
}