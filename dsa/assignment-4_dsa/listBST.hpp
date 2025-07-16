#ifndef LISTBST_H
#define LISTBST_H
#include<math.h>
#include "BST.hpp"
#include <iostream>
#include <stdexcept>
#include<algorithm>
/**
 * Binary Search Tree implementation using linked list structure
 *
 * @tparam Key - The type of keys stored in the BST
 * @tparam Value - The type of values associated with keys
 */
template <typename Key, typename Value>
class ListBST : public BST<Key, Value>
{
private:
    /**
     * Node class for the binary search tree
     */
    class Node
    {
    public:
        Key key;
        Value value;
        Node *left;
        Node *right;
        int height;

        Node(Key k, Value v) : key(k), value(v), left(nullptr), right(nullptr) {}
    };

    Node *root;
    size_t node_count;

    // TODO: Implement private helper functions as needed
    // Start your private helper functions here
    int getHeight(Node* node){
        if(node == nullptr) return -1;
        return node->height;
    }
    void updateheight(Node* node){
        if(node != nullptr) node->height = max(getHeight(node->left), getHeight(node->right))+1;
    }
    bool insertHelp(Node *cur, Key k, Value v)
    {
        if (k == cur->key)
            return false;
        else if (k < cur->key)
        {
            if (cur->left == nullptr)
            {
                cur->left = new Node(k, v);
                node_count++;
                return true;
            }
            else
                return insertHelp(cur->left, k, v);
        }
        else
        {
            if (cur->right == nullptr)
            {
                cur->right = new Node(k, v);
                node_count++;
                return true;
            }
            else
                return insertHelp(cur->right, k, v);
        }
    }
    Node *removeHelp(Node *cur, Key k, bool &suc)
    {
        if (cur == nullptr)
            return nullptr;
        else if (k < cur->key)
            cur->left = removeHelp(cur->left, k, suc);
        else if (k > cur->key)
            cur->right = removeHelp(cur->right, k, suc);
        else
        {
            suc = true;
            // 1 . No child
            if (cur->left == nullptr && cur->right == nullptr)
            {
                delete cur;
                return nullptr;
            }
            // 2. one child
            else if (cur->left == nullptr)
            {
                Node *temp = cur->right;
                delete cur;
                return temp;
            }
            else if (cur->right == nullptr)
            {
                Node *temp = cur->left;

                delete cur;
                return temp;
            }
            // 3.both child
            else
            {
                Node *temp = minimumNode(cur->right);
                cur->key = temp->key;
                cur->value = temp->value;
                cur->right = removeHelp(cur->right, temp->key, suc);
            }
        }
        return cur;
    }

    Node *minimumNode(Node *cur) const
    {
        if (cur == nullptr)
            return nullptr;
        while (cur->left != nullptr)
            cur = cur->left;
        return cur;
    }

    bool findHelp(Node *cur, Key k) const
    {
        if (cur == nullptr)
            return false;
        if (cur->key == k)
            return true;
        else if (k < cur->key)
            return findHelp(cur->left, k);
        else
            return findHelp(cur->right, k);
    }

    Value getHelp(Node *cur, Key k) const
    {
        if (cur == nullptr)
            throw std::runtime_error("Key not found");
        if (cur->key == k)
            return cur->value;
        else if (k < cur->key)
            return getHelp(cur->left, k);
        else
            return getHelp(cur->right, k);
    }
    void updateHelp(Node *cur, Key k, Value v)
    {
        if (cur == nullptr)
            throw std::runtime_error("Key not found");
        if (cur->key == k)
            cur->value = v;
        else if (k < cur->key)
            updateHelp(cur->left, k, v);
        else
            updateHelp(cur->right, k, v);
    }

    Key minimumHelp(Node *cur) const
    {
        if (cur == nullptr)
            throw std::runtime_error("Tree is empty");
        else if (cur->left == nullptr)
            return cur->key;
        return minimumHelp(cur->left);
    }
    Key maximumHelp(Node *cur) const
    {
        if (cur == nullptr)
            throw std::runtime_error("Tree is empty");
        else if (cur->right == nullptr)
            return cur->key;
        return maximumHelp(cur->right);
    }
    void clearHelp(Node *cur)
    {
        if (cur == nullptr)
            return;
        clearHelp(cur->left);
        clearHelp(cur->right);
        delete cur;
    }
    void printInorder(Node *cur) const
    {
        if (!cur)
            return;
        printInorder(cur->left);
        std::cout << "(" << cur->key << ":" << cur->value << ") ";
        printInorder(cur->right);
    }

