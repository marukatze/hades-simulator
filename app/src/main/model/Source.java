package main.model;

import main.simulation.Event;
import main.simulation.EventCalendar;
import main.simulation.EventType;

import java.util.Random;

public class Source {

    private final int sourceId;           // номер источника = приоритет (1 - highest)
    private int generatedCount = 0;       // счетчик сгенерированных душ
    private int rejectedCount = 0;        // счетчик отказов

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

    public int getGeneratedCount() {
        return generatedCount;
    }

    public int getRejectedCount() {
        return rejectedCount;
    }

    public void incrementRejected() {
        rejectedCount++;
    }

    /**
     * Генерирует НОВУЮ душу и планирует её прибытие
     */
    public void generateSoul(double currentTime) {
        generatedCount++;
        String soulId = sourceId + "-" + generatedCount;

        // Равномерное распределение [arrivalMin, arrivalMax]
        double interval = arrivalMin + (arrivalMax - arrivalMin) * random.nextDouble();
        double arrivalTime = currentTime + interval;

        Soul soul = new Soul(soulId, sourceId, arrivalTime);

        Event arrivalEvent = new Event(
                arrivalTime,
                EventType.SOUL_ARRIVED,
                soul
        );

        calendar.add(arrivalEvent);

        System.out.println("✨ Source " + sourceId + " generated soul " + soulId +
                " at t=" + String.format("%.3f", currentTime) +
                " (arrives at t=" + String.format("%.3f", arrivalTime) + ")");
    }

    /**
     * Планирует СЛЕДУЮЩУЮ душу (вызывается после прибытия текущей)
     */
    public void scheduleNextSoul(double currentTime) {
        generateSoul(currentTime);
    }

    /**
     * Запускает бесконечную генерацию - планирует ПЕРВОЕ прибытие
     */
    public void startGenerating(double startTime) {
        generateSoul(startTime);
    }
}