#include <iostream>
#include <vector>
#include <string>
using namespace std;

int lcs(string X, string Y)
{
    int m = X.size();
    int n = Y.size();
    vector<vector<int> > dp(m + 1, vector<int>(n + 1, 0));
    for (int i = 1; i <= m; i++)
    {
        for (int j = 1; j <= n; j++)
        {
            if (X[i - 1] == Y[j - 1])
                dp[i][j] = 1 + dp[i - 1][j - 1];
            else
                dp[i][j] = max(dp[i - 1][j], dp[i][j - 1]);
        }
    }
    string res = "";
    int i = m, j = n;
    while (i > 0 && j > 0)
    {
        if (X[i - 1] == Y[j - 1])
        {
            res = X[i - 1] + res;
            i--;
            j--;
        }
        else if (dp[i - 1][j] > dp[i][j - 1])
        {
            i--;
        }
        else
        {
            j--;
        }
    }
    cout << res << endl;
    return dp[m][n];
}

int main()
{
    cout << lcs("ABCBDAB", "BDCABA");
}