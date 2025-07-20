#include <iostream>
#include <algorithm>
#include<vector>
using namespace std;

bool seating_arrangement(int arr[], int n, int b, int m)
{

    int seat_per_bench = n / b;
    int available = 0;
    for (int i = 0; i < b; i++)
    {
        int start = i * seat_per_bench;
        vector<bool> occupied(seat_per_bench,false);
        for (int j = 0; j < seat_per_bench; j++)
        {
            int seat_no = start + j;
            if (arr[seat_no] == 1)
            {
                if (j + 1 < seat_per_bench)
                    occupied[j + 1] = true;
            }
            else if (arr[seat_no] == 2)
            {
                if (j - 1 >= 0)
                    occupied[j - 1] = true;
                if (j + 1 < seat_per_bench)
                    occupied[j + 1] = true;
            }
        }
        for (int k = 0; k < seat_per_bench; k++)
        {
            int seat_no = start + k;
            if (arr[seat_no] == 0 && occupied[k] == false)
            {
                available++;
                if (available >= m)
                    return true;
            }
        }
       
    }
    return (available >= m);
}

int main()
{

    int n, b, m;
    cin >> n >> b;
    int arr[n];
    for (int i = 0; i < n; i++)
    {
        cin >> arr[i];
    }
    cin >> m;
    if(seating_arrangement(arr,n,b,m)) cout<<"true"<<endl;
    else cout<<"false"<<endl;
}

