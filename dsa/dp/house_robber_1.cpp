#include<iostream>
#include<vector>
#include<string>
using namespace std;

int house_robber(vector<int> & val){
    int n=val.size();
    vector<int> dp(n+1,0);
    
    dp[0]=0;
    dp[1]=val[0];
    for(int i=2; i<=n; i++){
        dp[i] = max(dp[i-1], val[i-1]+dp[i-2]);
    }
    return dp[n];
}
int main() {
    int temp[] = {6, 7, 1, 3, 8, 2, 4};
vector<int> hval(temp, temp + sizeof(temp)/sizeof(temp[0]));
    cout << house_robber(hval) << endl;
    return 0;
}