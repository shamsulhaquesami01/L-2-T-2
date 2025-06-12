#include<iostream>
using namespace std;

void alter(int a[], int n)
{
    int ve;
    if (a[0] >= 0)
        ve = 1;
    else
        ve = 0;
    for (int i = 1; i < n; i++)
    {
        for (int j = i; j < n; j++)
        {
            if (ve == 1)
            {
                if (a[j] < 0)
                {
                    swap(a[i], a[j]);
                    ve = 0;
                    break;
                }
            }
            if (ve == 0)
            {
                if (a[j] > 0)
                {
                    swap(a[i], a[j]);
                    ve = 1;
                    break;
                }
            }
        }
    }
}

int main()
{
    int arr[] = {-2,-5,3,-1,-6};

    alter(arr, sizeof(arr) / sizeof(arr[0]));
    for (int i = 0; i < sizeof(arr) / sizeof(arr[0]); i++)
        cout << arr[i] << " ";
}