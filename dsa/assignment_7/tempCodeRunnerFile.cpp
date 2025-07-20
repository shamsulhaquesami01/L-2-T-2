#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <numeric>
using namespace std;

bool canAllocate(const vector<int>& tasks, int k, int maxLoad) {
    int currentSum = 0;
    int count = 1;
    for (int task : tasks) {
        if (currentSum + task > maxLoad) {
            count++;
            currentSum = task;
            if (count > k) {
                return false;
            }
        } else {
            currentSum += task;
        }
    }
    return true;
}

int minimize_maxworkload(const vector<int>& tasks, int n, int k) {
    if (n < k) {
        return -1;
    }
    int left = *max_element(tasks.begin(), tasks.end());
    int right = accumulate(tasks.begin(), tasks.end(), 0);
    int result = right;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (canAllocate(tasks, k, mid)) {
            result = mid;
            right = mid - 1;
        } else {
            left = mid + 1;
        }
    }
    return result;
}

int main() {
    // File input/output setup
    ifstream infile("input.txt");
    ofstream outfile("output.txt");
    
    // Check if files opened successfully
    if (!infile.is_open()) {
        cerr << "Error opening input file!" << endl;
        return 1;
    }
    if (!outfile.is_open()) {
        cerr << "Error opening output file!" << endl;
        return 1;
    }
    
    int t;
    infile >> t;
    
    while (t--) {
        int n, k;
        infile >> n >> k;
        
        vector<int> arr(n);
        for (int i = 0; i < n; i++) {
            infile >> arr[i];
        }
        
        outfile << minimize_maxworkload(arr, n, k) << endl;
    }
    
    // Close files
    infile.close();
    outfile.close();
    
    return 0;
}