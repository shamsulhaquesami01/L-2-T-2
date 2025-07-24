#include<iostream>
#include<vector>
#include<string>
using namespace std;

int editDistanceBottomUp(string X, string Y) {
    int m = X.length();
    int n = Y.length();
    vector<vector<int> > dp(m + 1, vector<int>(n + 1, 0));

    // Base case
    for (int i = 0; i <= m; i++) dp[i][0] = i;  // deletions
    for (int j = 0; j <= n; j++) dp[0][j] = j;  // insertions

    // Fill the table
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (X[i - 1] == Y[j - 1])
                dp[i][j] = dp[i - 1][j - 1];
            else
                dp[i][j] = 1 + min(
                    min(dp[i - 1][j],     // delete
                    dp[i][j - 1]),     // insert
                    dp[i - 1][j - 1]  // replace
                );
        }
    }
    return dp[m][n];
}

int main() {
    string X = "intention";
    string Y = "execution";
    cout << "Edit Distance (Bottom-Up): " << editDistanceBottomUp(X, Y) << endl;

    return 0;
}

