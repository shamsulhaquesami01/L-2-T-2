#include <iostream>
#include "queue.h"
#include "stack.h"
using namespace std;



// front mane queue er dequeue er side
// back-- queue er enqueue side
struct dequeueusingqueue
{
    ListQueue *q = new ListQueue();
    void push_back(int x){
        q->enqueue(x);
    }
    void push_front(int x){
        int size = q->size();
        q->enqueue(x);
        while(size--) q->enqueue(q->dequeue());
    }
    int pop_front(){
       int x = q->dequeue();
       return x;
    }
    int pop_back(){
        int size = q->size();
        size-=1;
        for(int i=0; i<size; i++) q->enqueue(q->dequeue());
        int x = q->dequeue();
        return x;
    }
};

int main(){
    dequeueusingqueue sami;
    cout<<sami.pop_front()<<endl;
    cout<<sami.pop_back()<<endl;
    sami.push_front(1);
     sami.push_front(2);
      sami.push_back(4);
      cout<<sami.pop_back()<<endl;
      cout<<sami.pop_front()<<endl;
}