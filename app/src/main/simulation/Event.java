package main.simulation;

import main.model.Soul;

public class Event {

    private final double time;
    private final EventType type;
    private final Soul soul;

    public Event(double time, EventType type, Soul soul) {
        this.time = time;
        this.type = type;
        this.soul = soul;
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

    public String describe() {
        return switch (type) {
            case SOUL_ARRIVED -> "📌 Soul " + soul.getId() + " arrived";
            case HADES_DECISION -> "👑 Hades decision";
            case CHARON_FINISHED ->  "🏁 Soul " + soul.getId() + " delivered";
        };
    }

}
