#include <iostream>
#include <fstream>
#include <string>
#include "listBST.hpp"
using namespace std;

int main(int argc, char **argv)
{
    if (argc != 2)
    {
        cerr << "Usage: filename" << "\n";
        return 1;
    }
    ifstream in_file(argv[1]);
    if (!in_file)
    {
        cerr << "Unable to open file\n";
        return 1;
    }

    BST<string, int> *bst1 = new ListBST<string, int>();
    if (!bst1)
    {
        cerr << "Memory allocation failed\n";
        return 2;
    }
    BST<string, int> *bst2 = new ListBST<string, int>();
    if (!bst2)
    {
        cerr << "Memory allocation failed\n";
        delete bst1; // Clean up previously allocated memory
        return 3;
    }

    int n;
    in_file >> n;
    for (int i = 0; i < n; ++i)
    {
        // TODO: Implement the logic to read Phil's words
        // Start your code here
        string s;
        in_file >> s;
        if (bst1->find(s))
        {
            int freq = bst1->get(s);
            bst1->update(s, freq + 1);
        }
        else
        {
            bst1->insert(s, 1);
        }

        // End your code here
    }
    for (int i = 0; i < n; ++i)
    {
        // TODO: Implement the logic to read Claire's words
        // Start your code here
        string s;
        in_file >> s;
        if (bst2->find(s))
        {
            int freq = bst2->get(s);
            bst2->update(s, freq + 1);
        }
        else
        {
            bst2->insert(s, 1);
        }
        // End your code here
    }

    // TODO: Implement the logic to print the initial state of both hands
    // Start your code here
    cout << "Phil's words:" << endl;
    bst1->print('I'); cout<<endl;
    cout << "Claire's words:" << endl;
    bst2->print('I');
    // End your code here
    cout << "\nGame starts!\n\n";
    cout << "==============================\n";

    while (true)
    {
        string word;
        in_file >> word;

        // TODO: Implement the logic to process the game turn and print both hands after each turn
        // Start your code here
        bool a = bst1->find(word);
        bool b = bst2->find(word);
        if (a)
        {
            cout << "Phil has " << word << " !" << endl;
            int value = bst1->get(word);
            if (value == 1)
                bst1->remove(word);
            else
                bst1->update(word, value - 1);
        }
        if (b)
        {
            cout << "Claire has " << word << " !" << endl;
            int value = bst2->get(word);
            if (value == 1)
                bst2->remove(word);
            else
                bst2->update(word, value - 1);
        }
         if (bst1->empty() && bst2->empty())
        {
            cout << "Tie" << endl;
            break;
        }
        else if (bst1->empty())
        {
            cout << "Phil wins!" << endl;
            break;
        }
        else if (bst2->empty())
        {
            cout << "Claire wins!" << endl;
            break;
        }
       
        else
        {
            cout << "\nPhil's remaining words:" << endl;
            bst1->print('I');
            cout << "\nClaire's remaining words:" << endl;
            bst2->print('I');
            cout<<endl;
        }
        // End your code here
        cout << "==============================\n";
    }

    in_file.close();
    delete bst1;
    delete bst2;
    return 0;
}