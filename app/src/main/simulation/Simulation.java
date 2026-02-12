package main.simulation;

import main.model.*;
import java.util.ArrayList;
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

    public void init() {
        if (!initialized) {
            // Запускаем бесконечную генерацию источников
            for (Source s : sources) {
                s.startGenerating(currentTime);  // ✅ generateSoul() вызывается внутри startGenerating()
            }
            // Первое решение Аида
            calendar.add(new Event(currentTime, EventType.HADES_DECISION, null));
            initialized = true;
        }
    }

    // ВАЖНО: теперь обрабатываем ТОЛЬКО ОДНО ближайшее событие
    public List<Event> tick(double deltaTime) {
        double targetTime = currentTime + deltaTime;
        List<Event> stepEvents = new ArrayList<>();

        // Обрабатываем ВСЕ события, время которых <= targetTime
        while (!calendar.isEmpty() && calendar.peek().getTime() <= targetTime) {
            Event e = calendar.next();
            currentTime = e.getTime();  // двигаем время точно к моменту события!
            hades.handle(e, currentTime);  // ✅ передаём currentTime вторым аргументом
            stepEvents.add(e);
        }

        // Если событий больше нет, но время еще не достигло targetTime
        if (currentTime < targetTime) {
            currentTime = targetTime;  // просто двигаем время вперед
        }

        return stepEvents;
    }

    // И для processNextEvent() тоже исправить:
    public boolean processNextEvent() {
        if (calendar.isEmpty()) return false;

        Event e = calendar.next();
        currentTime = e.getTime();
        hades.handle(e, currentTime);  // ✅ передаём currentTime
        return true;
    }


    public double getCurrentTime() {
        return currentTime;
    }

    public boolean isFinished() {
        return calendar.isEmpty() && hades.isIdle();
    }
}