 #include<bits/stdc++.h>
 using namespace std;

 void majority(int a[], int n){
    int max = a[0];
    for(int i=1; i<n; i++) if(a[i]>max) max = a[i];
    int freq[max+1];
    for(int i=0; i<max+1; i++) freq[i] = 0;
    for(int i=0; i<n; i++) freq[a[i]]++;
    for(int i=0; i<n ; i++)
    if(freq[a[i]]>n/2){
     cout<<a[i]<<endl;
     break;
    } 
    
 }
 int main(){
 int a[]={1,2,2,2,2,5};
 majority(a,6);

 }