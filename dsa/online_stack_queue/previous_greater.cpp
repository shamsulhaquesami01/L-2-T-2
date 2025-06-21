
#include <iostream>

#include "stack.h"
using namespace std;

void prevgreat(int arr[], int n){
    ArrayStack *s = new ArrayStack();
   
    for(int i=0; i<n; i++){
        while(!s->empty() && s->top() <= arr[i]) s->pop();

        if(s->empty()) cout<<-1<<" ";
        else cout<<s->top()<<" ";

        s->push(arr[i]);
    }

}

int main()
{
    int arr[] = { 10, 4, 2, 20, 40, 12, 30 };
    int n = sizeof(arr) / sizeof(arr[0]);
    prevgreat(arr, n);
    return 0;
}