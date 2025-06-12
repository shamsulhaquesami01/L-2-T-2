#include <iostream>
using namespace std;

// Define a Node structure
struct Node {
    int data;
    Node* next;
};

// Function to insert a node at the beginning
void insertAtBeginning(Node*& head, int value) {
    Node* newNode = new Node();
    newNode->data = value;
    newNode->next = head;
    head = newNode;
}

// Function to insert at the end
void insertAtEnd(Node*& head, int value) {
    Node* newNode = new Node();
    newNode->data = value;
    newNode->next = nullptr;

    if (head == nullptr) {
        head = newNode;
        return;
    }

    Node* temp = head;
    while (temp->next != nullptr) {
        temp = temp->next;
    }
    temp->next = newNode;
}

// Function to delete a node with a specific value
void deleteNode(Node*& head, int value) {
    if (head == nullptr) return;

    // If the node to be deleted is the head
    if (head->data == value) {
        Node* temp = head;
        head = head->next;
        delete temp;
        return;
    }

    Node* current = head;
    while (current->next != nullptr && current->next->data != value) {
        current = current->next;
    }

    if (current->next == nullptr) return; // Value not found

    Node* temp = current->next;
    current->next = temp->next;
    delete temp;
}

// Function to display the linked list
void display(Node* head) {
    Node* temp = head;
    while (temp != nullptr) {
        cout << temp->data << " -> ";
        temp = temp->next;
    }
    cout << "NULL" << endl;
    
}

int main() {
    Node* head = nullptr; // Start with an empty list

    insertAtBeginning(head, 10);
    insertAtBeginning(head, 20);
    insertAtEnd(head, 30);
    insertAtEnd(head, 40);
    
    cout << "Linked List: ";
    display(head);

    deleteNode(head, 20);
    cout << "After deleting 20: ";
    display(head);

    return 0;
}
