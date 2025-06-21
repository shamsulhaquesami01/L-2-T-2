/*
Given a stack St of M elements and
 a queue Q of N elements-> The task is to put every 
 \element of stack into the queue and every element of the queue into the stack without changing their order.


 Input: St = {4, 3, 2, 1}, Q = {8, 7, 6, 5}
Output: St = {8, 7, 6, 5}, Q = {1, 2, 3, 4}
Input: St = {0, 1}, Q = {2, 3}
Output: St = {2, 3}, Q = {0, 1}

in o(1) space
*/


#include <iostream>
#include "queue.h"
#include "stack.h"
using namespace std;

void print(ListStack  s){
    while(!s.empty()){
        cout<<s.pop()<<" ";
    }
    cout<<endl;
}

void printq(ArrayQueue q){
     while(!q.empty()){
        cout<<q.dequeue()<<" ";
    }
    cout<<endl;
}

void exchange(ArrayQueue *q, ListStack*s){
    int size_q = q->size();
    int temp = size_q;
    int size_s = s->size();
    while(!q->empty()){
        s->push(q->dequeue());
    }
    while(size_q--) q->enqueue(s->pop());
    while(!s->empty()) q->enqueue(s->pop());
    while(temp--) s->push(q->dequeue());

    while(!q->empty()) s->push(q->dequeue());
    while(size_s--) q->enqueue(s->pop());
}

int main(){
    ArrayQueue *q = new ArrayQueue(); 
    ListStack *s = new ListStack();
     s->push(4);
    s->push(3);
    s->push(2);
    s->push(1);
    q->enqueue(8);
    q->enqueue(7);
    q->enqueue(6);
    q->enqueue(5);
    s->print();
    string res = q->toString();
    cout<<res;
    exchange(q,s);
   s->print();
   //
    res = q->toString();
    cout<<res;
}


//g++ -std=c++11 ListStack.cpp arrayqueue.cpp liststack.cpp listqueue.cpp