package main.model;

import main.simulation.Event;
import main.simulation.EventType;
import main.utils.SoulStatus;

public class Charon {

    private final String name;
    private final double mu;
    private boolean busy = false;
    private Soul currentSoul = null;  // ВАЖНО: запоминаем, кого перевозим

    public Charon(String name, double mu) {
        this.name = name;
        this.mu = mu;
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
        double serviceTime = -Math.log(1 - u) / mu;
        soul.setServiceTime(serviceTime);

        double finishTime = currentTime + serviceTime;

        // 🔍 ОТЛАДКА
        System.out.println("⏱️ " + name + " started transport of soul " + soul.getId() +
                " at t=" + String.format("%.3f", currentTime) +
                ", service time=" + String.format("%.3f", serviceTime) +
                ", finish at t=" + String.format("%.3f", finishTime));

        return new Event(
                finishTime,
                EventType.CHARON_FINISHED,
                soul
        );
    }

    public void finish() {
        busy = false;
        currentSoul = null;
    }

    public String getName() {
        return name;
    }
}