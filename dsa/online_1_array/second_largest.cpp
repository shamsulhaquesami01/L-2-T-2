 #include<bits/stdc++.h>
 using namespace std;
 int main(){
   int a[]= {23,23,23,23,23};
   int n=sizeof(a)/sizeof(a[0]);
   int large = a[0];
   int s_large = INT_MIN;

   for(int i=1; i<n; i++){

   if(a[i] >= large && a[i] >= s_large) {
    s_large = large;
    large = a[i];
    
   }
   else if ( a[i] >= s_large && a[i] <= large) s_large = a[i];

   }
   cout<<s_large<<endl;
 }

 