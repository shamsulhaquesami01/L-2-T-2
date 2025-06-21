#include "queue.h"
#include <iostream>
using namespace std;
// Constructor implementation
ArrayQueue::ArrayQueue(int initial_capacity)
{
    // TODO: Initialize data members (data, capacity, front_idx, rear_idx)
    // TODO: Allocate memory for the array with the specified initial capacity
    data = new int[initial_capacity];
    capacity = initial_capacity;
    front_idx = rear_idx = -1;
}

// Destructor implementation
ArrayQueue::~ArrayQueue()
{
    delete[] data;
   
    // TODO: Free the dynamically allocated memory for the array
}

// Enqueue implementation (add an item to the rear of the queue)
void ArrayQueue::enqueue(int item)
{
    // TODO: Check if the array is full
    // TODO: If full, resize the array to double its current capacity
    // TODO: Add the new element to the rear of the queue
    if(size()>= capacity){
        resize(capacity*2);
    }
    if(empty()){
        front_idx =rear_idx =0;
    }
    else{
        rear_idx = (rear_idx+1)%capacity;
    }
     data[rear_idx] = item;
}

// Dequeue implementation (remove an item from the front of the queue)
int ArrayQueue::dequeue()
{
    // TODO: Check if the queue is empty, display error message if it is
    // TODO: Decrement current_size and return the element at the front of the queue
    // TODO: Update front index
    // TODO: Return the dequeued element
    int to_delete;
     if(empty()) {
        cout<<"Queue is Empty"<<endl;
        return -1;
    }
    to_delete = data[front_idx];
    if(size() == 1){
        
        front_idx =rear_idx =-1;
        
    }
    else{
        front_idx = (front_idx+1)%capacity;
    }

    if (size() <= capacity / 4 && capacity > 2) {
        resize(capacity / 2);
    }
    return to_delete;
}

// Clear implementation
void ArrayQueue::clear()
{
    // TODO: Reset the queue to be empty (reset capacity, front_idx, rear_idx, data)
    delete[] data;
    data = new int[2];
    capacity = 2;
    front_idx = rear_idx =-1;
}

// Size implementation
int ArrayQueue::size() const
{
    // TODO: Return the number of elements currently in the queue
   if (empty()) return 0;
    return (rear_idx >= front_idx) ? (rear_idx - front_idx + 1) : (capacity - front_idx + rear_idx + 1);
}

// Front implementation
int ArrayQueue::front() const
{
    // TODO: Check if the queue is empty, display error message if it is
    // TODO: Return the element at the front of the queue without removing it
    if(empty()) {
        cout<<"Queue is Empty"<<endl;
        return -1;
    }
    return data[front_idx];
}

// Back implementation (get the element at the back of the queue)
int ArrayQueue::back() const
{
    // TODO: Check if the queue is empty, display error message if it is
    // TODO: Return the element at the back of the queue without removing it
     if(empty()) {
        cout<<"Queue is Empty"<<endl;
        return -1;
    }
    return data[rear_idx];
}

// Empty implementation
bool ArrayQueue::empty() const
{
    // TODO: Return whether the queue is empty (current_size == 0)
    return (front_idx == -1 && rear_idx == -1);
}

// Print implementation
string ArrayQueue::toString() const
{
    // TODO: Convert queue to a string representation in the format: <elem1, elem2, ..., elemN|
    if(empty()) return "<|";
    string result ="<";
    int count =0;
    int size = this->size();
    if(front_idx <= rear_idx){
        for(int i= front_idx; i<=rear_idx; i++){
            result += to_string(data[i]);
            if(++count <size){
                result += ", ";
            }
        }
    }
    else {
        for (int i = front_idx; i < capacity; i++) {
            result += to_string(data[i]);
            if (++count < size) {
                result += ", ";
            }
        }
         for (int i = 0; i <= rear_idx; i++) {
            result += to_string(data[i]);
            if (++count < size) {
                result += ", ";
            }
        }  
}
result += "|";
    return result;
}

// Resize implementation
void ArrayQueue::resize(int new_capacity)
{
    // TODO: Create a new array with the new capacity
    // TODO: Copy elements from the old array to the new array
    // TODO: Delete the old array
    // TODO: Update the data pointer and capacity
    // TODO: Update front and rear indices
    if(new_capacity < 2) new_capacity =2;
    int* new_data = new int[new_capacity];
    int current_size = size();
    if (!empty()) {
        if (front_idx <= rear_idx) {
            for (int i = 0; i < current_size; i++) {
                new_data[i] = data[front_idx + i];
            }
        } else {
            int first_part = capacity - front_idx;
            for (int i = 0; i < first_part; i++) {
                new_data[i] = data[front_idx + i];
            }
            for (int i = 0; i <= rear_idx; i++) {
                new_data[first_part + i] = data[i];
            }
        }
    }
    delete[] data;
    data = new_data;
    capacity = new_capacity;
    front_idx=0;
    rear_idx = current_size-1;
}

int ArrayQueue::getCapacity() const
{
    // TODO: Return the current capacity of the queue
    return capacity;
}