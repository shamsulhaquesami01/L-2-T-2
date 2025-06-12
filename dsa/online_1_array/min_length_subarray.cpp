#include <iostream>
#include <climits>
using namespace std;

int minLengthWithExactSum(int arr[], int n, int target) {
    int left = 0, sum = 0;
    int minLen = INT_MAX;

    for (int right = 0; right < n; right++) {
        sum += arr[right];

        while (sum > target && left <= right) {
            sum -= arr[left];
            left++;
        }

        if (sum == target) {
            int len = right - left + 1;
            if (len < minLen)
                minLen = len;
        }
    }

    return (minLen == INT_MAX) ? 0 : minLen;
}

int main() {
    int arr[] = {2, 3, 1, 2, 4, 3};
    int n = sizeof(arr) / sizeof(arr[0]);
    int target = 7;

    int result = minLengthWithExactSum(arr, n, target);
    cout << "Minimum length = " << result << endl;
    return 0;
}
