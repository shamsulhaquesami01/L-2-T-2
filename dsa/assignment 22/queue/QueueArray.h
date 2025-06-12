#include <iostream>
using namespace std;

class Queue
{
private:
    int *array;
    int capacity;
    int front;
    int rear;
    int size ;
    //write your code here. Add additional private variables if necessary

public:
    // Constructor
    Queue()
    {
        capacity=1;
        array = new int[1];
        front=-1;
        rear=-1;
        size =0;
        //write your code here. Initialize additional private variables if necessary
    }
    Queue(int initialCapacity)
    {
        array = new int[initialCapacity];
        capacity = initialCapacity;
        front=-1;
        rear=-1;
         // Allocate initial memory
        //write your code here. Initialize additional private variables if necessary
    }

    // Destructor
    ~Queue()
    {
        delete[] array; // Free dynamically allocated memory
    }

    // Helper function to resize the array when full
    void resize(int newCapacity)
    {
        int *newArray = new int[newCapacity]; 
        int j=0;
        for(int i=front; i!=rear; i=(i+1)%capacity){
            newArray[j++]=array[i];
        }
        newArray[j]=array[rear];
        //write your code here. Copy the elements from the old array to the new array

        delete[] array; // Free old memory
        array = newArray;
        front =0;
        rear = size-1;
        capacity=newCapacity;
        //write your code here. Update the capacity and array pointers
    }

    // Enqueue an element onto the queue
    void enqueue(int x)
    {
        if(size >= capacity) {
            resize(2*capacity);
        }
        if(size==0) {
            front = rear = 0;
            array[front] = x;
            size++;
        }
        else{
        rear=(rear+1)%capacity;
        array[rear] =x;
        size++;
        }
        //write your code here. Check if the array is full and resize if necessary.
    }

    // Remove and return the peek element
    int dequeue()
    {
        if(size==0) return -1;
        int x = array[front];
        front = (front+1)%capacity;
        size--;
        if (size <= capacity / 4 && capacity > 1) {
            resize(capacity / 2);
        }
        return x;
       //write your code here. Check if the stack is empty and return -1 if it is.
       //remove the peek element and return it
       //resize the array if necessary
    }

    // Return the peek element without removing it
    int peek()
    {
        if(size==0) return -1;
        return array[front];
        //write your code here. Check if the queue is empty and return -1 if it is.
        //return the peek element
    }

    // Return the number of elements in the queue
    int length()
    {
        return size;
        //write your code here. Return the number of elements in the queue
    }

    // Check if the queue is empty
    bool isEmpty()
    {
        if(size==0) return true;
        else return false;
        //write your code here. Return true if the queue is empty, false otherwise
    }

    // Clear the queue
    void clear()
    {
        rear =-1;
        front =-1;
        size=0;
        resize(1);
        //write your code here. Clear the queue. resize the array to 1

    }
};
