#include "stack.h"
#include <iostream>
#include <stdexcept>
using namespace std;
// Constructor implementation
ArrayStack::ArrayStack(int initial_capacity)
{
    // TODO: Initialize data members (data, capacity, current_size)
    // TODO: Allocate memory for the array with the specified initial capacity
    data = new int[initial_capacity];
    capacity = initial_capacity;
    current_size = 0 ;
}
//sami 
// Destructor implementation
ArrayStack::~ArrayStack()
{
    delete[] data;
}

// Push implementation
void ArrayStack::push(int item)
{
    // TODO: Check if the array is 50% full (current_size >= capacity/2)
    // TODO: If 50% full, resize the array to double its current capacity
    // TODO: Add the new element to the top of the stack and increment current_size
    if (current_size >= capacity / 2)
    {
        resize(2 * capacity);
    }
    data[current_size++] = item;
}

// Pop implementation
int ArrayStack::pop()
{
    // TODO: Check if the stack is empty, display error message if it is
    // TODO: Decrement current_size and return the element at the top of the stack
    // TODO: If the array is less than 25% full, resize it to half its current capacity
    // TODO: Return the popped element
    if (empty())
    {
        throw underflow_error("stack is empty");
    }
    else
    {
        if(current_size<=capacity/4){
            resize(capacity/2);
        }
        int to_delete = data[current_size - 1];
        current_size--;
        return to_delete;
    }
}

// Clear implementation
void ArrayStack::clear()
{
    // TODO: Reset the stack to be empty (set current_size to 0)
    // TODO: No need to free memory, just logically clear the stack
    current_size = 0;
}

// Size implementation
int ArrayStack::size() const
{
    // TODO: Return the number of elements currently in the stack (current_size)
    return current_size;
}

// Top implementation
int ArrayStack::top() const
{
    // TODO: Check if the stack is empty, display error message if it is
    // TODO: Return the element at the top of the stack without removing it
    if (empty())
    {
        throw underflow_error("stack is empty");
    }
    else
    {
        return data[current_size - 1];
    }
}

// Empty implementation
bool ArrayStack::empty() const
{
    // TODO: Return whether the stack is empty (current_size == 0)
    return (current_size == 0);
}

// Print implementation
void ArrayStack::print() const
{
    // TODO: Print stack elements from top to bottom in the format: |elem1, elem2, ..., elemN>
    cout << "|";
    for (int i = current_size - 1; i >= 0; i--)
    {
        cout << data[i];
        if(i!=0) cout<<", ";
    }
    cout << ">";
}

// Resize implementation
void ArrayStack::resize(int new_capacity)
{
    // TODO: Create a new array with the new capacity
    // TODO: Copy elements from the old array to the new array
    // TODO: Delete the old array
    // TODO: Update the data pointer and capacity
    if(new_capacity<10) new_capacity =10;
    int *arr = new int[new_capacity];
    for (int i = 0; i < current_size; i++)
    {
        arr[i] = data[i];
    }
    delete[] data;
    data = arr;
    capacity = new_capacity;
}


