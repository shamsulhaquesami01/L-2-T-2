#include <iostream>
#include "queue.h"
#include "stack.h"
using namespace std;



// front mane queue er dequeue er side
// back-- queue er enqueue side
struct stackusingqueue
{
    ListQueue *q1 = new ListQueue();
    ListQueue *q2 = new ListQueue();
    void push(int x){
     while(!q1->empty()) {
        q2->enqueue(q1->dequeue());
     }
     q1->enqueue(x);
    while(!q2->empty()) {
        q1->enqueue(q2->dequeue());
     }
    }
    int pop(){
        if(q1->empty()) return -1;
        return q1->dequeue();
    }
};

int main(){
    stackusingqueue sami;
    cout<<sami.pop()<<endl;
    cout<<sami.pop()<<endl;
    sami.push(1);
     sami.push(2);
      sami.push(4);
      cout<<sami.pop()<<endl;
      cout<<sami.pop()<<endl;
}