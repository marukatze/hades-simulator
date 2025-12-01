package main.simulation;

import java.util.PriorityQueue;

public class EventCalendar {
    private final PriorityQueue<Event> queue = new PriorityQueue<>();

    // Добавляем событие
    public void addEvent(Event event) {
        queue.add(event);
    }

    // Получаем и убираем ближайшее событие
    public Event nextEvent() {
        return queue.poll();
    }

    // Проверка, есть ли события
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    // Получить текущее количество событий (для статистики)
    public int size() {
        return queue.size();
    }
}


