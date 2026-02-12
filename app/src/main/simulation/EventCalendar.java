package main.simulation;

import java.util.PriorityQueue;

public class EventCalendar {

    private final PriorityQueue<Event> events;

    public EventCalendar() {
        // ✅ ИНИЦИАЛИЗИРУЕМ В КОНСТРУКТОРЕ!
        this.events = new PriorityQueue<>((a, b) -> {
            int cmp = Double.compare(a.getTime(), b.getTime());
            if (cmp == 0) {
                // При одинаковом времени: HADES_DECISION последним
                if (a.getType() == EventType.HADES_DECISION && b.getType() != EventType.HADES_DECISION) return 1;
                if (b.getType() == EventType.HADES_DECISION && a.getType() != EventType.HADES_DECISION) return -1;
                // Если оба HADES_DECISION или оба не HADES_DECISION - порядок не важен
                return 0;
            }
            return cmp;
        });
    }

    public void add(Event event) {
        events.add(event);
        // System.out.println("📅 Added event: " + event.describe() + " at t=" + event.getTime());
    }

    public boolean isEmpty() {
        return events.isEmpty();
    }

    public Event next() {
        return events.poll();
    }

    public Event peek() {
        return events.peek();
    }

    public int size() {
        return events.size();
    }
}