
#include <iostream>
#include <vector>
#include<stack>
#include<queue>
using namespace std;
void print(queue<int>q){
    while(!q.empty()){
        cout<<q.front()<<" ";
        q.pop();
    }
    cout<<endl;
}
void sort_qu(queue<int> &q){
    stack<int> s;
    while(!q.empty()){
        int x = q.front(); q.pop();
        while(!s.empty() && s.top()<x){
            q.push(s.top());
            s.pop();
        }
        s.push(x);
    }
    while(!s.empty()){
        q.push(s.top());
        s.pop();
    }
}

int main(){
    queue<int> a;
    a.push(3);a.push(1);a.push(4);a.push(2);
    sort_qu(a);
    print(a);
}
