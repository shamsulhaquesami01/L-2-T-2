#ifndef ADJACENCY_LIST_GRAPH_H
#define ADJACENCY_LIST_GRAPH_H

#include "GraphADT.h"
#include "queue.h"
#include <iostream>
using namespace std;

class AdjacencyListGraph : public GraphADT
{
private:
    linkedList *adjlist;
    bool *exists;
    int capacity;
    int highest;

    void check_cap(int v)
    {
        if (v >= capacity)
        {
            int newCapacity = max(v + 1, capacity * 2);
            linkedList *new_adj = new linkedList[newCapacity];
            bool *newExists = new bool[newCapacity];

            for (int i = 0; i < newCapacity; ++i)
            {
                if (i < capacity)
                {
                    new_adj[i] = adjlist[i];
                    newExists[i] = exists[i];
                }
                else
                {
                    init(&new_adj[i]);
                    newExists[i] = false;
                }
            }

            delete[] adjlist;
            delete[] exists;
            adjlist = new_adj;
            exists = newExists;
            capacity = newCapacity;
        }
    }
    void resize_down()
    {
        int newCapacity = max(capacity / 2, 10);
        linkedList *new_adj = new linkedList[newCapacity];
        bool *newExists = new bool[newCapacity];

        for (int i = 0; i < newCapacity; ++i)
        {
            if (i < capacity)
            {
                new_adj[i] = adjlist[i];
                newExists[i] = exists[i];
            }
            else
            {
                init(&new_adj[i]);
                newExists[i] = false;
            }
        }

        delete[] adjlist;
        delete[] exists;
        adjlist = new_adj;
        exists = newExists;
        capacity = newCapacity;
    }
    int DFSUtil(int u, bool *visited) const
    {
        int count = 0;
        cout << u << " ";
        visited[u] = true;
        node *temp = adjlist[u].head;
        while (temp)
        {
            int neighbor = temp->element;
            if (!visited[neighbor])
            {

                DFSUtil(neighbor, visited);
                count++;
            }
            temp = temp->next;
        }
        return count;
    }

public:
    AdjacencyListGraph(int initialCap = 10)
    {
        capacity = initialCap;
        adjlist = new linkedList[capacity];
        exists = new bool[capacity];
        highest = 0;
        for (int i = 0; i < capacity; ++i)
        {
            init(&adjlist[i]);
            exists[i] = false;
        }
    }

    ~AdjacencyListGraph()
    {
        for (int i = 0; i < capacity; ++i)
        {
            free_list(&adjlist[i]);
        }
        delete[] adjlist;
        delete[] exists;
    }

    void AddNode(int v) override
    {
        check_cap(v);
        if (!exists[v])
        {
            exists[v] = true;
        }
    }

    void AddEdge(int u, int v) override
    {
        check_cap(max(u, v)); // highest index porjonto
        AddNode(u);
        AddNode(v);
        if (!is_present(v, &adjlist[u]))
            append(v, &adjlist[u]);
        if (!is_present(u, &adjlist[v]))
            append(u, &adjlist[v]);
    }

    bool CheckEdge(int u, int v) const override
    {
        if (u >= capacity || v >= capacity || !exists[u] || !exists[v])
            return false;
        return is_present(v, &adjlist[u]) && is_present(u, &adjlist[v]);
    }

    void RemoveNode(int v) override
    {
        if (v >= capacity || !exists[v])
        {
            cout << "Node " << v << " doesnt exist" << endl;
            return;
        }

        for (int i = 0; i < capacity; ++i)
        {
            if (exists[i] && is_present(v, &adjlist[i]))
            {
                delete_item(v, &adjlist[i]);
            }
        }
        clear(&adjlist[v]);
        exists[v] = false;
        for (int i = 0; i < capacity; ++i)
        {
            if (exists[i])
                highest = i;
        }

        if (highest < capacity / 4)
        {
            resize_down();
        }
    }

    void RemoveEdge(int u, int v) override
    {
        if (u >= capacity || v >= capacity)
        {
            cout << "Node " << " doesnt exist" << endl;
            return;
        }
        if (CheckEdge(u, v))
        {
            delete_item(v, &adjlist[u]);
            delete_item(u, &adjlist[v]);
        }
        else
        {

            cout << "Edge doesnt exist" << endl;
            return;
        }
    }

