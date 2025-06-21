/*
Given an array, for each element, find the next greater element on the right using stack.

Input: [4, 5, 2, 25] → Output: 5 25 25 -1 
*/

#include <iostream>
#include <stack>
#include <vector>
using namespace std;

vector<int> nextGreaterElements(vector<int>& arr) {
    int n = arr.size();
    vector<int> result(n);
    stack<int> st;

    for (int i = n - 1; i >= 0; i--) {
        // Pop smaller or equal
        while (!st.empty() && st.top() <= arr[i])
            st.pop();

        if (st.empty())
            result[i] = -1;
        else
            result[i] = st.top();

        st.push(arr[i]);
    }

    return result;
}

int main() {
    vector<int> arr = {4, 5, 2, 25};
    vector<int> ans = nextGreaterElements(arr);
    for (int num : ans)
        cout << num << " ";
    return 0;
}
