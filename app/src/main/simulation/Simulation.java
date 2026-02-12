package main.simulation;

import main.model.*;

import java.util.List;

public class Simulation {

    private final EventCalendar calendar;
    private final Hades hades;
    private final List<Source> sources;
    private double currentTime = 0.0;
    private boolean initialized = false;

    public Simulation(EventCalendar calendar, Hades hades, List<Source> sources) {
        this.calendar = calendar;
        this.hades = hades;
        this.sources = sources;
    }

    /**
     * Инициализация симуляции:
     * - Запускаем генерацию от всех источников
     * - Создаём первое решение Аида
     */
    public void init() {
        if (!initialized) {
            for (Source s : sources) {
                s.startGenerating(currentTime);
            }
            calendar.add(new Event(currentTime, EventType.HADES_DECISION, null));
            initialized = true;
        }
    }

    /**
     * ОСНОВНОЙ МЕТОД: обрабатывает ОДНО ближайшее событие.
     * Используй ЭТОТ метод для пошагового режима!
     */
    public boolean processNextEvent() {
        if (calendar.isEmpty()) return false;

        Event e = calendar.next();
        currentTime = e.getTime();
        hades.handle(e, currentTime);
        return true;
    }

    /**
     * Альтернативный метод для симуляции с фиксированным шагом по времени.
     * Может обработать несколько событий за один шаг.
     */
    public List<Event> tick(double deltaTime) {
        double targetTime = currentTime + deltaTime;
        List<Event> stepEvents = new java.util.ArrayList<>();

        while (!calendar.isEmpty() && calendar.peek().getTime() <= targetTime) {
            Event e = calendar.next();
            currentTime = e.getTime();
            hades.handle(e, currentTime);
            stepEvents.add(e);
        }

        if (currentTime < targetTime) {
            currentTime = targetTime;
        }

        return stepEvents;
    }

    /**
     * Возвращает следующее событие без его обработки (для логирования)
     */
    public Event getNextEvent() {
        return calendar.peek();
    }

    /**
     * Текущее время симуляции
     */
    public double getCurrentTime() {
        return currentTime;
    }

    /**
     * Проверка, завершена ли симуляция
     */
    public boolean isFinished() {
        return calendar.isEmpty() && hades.isIdle();
    }
}