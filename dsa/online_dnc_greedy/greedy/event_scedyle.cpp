#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

class Event{ 
    public:
    int start, end;
   
    Event(int start, int end){
        this->start = start;
        this->end=end;
    }
};

bool cmp(Event a, Event b){
    return a.end < b.end;  
}

int maxEvents(vector<Event>& events) {
    sort(events.begin(), events.end(), cmp);
    int count = 0;
    int lastEnd = -1;

    for (auto e : events) {
        if (e.start >= lastEnd){
            count++;
            lastEnd = e.end;
        }
    }

    return count;
}

int main() {
    vector<Event> events = {Event(1, 3), Event(2, 4), Event(3, 5)};
    cout << maxEvents(events) << endl;  
    return 0;
}