
#include<iostream>
#include<vector>
#include<numeric>
#include<string>
#include<algorithm>
using namespace std;


bool equalPartition(vector<int>& arr) {


    int sum = accumulate(arr.begin(), arr.end(), 0);
    int n = arr.size();

    if (sum % 2 != 0)
        return false;
        
    sum = sum/2;

    vector<vector<bool> > dp(n + 1, vector<bool>(sum + 1, false));

    // If sum is 0, then answer is true (empty subset)
    for (int i = 0; i <= n; i++)
        dp[i][0] = true;

    // Fill the dp table in bottom-up manner
    for (int i = 1; i <= n; i++) {
      
        for (int j = 0; j <= sum; j++) {
            if (j < arr[i - 1]) {
              
                // Exclude the current element
                dp[i][j] = dp[i - 1][j]; 
            }
            else {
              
                // Include or exclude
                dp[i][j] = dp[i - 1][j] 
                || dp[i - 1][j - arr[i - 1]];
            }
        }
    }

    return dp[n][sum];
}

int main() {
   // vector<int> arr = { 1, 5, 11, 5};
    int temp[] = {1, 5, 11, 5};
vector<int> val(temp, temp + sizeof(temp)/sizeof(temp[0]));
    if (equalPartition(val)) {
        cout << "True" << endl;
    }else{
        cout << "False" << endl;
    }
    return 0;
}