#ifndef LISTBST_H
#define LISTBST_H

#include "BST.hpp"
#include <iostream>
#include <stdexcept>

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

        Node(Key k, Value v) : key(k), value(v), left(nullptr), right(nullptr) {}
    };

    Node *root;
    size_t node_count;

    // TODO: Implement private helper functions as needed
    // Start your private helper functions here
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
};

#endif // LISTBST_H