#include <stdio.h>
#include <stdlib.h>

typedef struct
{
    int *array;
    int size;
    int capacity;
    int current_pos;

} arrayList;

void init(arrayList *list)
{
    list->array = (int *)malloc(2 * sizeof(int));
    list->capacity = 2;
    list->size = 0;
    list->current_pos = 0;
}

void free_list(arrayList *list)
{
    free(list->array);
    list->array= NULL;
}

void increase_capacity(arrayList *list)
{

    int *new_array = (int *)malloc(list->capacity * 2 * sizeof(int));
    if (new_array == NULL) {
        printf("Memory allocation failed in increase_capacity.\n");
        exit(1);
    }
    for (int i = 0; i < list->size; i++)
    {
        new_array[i] = list->array[i];
    }
    free(list->array);
    list->array = new_array;
    printf("Capacity increased from %d to %d\n", list->capacity, list->capacity * 2);
    list->capacity = list->capacity * 2;
}

void decrease_capacity(arrayList *list)
{

    int *new_array = (int *)malloc((list->capacity / 2) * sizeof(int));
    if (new_array == NULL) {
        printf("Memory allocation failed in increase_capacity.\n");
        exit(1);
    }
    for (int i = 0; i < list->size; i++)
    {
        new_array[i] = list->array[i];
    }
    free(list->array);
    list->array = new_array;
    printf("Capacity decreased from %d to %d\n", list->capacity, list->capacity / 2);
    list->capacity = list->capacity / 2;
}

void print(arrayList *list)
{
    if (list->size == 0)
    {
        printf("[.]\n");
        return;
    }

    printf("[");
    for (int i = 0; i < list->size; i++)
    {
        printf("%d", list->array[i]);
        if (i == list->current_pos)
            printf("|");
        if (i != list->size - 1)
            printf("  "); 
    }
    printf("]\n");
}

void insert(int item, arrayList *list)
{

    if (list->size >= list->capacity / 2)
    {
        increase_capacity(list);
    }
    if (list->size == 0)
    {
        list->array[list->current_pos] = item;
        list->size++;
        return;
    }
    if (list->current_pos == list->size - 1)
    {
        list->array[++list->current_pos] = item;
        list->size++;
        return;
    }
    for (int i = list->size - 1; i >= list->current_pos + 1; i--)
    {
        list->array[i + 1] = list->array[i];
    }
    list->array[list->current_pos + 1] = item;
    list->size++;
    list->current_pos++;
}

int delete_cur(arrayList *list)
{
    if (list->size == 0) {
        printf("Cannot delete ,list is empty\n");
        return -1;  
    }

    int delete = list->array[list->current_pos];
    list->size--;
    for (int i = list->current_pos; i < list->size; i++)
    {
        list->array[i] = list->array[i + 1];
    }
    if (list->current_pos >= list->size)
        list->current_pos--;
    if (list->size < list->capacity / 4)
        decrease_capacity(list);
    if (list->size == 0)
        list->current_pos = 0;
    return delete;
}

void append(int item, arrayList *list)
{

    if (list->size >= list->capacity / 2)
    {
        increase_capacity(list);
    }

    list->array[list->size] = item;
    list->size++;
}

int size(arrayList *list)
{

    return list->size;
}

void prev(int n, arrayList *list)
{

    if (list->current_pos == 0){
        printf("Already at the beginning\n");
        return;
    }
    list->current_pos -= n;
    if (list->current_pos < 0)
        list->current_pos = 0;
}

void next(int n, arrayList *list)
{

    if (list->current_pos == list->size - 1){
        printf("Already at the end \n");
        return;
    }
    list->current_pos += n;
    if (list->current_pos >= list->size)
        list->current_pos = list->size - 1;
}

int is_present(int n, arrayList *list)
{

    int flag = 0;
    for (int i = 0; i < list->size; i++)
    {
        if (list->array[i] == n)
            flag = 1;
    }
    return flag;
}

void clear(arrayList *list)
{

    printf("The List is cleared \n");
    list->size = 0;
    list->current_pos = 0;
}

void delete_item(int item, arrayList *list)
{

    int index = -1;
    for (int i = 0; i < list->size; i++)
    {
        if (list->array[i] == item)
        {
            index = i;
            break;
        }
    }

    if (index == -1)
    {
        printf("%d not found\n", item);
        return;
    }
    list->size--;
    for (int i = index; i < list->size; i++)
    {
        list->array[i] = list->array[i + 1];
    }
    if (list->current_pos >= list->size)
        list->current_pos--;
    if (list->size < list->capacity / 4)
        decrease_capacity(list);
    if (list->size == 0)
        list->current_pos = 0;
}

void swap_ind(int ind1, int ind2, arrayList *list)
{
    if (ind1 < 0 || ind2 < 0 || ind1 >= list->size || ind2 >= list->size) {
        printf("Invalid position \n");
        return;
    }
    int temp = list->array[ind1];
    list->array[ind1] = list->array[ind2];
    list->array[ind2] = temp;
}
//helper function
int current_position(arrayList *list){
    return list->current_pos;
}

int current_item(arrayList* list){
    return list->array[list->current_pos];
}