    bool CheckPath(int u, int v) const override
    {
        if (u >= capacity || v >= capacity || !exists[u] || !exists[v])
            return false;

        bool *visited = new bool[capacity]();
        ListQueue q;
        q.enqueue(u);
        visited[u] = true;

        while (!q.empty())
        {
            int curr = q.dequeue();
            if (curr == v)
            {
                delete[] visited;
                return true;
            }
            node *temp = adjlist[curr].head;
            while (temp)
            {
                int neighbor = temp->element;
                if (!visited[neighbor])
                {
                    visited[neighbor] = true;
                    q.enqueue(neighbor);
                }
                temp = temp->next;
            }
        }
        delete[] visited;
        return false;
    }

    void FindShortestPath(int u, int v) const override
    {
        if (u >= capacity || v >= capacity || !exists[u] || !exists[v])
        {
            cout << "No Such Node, Shortest Path Doesnt exist" << endl;
            return;
        }

        int *prev = new int[capacity];
        bool *visited = new bool[capacity]();
        for (int i = 0; i < capacity; ++i)
            prev[i] = -1;

        ListQueue q;
        q.enqueue(u);
        visited[u] = true;

        while (!q.empty())
        {
            int curr = q.dequeue();
            if (curr == v)
                break;
            node *temp = adjlist[curr].head;
            while (temp)
            {
                int neighbor = temp->element;
                if (!visited[neighbor])
                {
                    visited[neighbor] = true;
                    prev[neighbor] = curr;
                    q.enqueue(neighbor);
                }
                temp = temp->next;
            }
        }

        cout << "Shortest path: ";
        if (!visited[v])
        {
            cout << "Path Doesnt Exist between the nodes" << endl;
        }
        else
        {
            int path[capacity];
            int len = 0;
            for (int at = v; at != -1; at = prev[at])
            {
                path[len++] = at;
            }
            for (int i = len - 1; i >= 0; --i)
            {
                cout << path[i] << " ";
            }
            cout << endl;
        }
        delete[] prev;
        delete[] visited;
    }

    int FindShortestPathLength(int u, int v) const override
    {
        if (u >= capacity || v >= capacity || !exists[u] || !exists[v])
        {
            cout << "No Such Node, Shortest Path Doesnt exist" << endl;
            return -1;
        }
        bool *visited = new bool[capacity]();
        int *dist = new int[capacity];
        for (int i = 0; i < capacity; ++i)
            dist[i] = -1;

        ListQueue q;
        q.enqueue(u);
        visited[u] = true;
        dist[u] = 0;

        while (!q.empty())
        {
            int curr = q.dequeue();
            node *temp = adjlist[curr].head;
            while (temp)
            {
                int neighbor = temp->element;
                if (!visited[neighbor])
                {
                    visited[neighbor] = true;
                    dist[neighbor] = dist[curr] + 1;
                    q.enqueue(neighbor);
                }
                temp = temp->next;
            }
        }
        int res = dist[v];
        delete[] visited;
        delete[] dist;
        return res;
    }

    linkedList GetNeighbors(int u) const override
    {
        if (u >= capacity || !exists[u])
        {
            linkedList empty;
            init(&empty);
            return empty;
        }
        return adjlist[u];
    }
    // in main
    char array[5][5];
    char arr[26];
    for (int i = 0; i < 5; i++){
        for (int j = 0; j < 5; j++)
        {
            char[i * 5 + j] = char[i][j]; // 1d
        }
    }
    int MaZe(int arr[], int n)
    {
        for (int i = 0; i < n; i++)
            AddNode(i);
        for (int i = 0; i < n-1 ; i++) {
            if (arr[i] != '#' || arr[i + 1] != '#' ) AddEdge(i,i+1);
               
        }
          for (int i = 0; i < n-5 ; i++)
        {
            if (arr[i] != '#' || arr[i + 5] != '#' ) AddEdge(i,i+5);    
        }
        int start;
        int count = 0;
        for (int i = 0; i < n; i++)
        {
            if (arr[i] == 'S')
                start = i;
        }
        for (int i = 0; i < n; i++)
        {
            int end;
            if (arr[i] == 'E')
                end = i;
            if (CheckPath(start, end))
                count++:
        }
        return count;
    }
};

#endif // ADJACENCY_LIST_GRAPH_H