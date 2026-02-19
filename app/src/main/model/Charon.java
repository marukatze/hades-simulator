package main.model;

import main.simulation.Event;
import main.simulation.EventType;
import main.utils.SoulStatus;

public class Charon {

    private final String name;
    private final double lambda;  // интенсивность обслуживания
    private boolean busy = false;
    private Soul currentSoul = null;
    private double finishTime = 0.0;  // время окончания обслуживания

    public Charon(String name, double lambda) {
        this.name = name;
        this.lambda = lambda;
    }

    public boolean isBusy() {
        return busy;
    }

    public Soul getCurrentSoul() {
        return currentSoul;
    }

    public Event transport(Soul soul, double currentTime) {
        busy = true;
        currentSoul = soul;
        soul.setStatus(SoulStatus.TRANSPORTING);

        double u = Math.random();
        double serviceTime = -Math.log(1 - u) / lambda;
        soul.setServiceTime(serviceTime);
        soul.setServiceStartTime(currentTime);
        soul.setServiceEndTime(currentTime + serviceTime);

        finishTime = currentTime + serviceTime;

        return new Event(
                finishTime,
                EventType.CHARON_FINISHED,
                soul
        );
    }

    public void finish() {
        busy = false;
        currentSoul = null;
        finishTime = 0.0;
    }

    public String getName() {
        return name;
    }
}