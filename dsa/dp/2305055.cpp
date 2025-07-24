#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
using namespace std;

int score(char a, char b, int match_score, int mismatch_penalty) {
    return (a == b) ? match_score : mismatch_penalty;
}

void globalAlignment(string X, string Y, int match_score, int mismatch_penalty, int gap_penalty) {
    int m = X.length(), n = Y.length();
    vector<vector<int> > dp(m + 1, vector<int>(n + 1));

    // jekono ekta string null
    for (int i = 0; i <= m; i++) dp[i][0] = i * gap_penalty;
    for (int j = 0; j <= n; j++) dp[0][j] = j * gap_penalty;

    
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            int match = dp[i - 1][j - 1] + score(X[i - 1], Y[j - 1], match_score, mismatch_penalty);
            int del = dp[i - 1][j] + gap_penalty;
            int ins = dp[i][j - 1] + gap_penalty;
            dp[i][j] = max(max(match, del), ins);
        }
    }

    string alignX = "", alignY = "";
    int i = m, j = n;
    while (i > 0 || j > 0) {
        if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + score(X[i - 1], Y[j - 1], match_score, mismatch_penalty)) {
            alignX = X[i - 1] + alignX;
            alignY = Y[j - 1] + alignY;
            i--; j--;
        } else if (i > 0 && dp[i][j] == dp[i - 1][j] + gap_penalty) {
            alignX = X[i - 1] + alignX;
            alignY = '-' + alignY;
            i--;
        } else {
            alignX = '-' + alignX;
            alignY = Y[j - 1] + alignY;
            j--;
        }
    }

    cout << "Global Alignment:\n";
    cout << "Aligned X: " << alignX << endl;
    cout << "Aligned Y: " << alignY << endl;
    cout << "Alignment Score: " << dp[m][n] << endl;
}

void localAlignment(string X, string Y, int match_score, int mismatch_penalty, int gap_penalty) {
    int m = X.length(), n = Y.length();
    vector<vector<int> > dp(m + 1, vector<int>(n + 1, 0));

    int maxScore = 0, end_i = 0, end_j = 0;

    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            int match = dp[i - 1][j - 1] + score(X[i - 1], Y[j - 1], match_score, mismatch_penalty);
            int del = dp[i - 1][j] + gap_penalty;
            int ins = dp[i][j - 1] + gap_penalty;
            dp[i][j] = max(max(0, match), max(del, ins));

            if (dp[i][j] > maxScore) {
                maxScore = dp[i][j];
                end_i = i;
                end_j = j;
            }
        }
    }

    string alignX = "", alignY = "";
    int i = end_i, j = end_j;
    while (i > 0 && j > 0 && dp[i][j] > 0) {
        if (dp[i][j] == dp[i - 1][j - 1] + score(X[i - 1], Y[j - 1], match_score, mismatch_penalty)) {
            alignX = X[i - 1] + alignX;
            alignY = Y[j - 1] + alignY;
            i--; j--;
        } else if (dp[i][j] == dp[i - 1][j] + gap_penalty) {
            alignX = X[i - 1] + alignX;
            alignY = '-' + alignY;
            i--;
        } else {
            alignX = '-' + alignX;
            alignY = Y[j - 1] + alignY;
            j--;
        }
    }

    cout << "\nLocal Alignment:\n";
    cout << alignX << "\n";
    cout << alignY << "\n";
    cout << "Maximum Score: " << maxScore << endl;
}

int main() {
    string x, y;
    int match, mismatch, gap;

    cout << "Enter first sequence: ";
    cin >> x;
    cout << "Enter second sequence: ";
    cin >> y;
    cout << "Enter match score: ";
    cin >> match;
    cout << "Enter mismatch score: ";
    cin >> mismatch;
    cout << "Enter gap penalty: ";
    cin >> gap;

    globalAlignment(x, y, match, mismatch, gap);
    localAlignment(x, y, match, mismatch, gap);
}
