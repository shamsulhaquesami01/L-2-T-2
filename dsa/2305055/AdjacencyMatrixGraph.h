#ifndef ADJACENCY_MATRIX_GRAPH_H
#define ADJACENCY_MATRIX_GRAPH_H

#include "GraphADT.h"
#include "queue.h"
#include <iostream>
using namespace std;

class AdjacencyMatrixGraph : public GraphADT
{
private:
    bool **matrix;
    bool *exists;
    int capacity;
    int highest;

    void resizeMatrix()
    {
        int newCapacity = capacity * 2;
        bool **newMatrix = new bool *[newCapacity];
        for (int i = 0; i < newCapacity; ++i)
        {
            newMatrix[i] = new bool[newCapacity];
            for (int j = 0; j < newCapacity; ++j)
            {
                if (i < capacity && j < capacity)
                    newMatrix[i][j] = matrix[i][j];
                else
                    newMatrix[i][j] = false;
            }
        }

        bool *newExists = new bool[newCapacity];
        for (int i = 0; i < newCapacity; ++i)
        {
            if (i < capacity)
                newExists[i] = exists[i];
            else
                newExists[i] = false;
        }

        for (int i = 0; i < capacity; ++i)
            delete[] matrix[i];
        delete[] matrix;
        delete[] exists;

        matrix = newMatrix;
        exists = newExists;
        capacity = newCapacity;
    }

    void resize_down()
    {
        int newCapacity = max(capacity / 2, 10);
        bool **newMatrix = new bool *[newCapacity];
        for (int i = 0; i < newCapacity; ++i)
        {
            newMatrix[i] = new bool[newCapacity];
            for (int j = 0; j < newCapacity; ++j)
            {
                if (i < capacity && j < capacity)
                    newMatrix[i][j] = matrix[i][j];
                else
                    newMatrix[i][j] = false;
            }
        }

        bool *newExists = new bool[newCapacity];
        for (int i = 0; i < newCapacity; ++i)
        {
            if (i < capacity)
                newExists[i] = exists[i];
            else
                newExists[i] = false;
        }

        for (int i = 0; i < capacity; ++i)
            delete[] matrix[i];
        delete[] matrix;
        delete[] exists;

        matrix = newMatrix;
        exists = newExists;
        capacity = newCapacity;
    }

    void check_cap(int v)
    {
        while (v >= capacity)
            resizeMatrix();
    }

public:
    AdjacencyMatrixGraph(int initialCap = 10)
    {
        capacity = initialCap;
        matrix = new bool *[capacity];
        highest = 0;
        for (int i = 0; i < capacity; ++i)
        {
            matrix[i] = new bool[capacity];
            for (int j = 0; j < capacity; ++j)
                matrix[i][j] = false;
        }
        exists = new bool[capacity];
        for (int i = 0; i < capacity; ++i)
            exists[i] = false;
    }

    ~AdjacencyMatrixGraph()
    {
        for (int i = 0; i < capacity; ++i)
            delete[] matrix[i];
        delete[] matrix;
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
        check_cap(max(u, v));
        AddNode(u);
        AddNode(v);
        matrix[u][v] = true;
        matrix[v][u] = true;
    }

    bool CheckEdge(int u, int v) const override
    {
        if (u >= capacity || v >= capacity || !exists[u] || !exists[v])
            return false;
        return matrix[u][v];
    }

    void RemoveNode(int v) override
    {
        if (v >= capacity || !exists[v])
            return;
        for (int i = 0; i < capacity; ++i)
        {
            matrix[v][i] = false;
            matrix[i][v] = false;
        }
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
            return;
        matrix[u][v] = false;
        matrix[v][u] = false;
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
            for (int i = 0; i < capacity; ++i)
            {
                if (matrix[curr][i] && !visited[i] && exists[i])
                {
                    visited[i] = true;
                    q.enqueue(i);
                }
            }
        }
        delete[] visited;
        return false;
    }

    void FindShortestPath(int u, int v) const override
    {
        if (u >= capacity || v >= capacity || !exists[u] || !exists[v])
        {
            cout << "Shortest path: " << endl;
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
            for (int i = 0; i < capacity; ++i)
            {
                if (matrix[curr][i] && !visited[i] && exists[i])
                {
                    visited[i] = true;
                    prev[i] = curr;
                    q.enqueue(i);
                }
            }
        }

        cout << "Shortest path: ";
        if (!visited[v])
        {
            cout << endl;
        }
        else
        {
            int path[capacity];
            int len = 0;
            for (int at = v; at != -1; at = prev[at])
                path[len++] = at;
            for (int i = len - 1; i >= 0; --i)
                cout << path[i] << " ";
            cout << endl;
        }
        delete[] prev;
        delete[] visited;
    }

    int FindShortestPathLength(int u, int v) const override
    {
        if (u >= capacity || v >= capacity || !exists[u] || !exists[v])
            return -1;

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
            for (int i = 0; i < capacity; ++i)
            {
                if (matrix[curr][i] && !visited[i] && exists[i])
                {
                    visited[i] = true;
                    dist[i] = dist[curr] + 1;
                    q.enqueue(i);
                }
            }
        }
        int res = dist[v];
        delete[] visited;
        delete[] dist;
        return res;
    }

    linkedList GetNeighbors(int u) const override
    {
        linkedList neighbors;
        init(&neighbors);
        if (u >= capacity || !exists[u])
            return neighbors;

        for (int i = 0; i < capacity; ++i)
        {
            if (matrix[u][i] && exists[i])
                append(i, &neighbors);
        }
        return neighbors;
    }

};

#endif // ADJACENCY_MATRIX_GRAPH_H
