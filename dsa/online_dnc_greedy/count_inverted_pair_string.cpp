#include<iostream>

using namespace std;


int invert_pairs(string s, int l, int r){
    
    if(r<=l) return 0;
    int mid = l+(r-l)/2;
    int left = invert_pairs(s,l,mid);
    int right = invert_pairs(s,mid+1,r);
    if(s[mid-1]>s[mid]) return left+right+1;
    else return left+right;
}

int main(){
    cout<<invert_pairs("abdcbabxawer",0, 11);
}