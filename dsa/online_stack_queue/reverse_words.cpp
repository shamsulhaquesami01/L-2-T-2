  #include <iostream>
#include <stack>
using namespace std;
  
  
  
void reverseWords(string &s) {
        
        stack<char>st;
        bool inword = false;
        for(char x: s){
            if(x != ' '){
                inword = true;
            }
            else{
                inword = false;
            }
            if(inword){
                st.push(x);
            }
            else{
                if(!st.empty()){
                    while(!st.empty()){
                        cout<<st.top();
                        st.pop();
                    }
                    cout<<" ";
                }
            }
            
        }
         if(!st.empty()){
                    while(!st.empty()){
                        cout<<st.top();
                        st.pop();
                    }
                    cout<<" ";
                }
        
    }

int main(){

    string a = " ami  banglay gaan gai ";
    reverseWords(a);

}
