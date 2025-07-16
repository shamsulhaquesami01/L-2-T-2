#ifndef HEAP_H
#define HEAP_H

#include <iostream>
#include <vector>
#include <algorithm>

class Heap {
private:
    int* array;
    int currentSize;
    int capacity;

    void heapifyUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (array[parent] >= array[index]) {
                break;
            }
            std::swap(array[parent], array[index]);
            index = parent;
        }
    }

    void heapifyDown(int index) {
        while (true) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int largest = index;

            if (leftChild < currentSize && array[leftChild] > array[largest]) {
                largest = leftChild;
            }
            if (rightChild < currentSize && array[rightChild] > array[largest]) {
                largest = rightChild;
            }

            if (largest == index) {
                break;
            }

            std::swap(array[index], array[largest]);
            index = largest;
        }
    }

    void resize(int newCapacity) {
        int* newArray = new int[newCapacity];
        for (int i = 0; i < currentSize; i++) {
            newArray[i] = array[i];
        }
        delete[] array;
        array = newArray;
        capacity = newCapacity;
    }

public:
    
    Heap(int initialCapacity) : capacity(initialCapacity), currentSize(0) {
        array = new int[capacity];
    }

   
    Heap(const std::vector<int>& numbers) : capacity(numbers.size()), currentSize(numbers.size()) {
        array = new int[capacity];
        for (int i = 0; i < currentSize; i++) {
            array[i] = numbers[i];
        }
        
        for (int i = currentSize / 2 - 1; i >= 0; i--) {
            heapifyDown(i);
        }
    }

    
    ~Heap() {
        delete[] array;
    }

    
    Heap(const Heap& other) : capacity(other.capacity), currentSize(other.currentSize) {
        array = new int[capacity];
        for (int i = 0; i < currentSize; i++) {
            array[i] = other.array[i];
        }
    }

    
    Heap& operator=(const Heap& other) {
        if (this != &other) {
            delete[] array;
            capacity = other.capacity;
            currentSize = other.currentSize;
            array = new int[capacity];
            for (int i = 0; i < currentSize; i++) {
                array[i] = other.array[i];
            }
        }
        return *this;
    }

    
    int size() const {
        return currentSize;
    }

   
    void insert(int data) {
        if (currentSize >= capacity) {
            resize(capacity * 2);
        }
        array[currentSize] = data;
        heapifyUp(currentSize);
        currentSize++;
    }

   
    int getMax() const {
        if (currentSize == 0) {
            throw std::runtime_error("Heap is empty");
        }
        return array[0];
    }

    
    void deleteKey() {
        if (currentSize == 0) {
            throw std::runtime_error("Heap is empty");
        }
        array[0] = array[currentSize - 1];
        currentSize--;
        if (currentSize > 0 && currentSize <= capacity / 4) {
            resize(capacity / 2);
        }
        heapifyDown(0);
    }
};

void heapsort(std::vector<int>& numbers) {
    Heap h(numbers);
    numbers.clear();
    while (h.size() > 0) {
        numbers.push_back(h.getMax());
        h.deleteKey();
    }
}

#endif 