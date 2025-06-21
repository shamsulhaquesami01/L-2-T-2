
#include <iostream>
#include "queue.h"
#include "stack.h"
using namespace std;

void reverse(ListQueue *q){
    if(q->empty()) return ;
    int x = q->dequeue();
    reverse(q);
    q->enqueue(x);
}

int main(){
    ListQueue *q = new ListQueue();
    q->enqueue(56);
    q->enqueue(27);
    q->enqueue(30);
    q->enqueue(45);
    q->enqueue(85);
    q->enqueue(92);
    q->enqueue(58);
    q->enqueue(80);
    q->enqueue(90);
    q->enqueue(100);
    reverse(q);
    string res = q->toString();
    cout<<res;
}