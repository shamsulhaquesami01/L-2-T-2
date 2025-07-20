#include <iostream>
#include <algorithm>
using namespace std;

bool task_allocate(int arr[], int n, int k, long long load) {
    long long current_sum = 0;
    int employee = 1;
    for (int i = 0; i < n; i++) {
        if (arr[i] > load) return false; 
        
        if (current_sum + arr[i] > load) {
            employee++;
            current_sum = arr[i];
            if (employee > k) return false;
        } else {
            current_sum += arr[i];
        }
    }
    return true;
}

int minimize_maxworkload(int arr[], int n, int k) {
    if (k > n) return -1;
    
    long long left = *max_element(arr, arr + n);
    long long right = 0;
    for (int i = 0; i < n; i++) {
        right += arr[i];
    }
    
    long long result = right;
    
    while (left <= right) {
        long long mid = left + (right - left) / 2;
        if (task_allocate(arr, n, k, mid)) {
            result = mid;
            right = mid - 1;
        } else {
            left = mid + 1;
        }
    }
    return result;
}

int main(){
   
    int n,k;
    cin>>n>>k;
    int arr[n];
    for(int i=0; i<n ;i++){
        cin>>arr[i];
    }
    cout<<minimize_maxworkload(arr,n,k)<<endl;

}

