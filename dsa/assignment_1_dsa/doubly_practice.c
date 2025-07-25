#include <stdio.h>
#include <stdlib.h>
typedef struct node
{
    int data;
    struct node *next;
    struct  node *prev;
} node;

void append(node** head, int data) {
    node* newNode = (node*)malloc(sizeof(node));
    newNode->data = data;
    newNode->prev = newNode->next = NULL;

    if (*head == NULL) {
        *head = newNode;
        return;
    }

    node* temp = *head;
    while (temp->next != NULL)
        temp = temp->next;

    temp->next = newNode;
    newNode->prev = temp;
}
void insert_at_index(node** head, int data, int index) {
    node* newNode = (node*)malloc(sizeof(node));
    newNode->data = data;
    newNode->prev = newNode->next = NULL;

    if (index == 0) {
        newNode->next = *head;
        if (*head != NULL)
            (*head)->prev = newNode;
        *head = newNode;
        return;
    }

    node* temp = *head;
    for (int i = 0; i < index - 1 && temp != NULL; i++) {
        temp = temp->next;
    }

    if (temp == NULL) {
        printf("Position out of bounds\n");
        free(newNode);
        return;
    }

    newNode->next = temp->next;
    newNode->prev = temp;

    if (temp->next != NULL)
        temp->next->prev = newNode;
    temp->next = newNode;
}
void delete_at_index(node** head, int index) {
    if (*head == NULL) {
        printf("List is empty\n");
        return;
    }

    node* temp = *head;

    if (index == 0) {
        *head = temp->next;
        if (*head != NULL)
            (*head)->prev = NULL;
        free(temp);
        return;
    }

    for (int i = 0; i < index && temp != NULL; i++) {
        temp = temp->next;
    }

    if (temp == NULL) {
        printf("Position out of bounds\n");
        return;
    }

    if (temp->prev != NULL)
        temp->prev->next = temp->next;
    if (temp->next != NULL)
        temp->next->prev = temp->prev;

    free(temp);
}

struct node *removeDuplicates(struct node *head) {
    struct node *curr = head;

    // Traverse each node in the list
    while (curr != NULL) { 
        struct node *temp = curr;

        // Traverse the remaining nodes to
        // find and remove duplicates
        while (temp->next != NULL) {

            // Check if the next node has the same
            // data as the current node
            if (temp->next->data == curr->data) {
              
                // Duplicate found, remove it
                struct node *duplicate = temp->next;
                temp->next = temp->next->next;

                // Update the previous pointer of the next
                // node, if it exists
                if (temp->next != NULL)
                    temp->next->prev = temp;

                // Free the memory of the duplicate node
                free(duplicate);
            }
            else {
              
              	// If the next node has different data from 
              	// the current node, move to the next node
                temp = temp->next;
            }
        }

        // Move to the next node in the list
        curr = curr->next;
    }
    return head;
}

node *rotate(node *head, int k) {
    if (k == 0 || head == NULL)
        return head;

    // Rotate the list by k nodes
    for (int i = 0; i < k; ++i) {
        node *curr = head;
        while (curr->next != NULL)
            curr = curr->next;
        
        // Move the first node to the last
        curr->next = head;
        curr = curr->next;
        head = head->next;
        curr->next = NULL;
    }
    return head;
}
node* reverse(node* head){
   if(head== NULL) return NULL;


   node* temp = head->prev;
   head->prev = head->next;
   head->next = temp;


   if(head->prev == NULL) return head;
   return reverse(head->prev);
}

 node *reverse( node *head) {
    if (head == NULL || head->next == NULL)
        return head;

     node *prevnode = NULL;
     node *currnode = head;

    // Traverse the list and reverse the links
    while (currnode != NULL) {
      
        // Swap the next and prev pointers
        prevnode = currnode->prev;
        currnode->prev = currnode->next;
        currnode->next = prevnode;

        // Move to the next node in the original list 
      	// (which is now previous due to reversal)
        currnode = currnode->prev;
    }

    // The final node processed will be the new head
    // Fix head pointer
    if (prevnode != NULL)
        head = prevnode->prev;

    return head;
}

int main(){

}