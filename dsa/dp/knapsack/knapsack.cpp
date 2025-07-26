#include<iostream>
#include<vector>
#include<string>
#include<algorithm>
using namespace std;


int knap(vector<int> &val, vector <int> & weight, int W){
    int n = val.size();
    vector< vector<int> > dp(n+1, vector<int>(W+1,0));

    for(int i=1; i<=n; i++){
        for(int w=0; w<=W; w++){
            if(weight[i-1]<=w) dp[i][w]= max(val[i-1]+dp[i-1][w-weight[i-1]],dp[i-1][w]);
            else dp[i][w]=dp[i-1][w];
        }
    }
    int i=n, j =W;
    while(i>0 && j>0){
        if(dp[i][j] != dp[i-1][j]) 
        {
            cout<<"item "<<i<<endl;
            j= j-weight[i-1];
            i--;
            
        }
        else{
            i--;
        }

    }
    return dp[n][W];
}

int main(){
        int temp1[] = {42,40,12,25};
vector<int> val(temp1, temp1 + sizeof(temp1)/sizeof(temp1[0]));
    int temp2[] = {7,4,3,5};
vector<int> w(temp2, temp2 + sizeof(temp2)/sizeof(temp2[0]));
cout<<knap(val,w,7);
}