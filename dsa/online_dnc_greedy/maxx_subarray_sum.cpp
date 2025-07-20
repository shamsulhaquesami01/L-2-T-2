#include <iostream>
#include <climits>
using namespace std;

struct Result {
    int sum;
    int start;
    int end;
};

Result maxCrossingSum(int A[], int l, int m, int r) {
    int leftSum = INT_MIN, sum = 0, maxLeft = m;
    for (int i = m; i >= l; --i) {
        sum += A[i];
        if (sum > leftSum) {
            leftSum = sum;
            maxLeft = i;
        }
    }

    int rightSum = INT_MIN; sum = 0; int maxRight = m + 1;
    for (int i = m + 1; i <= r; ++i) {
        sum += A[i];
        if (sum > rightSum) {
            rightSum = sum;
            maxRight = i;
        }
    }

    return {leftSum + rightSum, maxLeft, maxRight};
}

Result maxSubarraySum(int A[], int l, int r) {
    if (l == r) return {A[l], l, r};

    int m = (l + r) / 2;
    Result left = maxSubarraySum(A, l, m);
    Result right = maxSubarraySum(A, m + 1, r);
    Result cross = maxCrossingSum(A, l, m, r);

    if (left.sum >= right.sum && left.sum >= cross.sum) return left;
    if (right.sum >= left.sum && right.sum >= cross.sum) return right;
    return cross;
}

int main() {
    int n;
    cin >> n;
    int A[n];
    for (int i = 0; i < n; ++i) cin >> A[i];

    Result res = maxSubarraySum(A, 0, n - 1);
    cout << "Max Sum: " << res.sum << "\n";
    cout << "Subarray: ";
    for (int i = res.start; i <= res.end; ++i)
        cout << A[i] << " ";
    cout << endl;
    return 0;
}