//#include "arrayList.h"
 #include "linkedList.h"
#include <stdio.h>


int main()
{
    const char* input_filename= "books.txt";
    FILE *file = fopen(input_filename, "r");
    if (file == NULL)
    {
        printf("Error opening file\n");
        return 1;
    }
    
    int number_of_books;
    const int CAPACITY = 10;
    fscanf(file, "%d", &number_of_books);
    printf("number_of_books: %d\n", number_of_books);

    //arrayList books;
    
     linkedList books;
     init(&books);
    // init_linkedlist(&books);
    int i;
    for (i=0; i<number_of_books; i++)
    {
        int book_id;
        fscanf(file, "%d", &book_id);
        append(book_id, &books);
    }

   
    print(&books);

    printf("\n");



    int func, param,param2;
    int a =1;
    while (a--)
    {
        fscanf(file, "%d %d %d", &func, &param, &param2);
        if (func == 1)
        {
            //skip(&books);
            printf("Command: skip\n");
            prev(1, &books);
            // use printf here
        }
        else if (func == 2)
        {
            printf("Command: swap\n");
            //swap_with(&books, param);
            swap_ind(current_position(&books), param, &books);
            // use printf here
        }
        else if (func == 3)
        {
            //discard(&books);
            printf("Command: delete_cur\n");
            if(current_position(&books) == size(&books)-1){
                delete_cur(&books);
            }
            else{
                delete_cur(&books);
                prev(1, &books);
            }
            
        }
        else{
            printf("Command: reversing a part\n");
            int a = param2-param+1;
            int arr[a];
            next(param, &books);
            for(int i=0; i<a; i++){
                arr[i]= delete_cur(&books);
            }
            prev(1, &books);
           
            for(int i=a-1; i>=0; i--){
                insert(arr[i], &books);
            }
        }
        print(&books);
    }

    printf("%d.  %d.   \n", current_position(&books), current_item(&books));
    free_list(&books);
    fclose(file);
    return 0;
}