    void printPreorder(Node *cur) const
    {
        if (!cur)
            return;
        std::cout << "(" << cur->key << ":" << cur->value << ") ";
        printPreorder(cur->left);
        printPreorder(cur->right);
    }

    void printPostorder(Node *cur) const
    {
        if (!cur)
            return;
        printPostorder(cur->left);
        printPostorder(cur->right);
        std::cout << "(" << cur->key << ":" << cur->value << ") ";
    }

    void printDefault(Node *node) const
    {
        if (!node)
            return;

        std::cout << "(" << node->key << ":" << node->value;

        // Case: both children are null — print nothing extra
        if (node->left == nullptr && node->right == nullptr)
        {
            std::cout << ")";
            return;
        }

        // Case: only right child — print empty left first
        if (node->left == nullptr && node->right != nullptr)
        {
            std::cout << " () ";
            printDefault(node->right);
        }

        // Case: only left child
        else if (node->left != nullptr && node->right == nullptr)
        {
            std::cout << " ";
            printDefault(node->left);
        }

        // Case: both children
        else
        {
            std::cout << " ";
            printDefault(node->left);
            std::cout << " ";
            printDefault(node->right);
        }

        std::cout << ")";
    }

    // End your private helper functions here

public:
    /**
     * Constructor
     */
    ListBST() : root(nullptr), node_count(0) {}

    /**
     * Destructor
     */
    ~ListBST()
    {

        // TODO: Implement destructor to free memory
        clear();
    }

    /**
     * Insert a key-value pair into the BST
     */
    bool insert(Key key, Value value) override
    {
        // TODO: Implement insertion logic
        if (root == nullptr)
        {
            root = new Node(key, value);
            node_count++;
            return true;
        }
        return insertHelp(root, key, value);
    }

    /**
     * Remove a key-value pair from the BST
     */
    bool remove(Key key) override
    {
        // TODO: Implement removal logic
        bool success = false;
        root = removeHelp(root, key, success);
        if (success)
            node_count--;
        return success;
    }

    /**
     * Find if a key exists in the BST
     */
    bool find(Key key) const override
    {
        // TODO: Implement find logic
        return findHelp(root, key);
    }

    /**
     * Find a value associated with a given key
     */
    Value get(Key key) const override
    {
        // TODO: Implement get logic
        return getHelp(root, key);
    }

    /**
     * Update the value associated with a given key
     */
    void update(Key key, Value value) override
    {
        // TODO: Implement update logic
        updateHelp(root, key, value);
    }

    /**
     * Clear all elements from the BST
     */
    void clear() override
    {
        // TODO: Implement clear logic
        clearHelp(root);
        root = nullptr;
        node_count = 0;
    }

    /**
     * Get the number of keys in the BST
     */
    size_t size() const override
    {
        // TODO: Implement size logic
        return node_count;
    }

    /**
     * Check if the BST is empty
     */
    bool empty() const override
    {
        // TODO: Implement empty check logic
        if (root == nullptr)
            return true;
        else
            return false;
    }

    /**
     * Find the minimum key in the BST
     */
    Key find_min() const override
    {
        // TODO: Implement find_min logic
        return minimumHelp(root);
    }

    /**
     * Find the maximum key in the BST
     */
    Key find_max() const override
    {
        // TODO: Implement find_max logic
        return maximumHelp(root);
    }

    /**
     * Print the BST using specified traversal method
     */
    void print(char traversal_type = 'D') const override
    {
        if (traversal_type == 'D')
        {
            std::cout << "BST (Default): ";
            printDefault(root);
        }
        else if (traversal_type == 'I')
        {
            std::cout << "BST (In-order): ";
            printInorder(root);
        }
        else if (traversal_type == 'P')
        {
            std::cout << "BST (Pre-order): ";
            printPreorder(root);
        }
        else if (traversal_type == 'O')
        {
            std::cout << "BST (Post-order): ";
            printPostorder(root);
        }
        std::cout << std::endl;
    }

