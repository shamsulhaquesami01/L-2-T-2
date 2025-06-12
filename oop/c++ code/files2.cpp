#include<iostream>
#include<fstream>
using namespace std;

int main(){
    ifstream in("ff.c");
    ofstream out("text_sami.cpp");
    string s;
    while(getline(in, s)){
        out<<s<<endl;
    }
    return 0;
}