#include <iostream>
using namespace std;

class Node
{
public:
    int data;
    Node *next;

    Node(int value)
    {
        data = value;
        next = nullptr;
    }

    Node(int value, Node *nextNode)
    {
        data = value;
        next = nextNode;
    }
};

class Stack
{
private:
    Node *topNode; // Points to the top of the stack
    int currentSize;

public:
    // Constructor
    Stack()
    {
        topNode = nullptr;
        currentSize = 0;
    }

    // Push an element onto the stack
    void push(int x)
    {
       //write your code here. Create a new node with the value x and push it onto the stack
       //update the topNode and currentSize
       Node* newnode = new Node(x, topNode);
       topNode = newnode;
       currentSize++;
    }

    // Remove and return the top element
    int pop()
    {

        if(isEmpty()) return -1;
        Node* temp = topNode;
        int x = temp->data;
        topNode= topNode->next;
        delete temp;
        currentSize--;
        return x;
        //write your code here. Check if the stack is empty and return -1 if it is.
        //pop the top element and return it
        //update the topNode and currentSize
        //delete the node that was popped
    }

    // Return the top element without removing it
    int top()
    { if(isEmpty()) return -1;
        return topNode->data;
        //write your code here. Check if the stack is empty and return -1 if it is.
        //return the top element
    }

    // Return the number of elements in the stack
    int length()
    {
        return currentSize;
        //write your code here. Return the number of elements in the stack
    }

    // Check if the stack is empty
    bool isEmpty()
    {
        return currentSize==0;
        //write your code here. Return true if the stack is empty, false otherwise
    }

    // Clear the stack
    void clear()
    {
        while (!isEmpty())
        {
            pop(); // Continuously pop elements until the stack is empty
        }
    }

    // Destructor
    ~Stack()
    {
        while (!isEmpty())
        {
            pop(); // Continuously pop elements until the stack is empty
        }
    }
};
