int minCoins(vector<int>& coins, int sum) {
    vector<int> dp(sum + 1, INT_MAX);  // dp[i] = min coins to make i
    dp[0] = 0;  // 0 coins to make sum 0

    for (int coin : coins) {  // Try every coin
        for (int i = coin; i <= sum; i++) {
            if (dp[i - coin] != INT_MAX)
                dp[i] = min(dp[i], dp[i - coin] + 1);
        }
    }

    return dp[sum] == INT_MAX ? -1 : dp[sum];
}
