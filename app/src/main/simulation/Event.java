package main.simulation;

import main.model.Soul;

public class Event implements Comparable<Event> {

    private final double time;       // время наступления события
    private final EventType type;    // тип события
    private final Soul soul;   // какая заявка участвует (если участвует)
    private final Integer deviceId;  // какой прибор участвовал (если участвовал)

    public Event(double time, EventType type) {
        this(time, type, null, null);
    }

    public Event(double time, EventType type, Soul soul) {
        this(time, type, soul, null);
    }

    public Event(double time, EventType type, Soul soul, Integer deviceId) {
        this.time = time;
        this.type = type;
        this.soul = soul;
        this.deviceId = deviceId;
    }

    public double getTime() {
        return time;
    }

    public EventType getType() {
        return type;
    }

    public Soul getSoul() {
        return soul;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    /**
     * Сравниваем события по времени.
     * Это позволит использовать PriorityQueue<Event>
     * как календарь событий — ближайшее событие всегда первое.
     */
    @Override
    public int compareTo(Event other) {
        return Double.compare(this.time, other.time);
    }

    @Override
    public String toString() {
        return "Event{" +
                "time=" + time +
                ", type=" + type +
                ", Soul=" + (soul != null ? soul.getId() : "-") +
                ", deviceId=" + (deviceId != null ? deviceId : "-") +
                '}';
    }
}

