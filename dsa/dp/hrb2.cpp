#include<iostream>
#include<vector>
#include<string>
#include<algorithm>
using namespace std;

int house_robber(vector<int> & val){
    int n=val.size();
    vector<int> dp(n+1,0);
    if(n==1) return val[0];
    dp[0]=0;
    dp[1]=val[0];
    for(int i=2; i<n; i++){
        dp[i] = max(dp[i-1], val[i-1]+dp[i-2]);
    }
    int ans1 = dp[n-1];

   for(int i=0; i<=n; i++) dp[i] =0;
     for(int i=2; i<=n; i++){
        dp[i] = max(dp[i-1], val[i-1]+dp[i-2]);
    }
    int ans2 = dp[n];

    return max(ans1,ans2);
}
int main() {
    int temp[] = {1,2,3};
vector<int> hval(temp, temp + sizeof(temp)/sizeof(temp[0]));
    cout << house_robber(hval) << endl;
    return 0;
}