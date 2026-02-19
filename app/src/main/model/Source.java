package main.model;

import main.simulation.Event;
import main.simulation.EventCalendar;
import main.simulation.EventType;

import java.util.Random;

public class Source {

    private final int sourceId;
    private int generatedCount = 0;

    private final double arrivalMin;
    private final double arrivalMax;

    private final Random random = new Random();
    private final EventCalendar calendar;

    public Source(int sourceId,
                  double arrivalMin,
                  double arrivalMax,
                  EventCalendar calendar) {

        this.sourceId = sourceId;
        this.arrivalMin = arrivalMin;
        this.arrivalMax = arrivalMax;
        this.calendar = calendar;
    }

    public int getSourceId() {
        return sourceId;
    }

    public Event scheduleNext(double currentTime) {
        double interval = arrivalMin + (arrivalMax - arrivalMin) * random.nextDouble();
        double arrivalTime = currentTime + interval;

        generatedCount++;
        String soulId = sourceId + "-" + generatedCount;

        Soul soul = new Soul(soulId, sourceId, arrivalTime);

        Event arrivalEvent = new Event(
                arrivalTime,
                EventType.SOUL_ARRIVED,
                soul
        );

        calendar.add(arrivalEvent);
        return arrivalEvent;
    }

    public EventCalendar getCalendar() {
        return calendar;
    }
}