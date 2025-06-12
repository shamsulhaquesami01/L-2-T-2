// maximum sum subarray

#include <iostream>

using namespace std;

void kadane(int a[], int n)
{

    int max = INT_MIN;
    int sum = 0;
    int start = 0;
    int temp_start = 0;
    int end = 0;

    for (int i = 0; i < n; i++)
    {
        sum += a[i];

        if (sum > max)
        {
            max = sum;
            start = temp_start;
            end = i;
        }
        if (sum < 0)
        {
            sum = 0;
            temp_start = i + 1;
        }
    }

    
    printf("Maximum Subarray Sum: %d\n", max);
    printf("Subarray: [");
    for (int i = start; i <= end; i++)
    {
        printf("%d", a[i]);
        if (i != end)
            printf(", ");
    }
    printf("]\n");
}

int main() {
    int arr[] = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
    int n = sizeof(arr) / sizeof(arr[0]);

    kadane(arr, n);

    return 0;
}
