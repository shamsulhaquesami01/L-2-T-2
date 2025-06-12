

#include<bits/stdc++.h>

using namespace std;
struct node{
    int data;
    node* next;
};
void addnodebeggining(int value, node*& head){
    node* newnode = new node();
    newnode->data= value;
    newnode->next = head;
    head = newnode;
}
int main(){
   node s;
}