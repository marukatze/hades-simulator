package main.model;

import main.simulation.Event;
import main.simulation.EventType;
import main.utils.SoulStatus;

public class Charon {

    private final String name;
    private boolean busy = false;

    public Charon(String name) {
        this.name = name;
    }

    public boolean isBusy() {
        return busy;
    }

    public Event transport(Soul soul, double currentTime) {
        busy = true;
        soul.setStatus(SoulStatus.TRANSPORTING);

        double serviceTime = Math.random() * 5 + 1; // пока заглушка

        return new Event(
                currentTime + serviceTime,
                EventType.CHARON_FINISHED,
                soul
        );
    }

    public void finish() {
        busy = false;
    }

    public String getName() {
        return name;
    }
}
