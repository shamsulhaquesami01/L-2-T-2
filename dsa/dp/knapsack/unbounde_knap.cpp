#include<iostream>
#include<vector>
#include<string>
#include<algorithm>
using namespace std;



int unboundedKnapsack(int W, vector<int>& wt, vector<int>& val, int n) {
   
    vector<vector<int>> dp(n + 1, vector<int>(W + 1, 0));
    
    for (int i = 1; i <= n; i++) {
        for (int w = 1; w <= W; w++) {
            if (wt[i-1] <= w) {
                // Can include current item multiple times
                dp[i][w] = max(val[i-1] + dp[i][w - wt[i-1]],  // Include
                               dp[i-1][w]);                     // Exclude
            } else {
                // Can't include current item
                dp[i][w] = dp[i-1][w];
            }
        }
    }
    
    return dp[n][W];
}