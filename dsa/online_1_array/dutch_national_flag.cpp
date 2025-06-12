 #include<bits/stdc++.h>
 using namespace std;

void dutch(int a[], int n){
  int zero=0,one=0,two=0;
  for(int i=0; i<n; i++) {
    if(a[i]==0) zero++;
    if(a[i]==1) one++;
    if(a[i]==2) two++;
  }
  for(int i=0; i<zero; i++) a[i] =0;
  for(int i=zero; i<zero+one; i++) a[i] =1;
  for(int i=zero+one; i<zero+one+two; i++) a[i]=2;
}

 int main(){

    int arr[] = {};

    dutch(arr, sizeof(arr) / sizeof(arr[0]));
    for (int i = 0; i < sizeof(arr) / sizeof(arr[0]); i++)
        cout << arr[i] << " ";
 
 }