#include <iostream>
#include <stack>
#include <cctype> 
// for isdigit()
#include<string>
using namespace std;

int main() {
    string s = string(1,'c');
    string s1 = "123+456*-7*+-89*+";
    stack<int> a; // Store integers, not chars
    //kgf
    for(char x : s1) {
        if(isdigit(x)) {
            a.push(x - '0'); // Convert char to int
        }
        else {
            int right = a.top(); a.pop();
            int left = a.top(); a.pop();
            
            switch(x) {
                case '+': a.push(left + right); break;
                case '-': a.push(left - right); break;
                case '*': a.push(left * right); break;
                case '/': a.push(left / right); break;
            }
        }
    }
    
    cout << "Final result: " << a.top() << endl;
    return 0;
}