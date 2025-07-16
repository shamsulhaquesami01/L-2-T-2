#include <stdio.h>
#include <stdlib.h>

typedef struct node
{
    int element;
    struct node *next;
    struct node *prev;
} node;

typedef struct {
    node* head;
    node* tail;
    node* cur;
    int size;

    // Iterator support
    struct iterator {
        node* current;

        iterator(node* ptr) : current(ptr) {}
        int& operator*() const { return current->element; }
        iterator& operator++() { current = current->next; return *this; }
        bool operator!=(const iterator& other) const { return current != other.current; }
    };

    iterator begin() const { return iterator(head); }
    iterator end() const { return iterator(nullptr); }

} linkedList;

void init(linkedList *list)
{

    list->head = NULL;
    list->tail = NULL;
    list->cur = NULL;
    list->size = 0;
}

void free_list(linkedList *list)
{

    node *current = list->head;
    while (current != NULL)
    {
        node *next = current->next;
        free(current);
        current = next;
    }

    // Reset all pointers
    list->head = NULL;
    list->tail = NULL;
    list->cur = NULL;
    current = NULL;
    list->size = 0;
}

void print(linkedList *list)
{

    if (list->size == 0)
    {
        printf("[.]\n");
        return;
    }

    printf("[");
    node *temp = list->head;
    while (temp != NULL)
    {
        printf("%d", temp->element);
        if (temp == list->cur)
            printf("|");
        if (temp->next != NULL)
            printf("  ");
        temp = temp->next;
    }
    printf("]\n");
}

void insert(int item, linkedList *list)
{

    node *temp = (node *)malloc(sizeof(node));
    if (!temp)
    {
        printf("Memory allocation failed in insert\n");
        exit(1);
    }
    temp->element = item;
    temp->next = NULL;
    temp->prev = NULL;

    if (list->head == NULL)
    {
        list->head = temp;
        list->tail = temp;
        list->cur = temp;
    }
    else if (list->cur == NULL || list->cur == list->tail)
    {

        temp->prev = list->tail;
        list->tail->next = temp;
        list->tail = temp;
        list->cur = temp;
    }
    else
    {

        temp->next = list->cur->next;
        temp->prev = list->cur;

        if (list->cur->next != NULL)
        {
            list->cur->next->prev = temp;
        }
        list->cur->next = temp;
        list->cur = temp;
    }

    list->size++;
}

void append(int item, linkedList *list)
{

    node *temp = (node *)malloc(sizeof(node));
    if (!temp)
    {
        printf("Memory allocation failed in insert\n");
        exit(1);
    }
    temp->element = item;
    temp->next = NULL;
    temp->prev = NULL;

    if (list->head == NULL)
    {
        list->head = temp;
        list->tail = temp;
        list->cur = temp;
    }
    else
    {

        temp->prev = list->tail;
        list->tail->next = temp;
        list->tail = temp;
    }

    list->size++;
}

int size(linkedList *list)
{

    return list->size;
}

void prev(int n, linkedList *list)
{
    while (n > 0 && list->cur != NULL && list->cur->prev != NULL)
    {
        list->cur = list->cur->prev;
        n--;
    }
}
void next(int n, linkedList *list)
{
    while (n > 0 && list->cur != NULL && list->cur->next != NULL)
    {
        list->cur = list->cur->next;
        n--;
    }
}

void clear(linkedList *list)
{

    node *temp = list->head;
    while (temp != NULL)
    {
        node *cur = temp;
        temp = temp->next;
        free(cur);
    }
    list->head = NULL;
    list->tail = NULL;
    list->cur = NULL;
    list->size = 0;
}
int is_present(int n, linkedList *list)
{
    node *temp = list->head;
    while (temp != NULL)
    {
        if (temp->element == n)
            return 1;
        temp = temp->next;
    }
    return 0;
}

int delete_cur(linkedList *list)
{
    if (list->cur == NULL)
    {
        printf("Cannot delete ,list is empty\n");
        return -1;
    }

    node *temp = list->cur;

    if (temp->prev != NULL)
        temp->prev->next = temp->next;
    else
        list->head = temp->next;

    if (temp->next != NULL)
        temp->next->prev = temp->prev;
    else
        list->tail = temp->prev;

    // cur upadate pdf er example onujayi
    if (temp->next != NULL)
        list->cur = temp->next;
    else
        list->cur = temp->prev;

    list->size--;
    return (temp->element);
}

void delete_item(int item, linkedList *list)
{

    node *temp = list->head;
    while (temp != NULL)
    {
        if (temp->element == item)
            break;
        temp = temp->next;
    }
    if (temp == NULL)
    {
        printf("%d not found\n", item);
        return;
    }
    if (temp->prev != NULL)
        temp->prev->next = temp->next;
    else
        list->head = temp->next; // temp er age kichu nei, head ke delete kora lagbe

    if (temp->next != NULL)
        temp->next->prev = temp->prev;
    else
        list->tail = temp->prev; // temp er pore kichu nei, tail ke delete kora lagbe

    if (temp == list->cur) // current pos e delete korle cur upadte er logic
    {
        if (temp->next != NULL)
            list->cur = temp->next;
        else
            list->cur = temp->prev;
    }
    temp = NULL;
    list->size--;
}

void swap_ind(int ind1, int ind2, linkedList *list)
{
    if (ind1 == ind2)
        return;
    if (ind1 < 0 || ind2 < 0 || ind1 >= list->size || ind2 >= list->size)
    {
        printf("Invalid position\n");
        return;
    }
    node *node1 = NULL, *node2 = NULL;
    node *current = list->head;
    int index = 0;
    while (current != NULL)
    {
        if (index == ind1)
            node1 = current;
        if (index == ind2)
        {
            node2 = current;
        }
        current = current->next;
        index++;
    }

    if (node1 && node2)
    {
        int temp = node1->element;
        node1->element = node2->element;
        node2->element = temp;
    }
}

//helper function
int current_position(linkedList *list){
    int pos=0;
    node *temp = list->head;
    while (temp != list->cur)
    {
        temp = temp->next;
        pos++;
    }
    return pos;
}


int current_item(linkedList* list){
    return list->cur->element;
}