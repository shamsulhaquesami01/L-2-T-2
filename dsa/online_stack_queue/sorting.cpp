
#include <iostream>
#include <vector>
#include<stack>
#include<queue>
using namespace std;

void printstack(stack<int> s){
    int l = s.size();
    while(l--){
        cout<<s.top()<<" ";
        s.pop();
    }
    cout<<endl;
}

void insert_sorted(stack<int>& s, int x){
    if(s.empty() || s.top()>x){
        s.push(x);
        return;
    }
    int val = s.top(); s.pop();
    insert_sorted(s,x);
    s.push(val);
}
void sort_stack(stack<int>& s){
     if(s.empty()) return;
     int x = s.top(); s.pop();
     sort_stack(s);
     insert_sorted(s, x);
}

void sort_no_recursion(stack<int>& st){ 
    stack<int> s;
    while(!st.empty()){
        int x = st.top(); st.pop();
        while(!s.empty() && s.top()<x){
            st.push(s.top());
            s.pop();
        }
        s.push(x);
    }
    while(!s.empty()){
        st.push(s.top());
        s.pop();
    }
}

int main(){
    int t; cin>>t; while(t--){
        int n; cin>>n; 
        stack<int> sami;
        while(n--){
            int a; cin>>a;
            sami.push(a);
        }
        sort_stack(sami);
        printstack(sami);
    }
}