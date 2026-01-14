package main.simulation;

import main.model.*;

import java.util.List;

public class Simulation {

    private double currentTime = 0.0;

    private final EventCalendar calendar;
    private final Hades hades;
    private final List<Source> sources;

    public Simulation(EventCalendar calendar,
                      Hades hades,
                      List<Source> sources) {

        this.calendar = calendar;
        this.hades = hades;
        this.sources = sources;
    }

    public void init() {
        // стартовые события от всех источников
        for (Source source : sources) {
            source.scheduleNextArrival(currentTime);
        }
    }

    public void step() {
        if (calendar.isEmpty()) {
            System.out.println("📭 Календарь пуст, симуляция остановлена");
            return;
        }

        Event event = calendar.next();
        currentTime = event.getTime();

        System.out.println("\n⏱ Время: " + currentTime);
        System.out.println("📌 Событие: " + event.getType());

        hades.handle(event);

        // если пришла душа — источник планирует следующую
        if (event.getType() == EventType.SOUL_ARRIVED) {
            for (Source source : sources) {
                source.scheduleNextArrival(currentTime);
            }
        }
    }

    public double getCurrentTime() {
        return currentTime;
    }
}
