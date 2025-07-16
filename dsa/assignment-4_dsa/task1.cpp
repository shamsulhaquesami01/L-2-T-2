#include <iostream>
#include <fstream>
#include "listBST.hpp"

using namespace std;

int main(int argc, char **argv) {
    if (argc != 2) {
        cerr << "Usage: filename" << "\n";
        return 1;
    }
    ifstream in_file(argv[1]);
    if (!in_file) {
        cerr << "Unable to open file\n";
        return 2;
    }
    char c, str[5];
    int val;
    ListBST<int, int> *bst = new ListBST<int, int>();
    if (!bst) {
        cerr << "Memory allocation failed\n";
        return 3;
    }
    while (in_file >> c) {
        // TODO: Implement the logic to read commands from the file and output accordingly
        // After every insertion and removal, print the BST in nested parentheses format
        // Handle exceptions where necessary and print appropriate error messages

        // Start your code here
        if(c=='I'){
            in_file>>val;
            if(bst->insert(val,val)){
                cout<<"Key"<<val<<" inserted into BST,";
            }
            else{
                cout<<"Removal failed! Key"<<val<<" already exists in BST,";
            }
               bst->print();  
               
            
        }
        else if(c=='D'){
             in_file>>val;
            if(bst->remove(val)){
               cout<<"Key"<<val<<" removed from BST,";
            }
            else{
                cout<<"Removal failed! Key"<<val<<" not found in BST,";
            }
              bst->print();  
            
        }
        else if(c=='F'){
             in_file>>val;
           if( bst->find(val)){
            cout<<"key"<<val<<" found in BST.";
           }
           else{
            cout<<"key"<<val<<" not found in BST.";
           }
           cout<<endl;
        }
        else if(c=='M'){
            try{
            in_file>>str;
            if(str[1]=='i') cout<<"Minimum value: "<<bst->find_min()<<endl;
            else cout<<"Maximum value: "<<bst->find_max()<<endl;
            }catch(const exception &e){
                cout<<e.what()<<endl;
            }
        }
        else if(c=='E'){
            if(bst->empty()) cout<<"Empty";
            else cout<<"Not- Empty";
            cout<<endl;
            bst.
        }
        else if(c=='S'){
           cout<<"Size: "<<bst->size()<<endl;
        }
        else if(c=='T'){
            in_file>>str;
            if(str[0]== 'I') bst->print(str[0]);
            else if (str[0]=='P' && str[1]== 'r') bst->print(str[0]);
            else bst->print('O');
        }
        
      cout<<bst->hayre_diameter()<<endl;

    }
    in_file.close();
    delete bst;
    return 0;
}
