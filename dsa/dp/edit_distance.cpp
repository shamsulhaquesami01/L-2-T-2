#include <iostream>
#include <vector>
#include <string>
using namespace std;

int editDistanceBottomUp(string X, string Y)
{
    int m = X.length();
    int n = Y.length();
    vector<vector<int> > dp(m + 1, vector<int>(n + 1, 0));

    // Base case
    for (int i = 0; i <= m; i++)
        dp[i][0] = i*2; // deletions
    for (int j = 0; j <= n; j++)
        dp[0][j] = j*2; // insertions

    // Fill the table
    for (int i = 1; i <= m; i++)
    {
        for (int j = 1; j <= n; j++)
        {
            if (X[i - 1] == Y[j - 1])
                dp[i][j] = dp[i - 1][j - 1];
            else
            {
                int mm = 1 + dp[i - 1][j - 1];
                int in = 2 + dp[i - 1][j];
                int de = 2 + dp[i][j - 1];
                dp[i][j] = min(
                    min(mm, in), de);
            }
        }
    }
    string res1 = "";
    string res2 = "";
    int i = m, j = n;
    while (i > 0 && j > 0)
    {
        if (X[i - 1] == Y[j - 1])
        {
            res1 = X[i - 1] + res1;
            res2 = Y[j - 1] + res2;
            i--;j--;
        }
        else if (dp[i][j] == dp[i - 1][j - 1] + 1)
        {
            res1 = X[i - 1] + res1;
            res2 = Y[j - 1] + res2;
            i--;j--;
        }
        else if (dp[i][j] == dp[i - 1][j] + 2)
        {
            res1 = X[i - 1] + res1;
            res2 = "-" + res2;
            i--;
        }
          else if (dp[i][j] == dp[i][j-1] + 2)
        {
            res1 = X[i - 1] + res1;
            res2 = "-" + res2;
            j--;

        }
    }
    cout << res1 << endl;
    cout << res2 << endl;
    return dp[m][n];
}

int main()
{
    string X = "ATCGGT";
    string Y = "ACCGT";
    cout << editDistanceBottomUp(X, Y) << endl;

    return 0;
}
