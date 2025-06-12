#include <iostream>
using namespace std;

class Stack
{
private:
    int *array;
    int size ;
    int capacity;
    //write your code here. Add additional private variables if necessary

public:
    // Constructor
    Stack()
    {
        capacity =1;
        array = new int[1]; 
        size =0;
        //write your code here. Initialize additional private variables if necessary
    }
    Stack(int initialCapacity)
    {
        capacity = initialCapacity;
        array = new int[capacity]; 
        size =0;// Allocate initial memory
        //write your code here. Initialize additional private variables if necessary
    }

    // Destructor
    ~Stack()
    {
        delete[] array; // Free dynamically allocated memory
    }

    // Helper function to resize the array when full
    void resize(int newCapacity)
    {
        int *newArray = new int[newCapacity];
         // Allocate new array
         for(int i=0; i<size; i++){
            newArray[i] = array[i];
         }
        
        //write your code here. Copy the elements from the old array to the new array

        delete[] array; // Free old memory
        array = newArray;
        capacity = newCapacity;
        //write your code here. Update the capacity and array pointers
    }

    // Push an element onto the stack
    void push(int x)
    {
        if(size>= capacity) resize(capacity*2);
        
        array[size] =x;
        size++;
       
        //write your code here. Check if the array is full and resize if necessary.
        //push the element onto the stack
    }

    // Remove and return the top element
    int pop()
    {
        
        if(size==0) return -1;
        int x = array[size-1];
        size--;
        return x;

       //write your code here. Check if the stack is empty and return -1 if it is.
       //pop the top element and return it
       //resize the array if necessary
    }

    // Return the top element without removing it
    int top()
    {
        if(size==0) return -1;
        return array[size-1];
        //write your code here. Check if the stack is empty and return -1 if it is.
        //return the top element
    }

    // Return the number of elements in the stack
    int length()
    {
        return size;
        //write your code here. Return the number of elements in the stack
    }

    // Check if the stack is empty
    bool isEmpty()
    {
        if(size ==0 ) return true;
        else return false;
        //write your code here. Return true if the stack is empty, false otherwise
    }

    // Clear the stack
    void clear()
    {
        size =0;
        //write your code here. Clear the stack. resize the array to 1
        
    }
};
