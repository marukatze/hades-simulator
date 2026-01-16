package main.model;

import main.simulation.Event;
import main.simulation.EventType;
import main.utils.SoulStatus;

public class Charon {

    private final String name;
    private final double mu;
    private boolean busy = false;

    public boolean isBusy() {
        return busy;
    }

    public Charon(String name, double mu) {
        this.name = name;
        this.mu = mu;
    }

    public Event transport(Soul soul, double currentTime) {
        busy = true;
        soul.setStatus(SoulStatus.TRANSPORTING);

        double u = Math.random();
        double serviceTime = -Math.log(1 - u) / mu;

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