    Key secondmax(Node *cur)
    {
        if (cur == nullptr)
            return -1;
        Node *temp;
        while (cur->right != nullptr)
        {
            temp = cur;
            cur = cur->right;
        }
        if (cur->left)
        {
            cur = cur->left;
            while (cur->right != nullptr)
            {
                cur = cur->right; // Go to the rightmost node in the left subtree
            }
            return cur->key;
           
        }
        else
        {
            return temp->key;
        }
    }

    Node *lca(Node *node, int a, int b)
    {
        if (node == nullptr)
            return nullptr;
        if (a > node->key && b > node->key)
            return lca(node->right, a, b);
        else if (a < node->key && b < node->key)
            return lca(node->left, a, b);
        else
            return node;
    }

    int countInrange(Node *node, int start, int end)
    {
        if (node == nullptr)
            return 0;
        if (node->key < start)
            return countInrange(node->right, start, end);
        if (node->key > end)
            return countInrange(node->left, start, end);
        else
            return 1 + countInrange(node->right, start, end) + countInrange(node->left, start, end);
    }


    int index = 0; // Global variable to track in-order index
    int arr[1000]; // Static array to hold the in-order traversal elements (assuming tree size <= 1000)

    void inorder(Node *node)
    {
        if (node == nullptr)
            return;
        inorder(node->left);
        arr[index++] = node->key; // Store the current node key in the array
        inorder(node->right);
    }

    
    int kthSmallest(Node *root, int k)
    {
        index = 0;         // Reset the index before starting the traversal
        inorder(root);     // Perform in-order traversal to fill the array
        return arr[k - 1]; // Return the k-th smallest element (0-indexed, so we use k-1)
    }


    // int find_height(Node *node)
    // {
    //     if (node == nullptr)
    //         return -1;
    //     int left = find_height(node->left);
    //     int right = find_height(node->right);
    //     return max(left, right) + 1;
    // }


    bool isbalanced(Node *root)
    {
        if (root == nullptr)
            return false;
        int left = find_height(root->left);
        int right = find_height(root->right);
        if (abs(left - right) <= 1)
        {
            retrun(isbalanced(root->left) && isbalanced(root->right));
        }
        return false;
    }



    Node *inorderSuccessor(Node *root, Node *n)
    {
        if (n->right)
            return find_min(n->right);
        Node *succ = nullptr;
        while (root)
        {
            if (n->key < root->key)
            {
                succ = root;
                root = root->left;
            }
            else if (n->key > root->key)
            {
                root = root->right;
            }
            else
            {
                break;
            }
        }
        return succ;
    }


    bool isSubTreeGreater(Node* node, int data){
        if(node == nullptr) return false;
        if(node->key >= data && isSubTreeGreater(node->left, data) && isSubTreeGreater(node->right,data)) return true;
        return false;
    }
    bool isSubTreeLesser(Node* node, int data){
        if(node == nullptr) return false;
        if(node->key <= data && isSubTreeLesser(node->left, data) && isSubTreeLesser(node->right,data)) return true;
        return false;
    }
    bool isBST(Node* root){
        if(root == nullptr) return true;
        if(isSubTreeGreater(root->left,root->key) && isSubTreeLesser(root->right,root->key)
        && isBST(root->left) && isBST(root->right)) return true;
        else return false;

    }


    void mirrorBST(Node * node){
        if(node == nullptr) return;
        mirrorBST(node->left);
        mirrorBST(node->right);
        Node* temp = node->left;
        node->left=node->right;
        node->right=temp;
    }


//  bool check_sublist(Node* root, int[] list, int n){
//     if(root == nullptr) return;
//     check_sublist(root->left, list, n);
//     //smtg
//     check_sublist(root->right, list, n);
    
//  }
 int calculate_diameter(Node* node, int& res) const{
    if(node == nullptr) return 0;
    int lheight = calculate_diameter(node->left,res);
    int rheight = calculate_diameter(node->right, res);
    res = res >( lheight+rheight)?  res : (lheight+rheight);
    return ((lheight>rheight)?lheight:rheight)+1;
 }
 int hayre_diameter() const {
    int res =0;
    calculate_diameter(root, res);
    return res;
 }
};

#endif // LISTBST_H