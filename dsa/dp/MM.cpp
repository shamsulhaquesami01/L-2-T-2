#include<iostream>
#include<vector>
#include<string>
#include<algorithm>
using namespace std;

int MCM(vector<int> &dim){
    int n= dim.size()-1;
    vector< vector<int> > dp(n+1, vector<int>(n+1,0));
    for(int l=2; l<=n; l++){
        for(int i=1; i <= n-l+1; i++){
            int j = i+l-1;
             dp[i][j] = INT_MAX;
            for(int k=i ; k<=j-1; k++){
              int q = dp[i][k]+dp[k+1][j]+(dim[i-1]*dim[k]*dim[j]);
              dp[i][j] = min(dp[i][j], q);
            }
            
        }
    }
    return dp[1][n];
}

int main(){
        int temp[] = {15,5,50,20,10,35,25};
vector<int> hval(temp, temp + sizeof(temp)/sizeof(temp[0]));
cout<<MCM(hval);
}