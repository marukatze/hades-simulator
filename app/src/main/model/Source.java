package main.model;

import main.simulation.Event;
import main.simulation.EventCalendar;
import main.simulation.EventType;
import main.simulation.Simulation;

import java.util.Random;

public class Source {
    private final int id;                     // Номер источника (важен для приоритетов)
    private final Random random = new Random();
    private int soulsCounter;
    private final double arrivalMin;          // Минимальный интервал
    private final double arrivalMax;          // Максимальный интервал
    private final EventCalendar calendar;     // Чтобы планировать новые события

    public Source(int id, double arrivalMin, double arrivalMax, EventCalendar calendar, Simulation simulation) {
        this.id = id;
        this.arrivalMin = arrivalMin;
        this.arrivalMax = arrivalMax;
        this.calendar = calendar;
        soulsCounter = 0;
    }

    public int getId() {
        return id;
    }

    public Soul generateSoul(double currentTime) {
        soulsCounter++;
        return new Soul(id, id + "-" + soulsCounter, currentTime);
    }

    // Планирование следующего ARRIVAL-события
    public void scheduleNextArrival(double currentTime) {
        double nextInterval = arrivalMin + (arrivalMax - arrivalMin) * random.nextDouble();
        double eventTime = currentTime + nextInterval;

        Soul newSoul = generateSoul(currentTime);
        Event arrivalEvent = new Event(eventTime, EventType.ARRIVAL, newSoul);

        calendar.addEvent(arrivalEvent);  // кладём в календарь
    }
}

