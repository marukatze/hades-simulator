package main.simulation;

import java.util.PriorityQueue;

public class EventCalendar {

    private final PriorityQueue<Event> events =
            new PriorityQueue<>((a, b) -> Double.compare(a.getTime(), b.getTime()));

    public void add(Event event) {
        events.add(event);
    }

    public boolean isEmpty() {
        return events.isEmpty();
    }

    public Event next() {
        return events.poll();
    }
}
