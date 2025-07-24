#include <iostream>
#include <climits>
using namespace std;

void printParenthesis(int i, int j, int s[][7], char& name) {
    if (i == j) {
        cout << name++;
        return;
    }
    cout << "(";
    printParenthesis(i, s[i][j], s, name);
    printParenthesis(s[i][j] + 1, j, s, name);
    cout << ")";
}

int main() {
    int n = 6;
    int p[] = {30, 35, 15, 5, 10, 20, 25};

    int m[7][7];  // cost matrix
    int s[7][7];  // split matrix

    // Initialize diagonal to 0 (cost of single matrix is zero)
    for (int i = 1; i <= n; i++)
        m[i][i] = 0;

    // L = chain length
    for (int L = 2; L <= n; L++) {
        for (int i = 1; i <= n - L + 1; i++) {
            int j = i + L - 1;
            m[i][j] = INT_MAX;

            for (int k = i; k < j; k++) {
                int cost = m[i][k] + m[k + 1][j] + p[i - 1] * p[k] * p[j];
                if (cost < m[i][j]) {
                    m[i][j] = cost;
                    s[i][j] = k;
                }
            }
        }
    }

    cout << "Minimum number of multiplications: " << m[1][n] << endl;

    char name = 'A';
    cout << "Optimal Parenthesization: ";
    printParenthesis(1, n, s, name);
    cout << endl;

    return 0;
}
