#include <stdio.h>
#include <stdlib.h>

// Comparator function for sorting
int compare(const void *a, const void *b)
{
    return (*(int *)a - *(int *)b);
}

void findTriplets(int arr[], int n, int target)
{
    // Sort the array
    qsort(arr, n, sizeof(int), compare);

    for (int i = 0; i < n - 2; i++)
    {
        // Skip duplicates for the first element
        if (i > 0 && arr[i] == arr[i - 1])
            continue;

        int left = i + 1;
        int right = n - 1;

        while (left < right)
        {
            int sum = arr[i] + arr[left] + arr[right];

            if (sum == target)
            {
                printf("Triplet found: [%d, %d, %d]\n", arr[i], arr[left], arr[right]);

                // Skip duplicates for the second element
                while (left < right && arr[left] == arr[left + 1])
                    left++;

                // Skip duplicates for the third element
                while (left < right && arr[right] == arr[right - 1])
                    right--;

                left++;
                right--;
            }
            else if (sum < target)
            {
                left++; // Need a larger sum
            }
            else
            {
                right--; // Need a smaller sum
            }
        }
    }
}

int main()
{
    int arr[] = {-1, 0, 1, 2, -1, -4};
    int n = sizeof(arr) / sizeof(arr[0]);
    int target = 0;

    printf("Triplets with sum %d:\n", target);
    findTriplets(arr, n, target);

    return 0;
}