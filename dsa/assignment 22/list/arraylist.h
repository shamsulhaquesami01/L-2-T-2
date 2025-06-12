#include <stdio.h>
#include <stdlib.h>

typedef struct
{
    int * array;
    int size;
    int capacity;
    int current_pos;
    
} arrayList;


void init_arraylist(arrayList *list, int capacity)
{

    printf("implement init_arraylist\n");
    list->array = (int*)malloc(capacity*sizeof(int));
    list->capacity = capacity;
    list->size=0;
    list->current_pos=0;
    
    // dynamically allocate space for the array
    // initialize the size, capacity, and current position
}


void clear(arrayList *list)
{
    printf("The List is cleared \n");
    list->size=0;
    list->current_pos=0;
    // clear the list but do not free the array
    // modify the size, capacity, and current position
}

int get_size(arrayList *list)
{
    printf("Size of the list is : %d\n", list->size);
    return list->size;
}

void reverse(arrayList *list)
{
    printf("Reversed List :\n");
    for(int i=0; i<list->size/2; i++) {
        int temp = list->array[i];
        list->array[i]= list->array[list->size-1-i];
        list->array[list->size-1-i]=temp;
    }
}

void resize(arrayList *list, int new_capacity)
{
    int *new_array = (int *)malloc(new_capacity * sizeof(int));
    for (int i = 0; i < list->size; i++) {
        new_array[i] = list->array[i];
    }
    free(list->array);
    list->array = new_array;
    printf("Resized array from %d to %d\n", list->capacity, new_capacity);
    list->capacity = new_capacity;
   
}


void append(arrayList *list, int value)
{
    printf("%d is appended to the list\n", value);
    if (list->size >= list->capacity){
        resize(list, list->capacity * 2); // Resize if necessary
    }
    
    list->array[list->size] = value;
    list->size++;
    // call resize if necessary
    // add value to the end of the list
}


void insert(arrayList *list, int value)
{
    printf("Inserted %d at position %d \n", value, list->current_pos);
    if (list->size >= list->capacity){
        resize(list, list->capacity * 2); // Resize if necessary
    }
    for(int i=list->size-1; i>=list->current_pos; i--){
        list->array[i+1] = list->array[i];
    }
    list->array[list->current_pos] =value;
    list->size++;
    // call resize if necessary
    // shift the elements to the right to make space
    // add value at the current position
}


int remove_at_current(arrayList *list)
{
    printf("Removed %d from %d position\n", list->array[list->current_pos], list->current_pos);
    if (list->size >= list->capacity){
        resize(list, list->capacity * 2); // Resize if necessary
    }
    int delete = list->current_pos;
    list->size--;
    for(int i=list->current_pos; i<list->size; i++){
        list->array[i] = list->array[i+1];
    }
    if(list->current_pos == list->size) list->current_pos --;
    return delete;
    // save the value of the current element in a variable
    // shift the elements to the left to fill the gap
    // change the size, and current position as necessary
    // call resize if necessary
    // return the saved value
}

int find(arrayList *list, int value)
{
    int i;
    
    for(i=0; i<list->size; i++){
        if(list->array[i] == value) {
            printf("Found %d at position %d\n", value,i);
            return i;
        }
    }
   
    printf("Element %d Not Found\n", value);
    return -1;
    
    // traverse the list and return the position of the value
    // return -1 if the value is not found
}


void move_to_start(arrayList *list)
{
   
    list->current_pos=0;
    printf("Moved current position to the start: 0\n");
    // consider the cases when the list is empty
}


void move_to_end(arrayList *list)
{
    printf("Implement move_to_end\n");
    if(list->size==0) list->current_pos =0;
    else list->current_pos = list->size-1;
    printf("Moved current position to the end: %d\n", list->size-1);
    // consider the cases when the list is empty
}


void prev(arrayList *list)
{
   
    if(list->current_pos==0) return;
    else list->current_pos--;
    printf("Moved to previous position\n");
    // no change if the current position is at the start
}


void next(arrayList *list)
{
  
    if(list->current_pos==list->size-1) return;
    else list->current_pos++;
    printf("Moved to next position\n");
    // no change if the current position is at the end
}


void move_to_position(arrayList *list, int position)
{
    printf("Moved Current Position to %d\n", position);
    list->current_pos =position;
    
}


int get_current_position(arrayList *list)
{
    printf("Current position is %d\n", list->current_pos);
    return list->current_pos;
}


int get_current_element(arrayList *list)
{
    printf("Current element is %d \n", list->array[list->current_pos]);
    return list->array[list->current_pos];
}


void print_list(arrayList *list)
{
    printf("< ");
    int i;
    for(i=0; i<list->size; i++) {
        if(i==list->current_pos)printf("|");
        printf("%d  ", list->array[i]);
    }
    printf("\b");
    printf(">\n");

}


void free_list(arrayList *list)
{
    printf("Implement free_list\n");
    free(list->array);
    // free the array before terminating the program
}


