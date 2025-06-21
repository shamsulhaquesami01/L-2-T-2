#include <iostream>
#include <stack>
using namespace std;

void deleteMid(stack<int>& s)
{
    int n = s.size();
    stack<int> temp;
    int count = 0;

    while (count < n / 2) {
        int c = s.top();
        s.pop();
        temp.push(c);
        count++;
    }

    s.pop();

    while (!temp.empty()) {
        s.push(temp.top());
        temp.pop();
    }
}

int main()
{
    stack<int> s;

    s.push(10);
    s.push(20);
    s.push(30);
    s.push(40);
    s.push(50);

    deleteMid(s);

    while (!s.empty()) {
        int p = s.top();
        s.pop();
        cout << p << " ";
    }
    return 0;
}