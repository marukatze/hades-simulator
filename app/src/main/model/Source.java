package main.model;

import main.simulation.Event;
import main.simulation.EventCalendar;
import main.simulation.EventType;

import java.util.Random;

public class Source {

    private final int sourceId;
    private int innerCounter = 0;

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

    /**
     * Создаёт новую душу с ID вида sourceId-innerId
     */
    private Soul generateSoul() {
        innerCounter++;
        String soulId = sourceId + "-" + innerCounter;
        return new Soul(soulId);
    }

    /**
     * Планирует событие прибытия души
     */
    public void scheduleNextArrival(double currentTime) {

        // равномерное распределение
        double interval =
                arrivalMin + (arrivalMax - arrivalMin) * random.nextDouble();

        double eventTime = currentTime + interval;

        Soul soul = generateSoul();

        Event arrivalEvent = new Event(
                eventTime,
                EventType.SOUL_ARRIVED,
                soul
        );

        calendar.add(arrivalEvent);
    }
}
