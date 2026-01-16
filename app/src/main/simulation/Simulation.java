package main.simulation;

import main.model.*;

import java.util.ArrayList;
import java.util.Iterator;
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

    // инициализация стартовых событий
    public void init() {
        if (!initialized) {
            for (Source s : sources) {
                s.scheduleNextArrival(currentTime);
            }
            // первый ход Hades
            calendar.add(new Event(currentTime, EventType.HADES_DECISION, null));
            initialized = true;
        }
    }

    // возвращает все события за один шаг времени (currentTime -> currentTime + deltaTime)
    public List<Event> tick(double deltaTime) {
        currentTime += deltaTime;
        List<Event> stepEvents = new ArrayList<>();

        Iterator<Event> iter = calendar.getEvents().iterator();
        while (iter.hasNext()) {
            Event e = iter.next();
            if (e.getTime() <= currentTime) {
                hades.handle(e);          // обработка события
                stepEvents.add(e);        // добавляем для отчета
                iter.remove();            // удаляем из календаря
            }
        }

        return stepEvents;
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public boolean isFinished() {
        // Симуляция закончена, когда календарь пуст и буфер пуст, а Хароны свободны
        return calendar.isEmpty() && hades.isIdle();
    }
}
