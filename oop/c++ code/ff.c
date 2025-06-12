#include <stdio.h>
int a = 2;
static int b = 5;
int mult(){
a++; b++;
return a * b;
}
void fun(){
static int count = 0;
count++;
printf("Count: %d\n", count);
}
int main(){
for(int i = 0; i < 5; i++){
fun();
}
printf("Product: %d\n", mult());
printf("Product: %d\n", mult());

return 0;
}