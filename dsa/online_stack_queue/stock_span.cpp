
#include <iostream>
#include "queue.h"
#include "stack.h"
using namespace std;


void calculatespan(int arr[], int n){
    ArrayStack *s = new ArrayStack();

    for(int i=0; i<n; i++){
        while(!s->empty() && arr[s->top()]<= arr[i]) s->pop();
        if(s->empty()) cout<<i+1<<",";
        else cout<<i-s->top()<<",";
        s->push(i);
    }
}

int main(){
    int arr[] = {10,4,5,90,120,80};
    calculatespan(arr, sizeof(arr)/sizeof(int));
}