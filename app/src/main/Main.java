package main;

import main.model.*;
import main.simulation.*;
import main.utils.EventLogger;

import javax.swing.border.EmptyBorder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        boolean stepMode = true;

        // 1️⃣ Календарь событий
        EventCalendar calendar = new EventCalendar();

        // 2️⃣ Буфер на 4 места (Д2Б4) и Цербер
        Buffer buffer = new Buffer(4);
        Cerberus cerberus = new Cerberus(buffer);

        // 3️⃣ Хароны с правильной интенсивностью
        double mu = 1.5;  // среднее время обслуживания = 0.4 сек
        List<Charon> charons = new ArrayList<>();
        charons.add(new Charon("Charon-1", mu));
        charons.add(new Charon("Charon-2", mu));
        charons.add(new Charon("Charon-3", mu));
        charons.add(new Charon("Charon-4", mu));

        // 4️⃣ Источники (3 штуки, равномерное распределение)
        List<Source> sources = new ArrayList<>();
        sources.add(new Source(1, 0.2, 0.3, calendar));  // приоритет 1 (высокий)
        sources.add(new Source(2, 0.2, 0.3, calendar));  // приоритет 2
        sources.add(new Source(3, 0.2, 0.3, calendar));  // приоритет 3 (низкий)

        // 5️⃣ Аид с доступом ко всем компонентам
        Hades hades = new Hades(buffer, cerberus, charons, calendar, sources);

        // 6️⃣ Симуляция
        Simulation sim = new Simulation(calendar, hades, sources);

        // 7️⃣ Запуск
        if (stepMode) {
            runStepMode(sim, buffer, charons);
        } else {
            runAutoMode(sim, buffer, charons, 1000.0);
        }
    }

    /**
     * ПОШАГОВЫЙ РЕЖИМ - обрабатываем по одному событию
     */
    private static void runStepMode(Simulation sim, Buffer buffer, List<Charon> charons) {
        Scanner scanner = new Scanner(System.in);
        int eventCount = 0;

        System.out.println("STEP MODE");
        System.out.println(EventLogger.SEPARATOR);

        sim.init();

        while (!sim.isFinished()) {
            System.out.println("\npress enter");
            scanner.nextLine();

            EventLogger.logEventHeader(eventCount, sim.getCurrentTime());
            boolean processed = sim.processNextEvent();
            if (!processed) {
                System.out.println("no more events");
                break;
            }

            eventCount++;

            EventLogger.logBufferState(buffer);
            EventLogger.logCharonsState(charons);
        }

        System.out.println("\n🏁 СИМУЛЯЦИЯ ЗАВЕРШЕНА");
        System.out.println("Обработано событий: " + eventCount);
    }

    /**
     * АВТОМАТИЧЕСКИЙ РЕЖИМ - симуляция до заданного времени
     */
    private static void runAutoMode(Simulation sim, Buffer buffer, List<Charon> charons, double maxTime) {
        sim.init();

        System.out.println("🚀 ЗАПУСК АВТОМАТИЧЕСКОЙ СИМУЛЯЦИИ");
        System.out.println("⏱️  Максимальное время: " + maxTime + " сек");
        System.out.println(EventLogger.SEPARATOR);

        int eventCount = 0;
        while (!sim.isFinished() && sim.getCurrentTime() < maxTime) {
            boolean processed = sim.processNextEvent();
            if (!processed) break;
            eventCount++;

            // Каждые 50 событий показываем состояние
            if (eventCount % 50 == 0) {
                System.out.printf("📈 Прогресс: t=%.3f, событий: %d, буфер: %d/%d%n",
                        sim.getCurrentTime(), eventCount,
                        buffer.getCurrentSize(), buffer.getCapacity());
            }
        }

        System.out.println("\n🏁 СИМУЛЯЦИЯ ЗАВЕРШЕНА");
        System.out.println("⏱️  Время: " + String.format("%.3f", sim.getCurrentTime()) + " сек");
        System.out.println("📊 Всего событий: " + eventCount);

        // Финальное состояние
        System.out.println("\n📊 ФИНАЛЬНОЕ СОСТОЯНИЕ:");
        EventLogger.logBufferState(buffer);
        EventLogger.logCharonsState(charons);
    }
}