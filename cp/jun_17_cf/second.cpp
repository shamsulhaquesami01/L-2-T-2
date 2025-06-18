#include <iostream>
#include <string>

using namespace std;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);

    int t;
    cin >> t;
    while (t--) {
        int n;
        string s;
        cin >> n >> s;

        bool found = false;
      
        for (int i = 1; i < n - 1 && !found; ++i) {
            string a = s.substr(0, i);
            string b = s.substr(i, 1); 
            string c = s.substr(i + 1);
            string ac = a + c;
            if (ac.find(b) != string::npos) {
                found = true;
            }
        }

        string a = s.substr(0, 1);
        string b = s.substr(1, n - 2);
        string c = s.substr(n - 1);
        string ac = a + c;
        if (ac.find(b) != string::npos) {
            found = true;
        }

        cout << (found ? "Yes" : "No") << '\n';
    }
    return 0;
}