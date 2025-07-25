#include<iostream>
#include<vector>
#include<string>
#include<algorithm>
using namespace std;


int knap(int n, vector<int> &val, vector <int> & weight, int W){

    vector<int> dp(W+1, 0);
    for(int w=0; w<=W; w++){
        for(int i=0;i<n; i++){
            if(weight[i] <= W) dp[w]= max(dp[w], dp[w-weight[i]]+val[i]);
        }
    }
  return dp[W];
}