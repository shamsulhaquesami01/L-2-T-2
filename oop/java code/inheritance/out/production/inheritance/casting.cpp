

#include<iostream>
using namespace std;

int main(){
const int num = 10;
int p =1000;
const int* ptr1 = &num;
 ptr1 = &p;
 cout<<*ptr1<<endl;
int* ptr2 = const_cast<int*>(ptr1);
*ptr2 =100;
cout << "Value of num after: " << num << endl;
cout << "*ptr1: " << *ptr1 << endl;
cout << "Value of num before: " << num << endl;
//int val = const_cast<int>(num);
//Student std(3);
// cout << "Roll number before: " << std.getRoll() << endl;
// std.funct();
// cout << "Roll number after: " << std.getRoll() << endl;
// return 0;
}