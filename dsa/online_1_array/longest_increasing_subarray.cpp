 #include<bits/stdc++.h>
 using namespace std;


void inc(int a[], int n){
    int start =0;
    int end=0;
    int max_len=1;
    int len=1;
    for(int i=0; i<n-1; i++){
      
      int j;
       for(j=i+1; j<n; j++){
         if(a[j]>a[j-1]) len ++;
         else break;
       }
       if(len>max_len) {
        max_len = len;
        start =i;
       }
       len=1;
    }
    cout<<"length =" <<max_len<<endl;
    for(int i=start; i<start+max_len; i++) cout<<a[i]<<" ";
}


 int main(){
    int arr[] = {-3,-2,-1,0,-1,2,3};

    inc(arr, sizeof(arr) / sizeof(arr[0]));
 }