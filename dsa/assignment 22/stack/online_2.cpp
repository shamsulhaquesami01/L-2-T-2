#include "StackLinkedList.h" // Include Array/Linked-List based Stack implementation
#include <cstdio>       // For FILE*, fopen, fscanf, fprintf

// Helper function to print the current stack
void printStack(Stack &stack, FILE *outputFile)
{
    Stack tempStack;
    fprintf(outputFile, "Current stack: ");
    while (!stack.isEmpty())
    {
        int value = stack.pop();
        tempStack.push(value);
    }
    while (!tempStack.isEmpty())
    {
        int value = tempStack.pop();
        stack.push(value);
        fprintf(outputFile, "%d ", value);
    }
    fprintf(outputFile, "\n");
}

void sortedInsert(Stack &stack, int n){
    if(stack.isEmpty() || stack.top()>n){
        stack.push(n);
        return;
    }
    int x = stack.top();
    stack.pop();
    sortedInsert(stack,n);
    stack.push(x);

}

void sortStack(Stack &stack, FILE *outputFile)
{
    //write your code here
    if(stack.isEmpty()){
        return;
    }

    int x = stack.top();
    stack.pop();
    sortStack(stack,outputFile);
    sortedInsert(stack,x);


    printStack(stack, outputFile);
}

// Process each test case and sort the stack
void processStackOperations(Stack &stack, FILE *inputFile, FILE *outputFile)
{
    int testCases;
    fscanf(inputFile, "%d", &testCases);  // Read number of test cases

    for (int t = 1; t <= testCases; ++t)
    {
        fprintf(outputFile, "Test Case %d:\n", t);

        int numElements;
        fscanf(inputFile, "%d", &numElements);  // Read number of elements for this test case

        for (int i = 0; i < numElements; ++i)
        {
            int value;
            fscanf(inputFile, "%d", &value);  // Read each element and push to stack
            stack.push(value);
        }

        // Sort the stack after pushing all elements
        sortStack(stack, outputFile);
        fprintf(outputFile, "-------------------\n");
        stack.clear();  // Clear stack after each test case
    }
}

int main()
{
    FILE *inputFile = fopen("input.txt", "r");
    FILE *outputFile = fopen("output.txt", "w");

    if (inputFile == NULL || outputFile == NULL)
    {
        perror("Error opening input or output file");
        return 1;
    }

    Stack stack;

    fprintf(outputFile, "-------------Testing Stack with Sorting-------------\n");
    processStackOperations(stack, inputFile, outputFile);

    fclose(inputFile);
    fclose(outputFile);

    return 0;
}
