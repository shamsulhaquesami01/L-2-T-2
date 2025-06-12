#include<iostream>
#include<vector>
#include<algorithm>

using namespace std;

int main(){
    vector<int> x;
     vector<int> ::iterator it;
    x.push_back(100);
    x.push_back(10);
    it=x.begin();
    x.insert(it+2,500);
    x.push_back(1000);
    it=x.begin();
    for(int i=0; i<=x.size(); i++){
        cout<<*it<<" ";
        it++;
    }
}