
#include<iostream>
using namespace std;

void maxSubarraySumAtMostTarget(int arr[], int n, int target) {
    int left = 0, sum = 0;
    int maxLen = 0;

    for (int right = 0; right < n; ++right) {
        sum += arr[right];

        while (sum > target && left <= right) {
            sum -= arr[left];
            left++;
        }

    
        if (sum == target)
            maxLen = max(maxLen, right-left+1);
    }

    cout << "Maximum length subarray with sum ≤ " << target << " = " << maxLen << endl;
}
int main() {
    int arr[] = {2, 1, 5, 2, 3, 2};
    int n = sizeof(arr) / sizeof(arr[0]);
    int target = 7;

    maxSubarraySumAtMostTarget(arr, n, target);
    

    return 0;
}
