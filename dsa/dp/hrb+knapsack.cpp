#include<iostream>
#include<vector>
#include<string>
using namespace std;

int hrbUpdated(vector<int> &val, vector<int> &weight, int n, int W){
    vector<vector<vector<int> > > dp(n+1, vector<vector<int> >(W+1, vector<int>(2,0)));

    for(int i=1; i<=n; i++){
        for(int w=0; w<=W; w++){
            dp[i][w][0] = max(dp[i-1][w][0], dp[i-1][w][1]);
            if(w >= weight[i-1]){
              dp[i][w][1] = dp[i-1][w-weight[i-1]][0]+val[i-1];  
            }
        }
    }
    return max(dp[n][W][0], dp[n][W][1]);
}
int main(){
        int temp[] = {60,120,80,100,70};
vector<int> val(temp, temp + sizeof(temp)/sizeof(temp[0]));
        int temp2[] = {10,20,30,20,10};
vector<int> w(temp2, temp + sizeof(temp)/sizeof(temp[0]));
cout<<hrbUpdated(val,w,5,50);
}