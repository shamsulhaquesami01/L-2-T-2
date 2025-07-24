#include <iostream>
#include <vector>
#include<stack>
using namespace std;


bool isBalanced(const string& str, int & x) {
    int current_depth =0;
    int max_depth =0;
    stack<char> s;
    for (char c : str) {
        if (c == '(' || c == '[' || c == '{') {
            s.push(c);
            max_depth = max( max_depth, (int)s.size());
        } else if (c == ')' || c == ']' || c == '}') {
            if (s.empty()) {
                return false;
            }
            char top = s.top();
            s.pop();
            if ((c == ')' && top != '(') ||
                (c == ']' && top != '[') ||
                (c == '}' && top != '{')) {
                return false;
            }
          
        }
     
    } 
    x = max_depth;
    return s.empty();
}

bool hasDuplicateParentheses(string expr) {
    stack<char> st;

    for (char ch : expr) {
        if (ch == ')') {
            int elementsInside = 0;

            while (!st.empty() && st.top() != '(') {
                st.pop();
                elementsInside++;
            }

            // If there are 0 or 1 characters inside the pair, it's duplicate
            if (elementsInside < 1)
                return true;

            if (!st.empty()) st.pop(); // pop the matching '('
        } else {
            st.push(ch);
        }
    }

    return false;
}


int main(){
    string s = "([{}]{})";
    string s1 = "{(])}";
    string s3 = "((a)+(b)+(";
    int n;
    if(isBalanced(s, n)) cout<<"yes. "<<n<<endl;
    else cout<<"no"<<endl;

     if(isBalanced(s1, n)) cout<<"yes"<<n<<endl;
    else cout<<"no"<<endl;
    cout<<hasDuplicateParentheses(s3)<<endl;
}