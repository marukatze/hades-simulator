package main.simulation;

import main.model.*;
import main.utils.EventLogger;
import main.utils.Statistics;

import java.util.List;

public class Simulation {

    private final EventCalendar calendar;
    private final Cerberus cerberus;
    private final Hades hades;
    private final List<Source> sources;
    private final boolean isStep;
    private final Statistics statistics;  // ✅ Добавляем статистику

    private double currentTime = 0.0;
    private static final double EPS = 1e-6;

    public Simulation(EventCalendar calendar,
                      Cerberus cerberus,
                      Hades hades,
                      List<Source> sources,
                      boolean isStep,
                      Statistics statistics) {  // ✅ Добавляем в конструктор

        this.calendar = calendar;
        this.cerberus = cerberus;
        this.hades = hades;
        this.sources = sources;
        this.isStep = isStep;
        this.statistics = statistics;
    }

    public boolean processNextEvent() {
        if (calendar.isEmpty()) return false;

        Event event = calendar.next();
        currentTime = event.getTime();

        // ✅ Обновляем статистику перед обработкой события
        if(!isStep) statistics.updateBeforeEvent(currentTime, hades.getBuffer(), hades.getCharons());

        handleEvent(event);

        // ✅ Обновляем статистику после обработки события
        if(!isStep) statistics.updateAfterEvent(currentTime, hades.getBuffer(), hades.getCharons());

        return true;
    }

    private void handleEvent(Event event) {

        switch (event.getType()) {

            case SOUL_ARRIVED -> handleArrival(event);

            case HADES_DECISION -> handleDecision();

            case CHARON_FINISHED -> handleFinish(event);
        }
    }

    private void handleArrival(Event event) {
        Soul soul = event.getSoul();

        if(isStep) EventLogger.logSoulArrival(soul, currentTime);
        else statistics.registerSoulCreated(soul);

        Soul rejected = cerberus.handleArrival(soul, currentTime);
        if (rejected == null) {
            if(isStep) EventLogger.logCerberusInsert(soul, soul.getBufferIndex());
            else statistics.registerSoulBuffered(soul);
        } else {
            if(isStep) EventLogger.logCerberusReject(rejected, soul, soul.getBufferIndex());
            else {
                statistics.registerSoulRejected(rejected, currentTime);
                statistics.registerSoulBuffered(soul);
            }
        }

        Source source = findSourceById(soul.getSourceId());
        if (source != null) {
            source.scheduleNext(currentTime);
        }

        calendar.add(new Event(currentTime + EPS,
                EventType.HADES_DECISION,
                null));
    }

    private void handleDecision() {

        Event finishEvent = hades.makeDecision(currentTime);

        if (finishEvent != null) {
            calendar.add(finishEvent);
            if(isStep) EventLogger.logHadesDecision(finishEvent.getSoul(), finishEvent.getSoul().getCharon());
            // ✅ Регистрируем начало обслуживания
            statistics.registerServiceStarted(finishEvent.getSoul(), currentTime);
        } else {
            if(isStep) EventLogger.logHadesSleeps();
        }
    }

    private void handleFinish(Event event) {

        Soul soul = event.getSoul();

        hades.finishService(soul);
        if(isStep) EventLogger.logCharonFinish(soul.getCharon(), soul, currentTime);
        else statistics.registerServiceCompleted(soul, currentTime);

        calendar.add(new Event(currentTime + EPS,
                EventType.HADES_DECISION,
                null));
    }

    private Source findSourceById(int id) {
        return sources.stream()
                .filter(s -> s.getSourceId() == id)
                .findFirst()
                .orElse(null);
    }

    public double getCurrentTime() {
        return currentTime;
    }

    // ✅ Геттер для статистики
    public Statistics getStatistics() {
        return statistics;
    }
}