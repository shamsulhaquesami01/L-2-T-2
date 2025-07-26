#include <iostream>
#include <vector>
#include <string>
using namespace std;

string longestPalindrome(string s) {
    int n = s.length();
    if (n == 0) return "";
    
    // DP table: dp[i][j] = true if s[i..j] is palindrome
    vector<vector<bool> > dp(n+1, vector<bool>(n+1, false));
    
    // All substrings of length 1 are palindromes
    for (int i = 1; i < n; i++) {
        dp[i][i] = true;
    }
    
    int start = 0;
    int maxLength = 1;
    
    // Check for substrings of length 2
    for (int i = 1; i < n; i++) {
        if (s[i-1] == s[i]) {
            dp[i][i + 1] = true;
            start = i-1;
            maxLength = 2;
        }
    }
    
    // Check for lengths greater than 2
    for (int len = 3; len <= n; len++) {
        for (int i = 1; i <= n - len + 1; i++) {
            int j = i + len - 1;  // Ending index
            
            // Check if the first and last characters match
            // and the substring between them is a palindrome
            if (s[i-1] == s[j-1] && dp[i + 1][j - 1]) {
                dp[i][j] = true;
                if (len > maxLength) {
                    start = i-1;
                    maxLength = len;
                }
            }
        }
    }
    
    return s.substr(start, maxLength);
}

int main() {
    string s = "babad";
    cout << "Longest palindromic substring: " << longestPalindrome(s) << endl;
    return 0;
}