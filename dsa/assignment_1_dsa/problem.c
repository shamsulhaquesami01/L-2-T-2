#include <stdio.h>
#include "arrayList.h"
 //#include "linkedList.h"

int main()
{
    FILE *file = fopen("in_prob.txt", "r");
    if (file == NULL)
    {
        return 1;
    }

    arrayList dal, fl;
     //linkedList dal, fl;
    init(&dal);
    init(&fl);

    int func, param;
    while (fscanf(file, "%d", &func) == 1 && func != 0)
    {
        if (func == 1 && fscanf(file, "%d", &param) == 1)
        {
            if (is_present(param, &fl))
            {
                printf("Recruit %d\n", param);
                printf("In the Foe list, cannot recruit \n");
            }
            else if (is_present(param, &dal))
            {
                printf("Already recruited\n");
            }
            else
            {
                printf("Recruit %d\n", param);
                append(param, &dal);
                printf("DA List: \n");
                print(&dal);
            }
        }
        else if (func == 2 && fscanf(file, "%d", &param) == 1)
        {
            if (!is_present(param, &dal))
            {
                printf("%d not in DA list\n", param);
            }
            else
            {
                printf("Fire %d\n", param);
                delete_item(param, &dal);
                printf("DA List: \n");
                print(&dal);
                printf("\n");
                append(param, &fl);
                printf("Foe List: \n");
                print(&fl);
            }
        }
        else if (func == 3 && fscanf(file, "%d", &param) == 1)
        {
            printf("Check %d\n", param);
            if (is_present(param, &fl))
            {
                printf("Foe\n");
            }
            else if (is_present(param, &dal))
            {
                printf("Friend\n");
            }
            else
            {
                printf("Unknown\n");
            }
        }
        else
        {
            clear(&dal);
            clear(&fl);
            free_list(&dal);
            free_list(&fl);
            break;
        }

        printf("\n");
    }

    return 0;
}