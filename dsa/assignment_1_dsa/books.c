//#include "arrayList.h"
 #include "linkedList.h"
#include <stdio.h>

void reverse(linkedList *books){
     int a = size(books);
     int arr[a];
     for(int i=0; i<a; i++){
        arr[i] = delete_cur(books);
     }
     for(int i=a-1; i>=0; i--){
        append(arr[i], books);
     }
}

void skip(){

}
void discard(){
    
}
void swap(int a, linkedList *books){
   swap_ind(current_position(books), a, books);
}

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
    //const int CAPACITY = 10;
    fscanf(file, "%d", &number_of_books);

    //arrayList books;
    
    linkedList books;
    init(&books);
    // init_linkedbooks(&books);
    int i;
    for (i=0; i<number_of_books; i++)
    {
        int book_id;
        fscanf(file, "%d", &book_id);
        append( book_id, &books);
    }

    print(&books);
    printf("\n");
    
    // implement this function
    //reverse(&books);
    reverse(&books);
    printf("Reveresed books: ");
    print(&books);
    // next(10, &books);
   
    // printf("%d.  %d.   \n", current_position(&books), current_item(&books));
    
    free_list(&books);
    fclose(file);
    return 0;
}