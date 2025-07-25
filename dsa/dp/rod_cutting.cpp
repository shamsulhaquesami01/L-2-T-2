#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
using namespace std;

int rodcutting(vector<int> value, int size)
{
    vector<int> dp(size + 1, 0);
    for (int i = 1; i <= size; i++)
    {
        int maximum = INT_MIN;
        for (int j = 1; j <= i; j++)
        {
            if (value[j - 1] + dp[i - j] > maximum)
            {
                maximum = value[j - 1] + dp[i - j];
            }
        }
        dp[i] = maximum;
    }
    int n = size;
    while (n > 0)
    {
        for (int j = 1; j <= n; j++)
        {
            if (dp[n] == value[j - 1] + dp[n - j])
            {
                cout << j << " ";
                n -= j;
            }
        }
    }
    return dp[size];
}

int main()
{
    int temp1[] = {1, 5, 8, 9, 10, 17, 17, 20};
    vector<int> val(temp1, temp1 + sizeof(temp1) / sizeof(temp1[0]));
    cout << rodcutting(val, 8);
}