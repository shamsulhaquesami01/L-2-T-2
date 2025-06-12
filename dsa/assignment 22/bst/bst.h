#include <iostream>

using namespace std;

struct Node
{
    int val;
    Node *left;
    Node *right;
};

Node *insertNode(Node *node, int val)
{
    // Write your codes here
    // This function takes a pointer to the root of the BST and 
    // the value to be inserted
    // Returns the pointer to the root of the modified BST.
}

Node *deleteNode(Node *node, int val)
{
    // Write your codes here
    // This function takes a pointer to the root of the BST and 
    // the value to be deleted.
    // Returns the pointer to the root of the modified BST.
}

Node* find(Node *node, int val)
{
    // Write your codes here
    // This function takes a pointer to the root of the BST and 
    // the value to be searched.
    // Returns the pointer to the node that contains val.
    // Returns NULL if not found.
}

void inOrder(Node *node)
{
    // Write your codes here
    // This function takes a pointer to the root of the BST
    // Prints the in-order traversal of the given BST
}

void preOrder(Node *node)
{
    // Write your codes here
    // This function takes a pointer to the root of the BST
    // Prints the pre-order traversal of the given BST
}

void postOrder(Node *node)
{
    // Write your codes here
    // This function takes a pointer to the root of the BST
    // Prints the post-order traversal of the given BST
}

void printTree(Node *root){
    // Write your codes here
    // This function takes a pointer to the root of the BST
    // Prints parenthesized representation of the given BST
}