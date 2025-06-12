 #include<bits/stdc++.h>
 using namespace std;
 void missing(int a[], int n){
  int sum=0;
  for(int i=0; i<n; i++) sum += a[i];
  int actual_sum = ((n+1)*(n+2))/2;
  cout<<actual_sum-sum<<endl;
 }


 void missingNumber(int a[], int n) {
    int xor1 = 0, xor2 = 0;

    // XOR all array elements
    for (int i = 0; i < n; i++)
        xor1 ^= a[i];

    // XOR all numbers from 1 to n+1
    for (int i = 1; i <= n + 1; i++)
        xor2 ^= i;

    int missing = xor1 ^ xor2;

    cout << "Missing number is: " << missing << endl;
}


 int main(){
 int a[]={1,3,4,2};
 missing(a, sizeof(a)/sizeof(a[0]));
 
 }