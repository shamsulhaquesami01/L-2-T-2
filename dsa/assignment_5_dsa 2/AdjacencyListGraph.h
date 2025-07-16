#ifndef ADJACENCY_LIST_GRAPH_H
#define ADJACENCY_LIST_GRAPH_H

#include "GraphADT.h"
#include "queue.h"
#include <iostream>
using namespace std;

class AdjacencyListGraph : public GraphADT {
private:
    linkedList* adjLists;      
    bool* exists;             
    int capacity;             

    void check_cap(int v) {
        if (v >= capacity) {
            int newCapacity = max(v + 1, capacity * 2);
            linkedList* newAdjLists = new linkedList[newCapacity];
            bool* newExists = new bool[newCapacity];

            for (int i = 0; i < newCapacity; ++i) {
                if (i < capacity) {
                    newAdjLists[i] = adjLists[i];
                    newExists[i] = exists[i];
                } else {
                    init(&newAdjLists[i]);
                    newExists[i] = false;
                }
            }

            delete[] adjLists;
            delete[] exists;
            adjLists = newAdjLists;
            exists = newExists;
            capacity = newCapacity;
        }
    }

public:
    AdjacencyListGraph(int initialCap = 100) {
        capacity = initialCap;
        adjLists = new linkedList[capacity];
        exists = new bool[capacity];
        for (int i = 0; i < capacity; ++i) {
            init(&adjLists[i]);
            exists[i] = false;
        }
    }

    ~AdjacencyListGraph() {
        for (int i = 0; i < capacity; ++i) {
            free_list(&adjLists[i]);
        }
        delete[] adjLists;
        delete[] exists;
    }

    void AddNode(int v) override {
        check_cap(v);
        exists[v] = true;
    }

    void AddEdge(int u, int v) override {
        check_cap(max(u, v));
        AddNode(u);
        AddNode(v);
        if (!is_present(v, &adjLists[u])) append(v, &adjLists[u]);
        if (!is_present(u, &adjLists[v])) append(u, &adjLists[v]);
    }

    bool CheckEdge(int u, int v) const override {
        if (u >= capacity || v >= capacity || !exists[u] || !exists[v]) return false;
        return is_present(v, &adjLists[u]);
    }

void RemoveNode(int v) override {
    if (v >= capacity || !exists[v]) return;

    for (int i = 0; i < capacity; ++i) {
        if (exists[i] && is_present(v, &adjLists[i])) {
            delete_item(v, &adjLists[i]);
        }
    }
    clear(&adjLists[v]);
    exists[v] = false;
}


    void RemoveEdge(int u, int v) override {
        if (u >= capacity || v >= capacity) return;
        delete_item(v, &adjLists[u]);
        delete_item(u, &adjLists[v]);
    }

    bool CheckPath(int u, int v) const override {
        if (u >= capacity || v >= capacity || !exists[u] || !exists[v]) return false;

        bool* visited = new bool[capacity]();
        ListQueue q;
        q.enqueue(u);
        visited[u] = true;

        while (!q.empty()) {
            int curr = q.dequeue();
            if (curr == v) {
                delete[] visited;
                return true;
            }
            node* temp = adjLists[curr].head;
            while (temp) {
                int neighbor = temp->element;
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    q.enqueue(neighbor);
                }
                temp = temp->next;
            }
        }
        delete[] visited;
        return false;
    }

    void FindShortestPath(int u, int v) const override {
        if (u >= capacity || v >= capacity || !exists[u] || !exists[v]) {
            cout << "Shortest path: " << endl;
            return;
        }

        int* prev = new int[capacity];
        bool* visited = new bool[capacity]();
        for (int i = 0; i < capacity; ++i) prev[i] = -1;

        ListQueue q;
        q.enqueue(u);
        visited[u] = true;

        while (!q.empty()) {
            int curr = q.dequeue();
            if (curr == v) break;
            node* temp = adjLists[curr].head;
            while (temp) {
                int neighbor = temp->element;
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    prev[neighbor] = curr;
                    q.enqueue(neighbor);
                }
                temp = temp->next;
            }
        }

        cout << "Shortest path: ";
        if (!visited[v]) {
            cout << endl;
        } else {
            int path[capacity];
            int len = 0;
            for (int at = v; at != -1; at = prev[at]) {
                path[len++] = at;
            }
            for (int i = len - 1; i >= 0; --i) {
                cout << path[i] << " ";
            }
            cout << endl;
        }
        delete[] prev;
        delete[] visited;
    }

    int FindShortestPathLength(int u, int v) const override {
        if (u >= capacity || v >= capacity || !exists[u] || !exists[v]) return -1;

        bool* visited = new bool[capacity]();
        int* dist = new int[capacity];
        for (int i = 0; i < capacity; ++i) dist[i] = -1;

        ListQueue q;
        q.enqueue(u);
        visited[u] = true;
        dist[u] = 0;

        while (!q.empty()) {
            int curr = q.dequeue();
            node* temp = adjLists[curr].head;
            while (temp) {
                int neighbor = temp->element;
                if (!visited[neighbor]) {
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

    linkedList GetNeighbors(int u) const override {
        if (u >= capacity || !exists[u]) {
            linkedList empty;
            init(&empty);
            return empty;
        }
        return adjLists[u];
    }
};

#endif // ADJACENCY_LIST_GRAPH_H