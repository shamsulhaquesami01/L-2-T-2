#include <iostream>
#include <vector>
using namespace std;

long long mergeCount(vector<char>& s, int l, int r) {
    if (r - l + 1 < 2) return 0;

    int mid = (l + r) / 2;
    long long count = 0;

    count += mergeCount(s, l, mid);
    count += mergeCount(s, mid + 1, r);

    // Count strictly decreasing substrings crossing mid
    int j = mid + 1;
    for (int i = mid; i >= l; i--) {
        while (j <= r && s[i] > s[j]) {
            j++;
        }
        count += (j - (mid + 1));
    }

    return count/2;
}

int main() {
    string str;
    cin >> str;

    vector<char> s(str.begin(), str.end());
    cout << mergeCount(s, 0, s.size() - 1) << endl;
    return 0;
}
