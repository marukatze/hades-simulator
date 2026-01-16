package main;

import main.model.*;
import main.simulation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        boolean stepMode = true; // true = пошаговый режим, false = авто
        double deltaTime = 1.0;  // единица времени за шаг
        double mu = 15; // placeholder

        // 1️⃣ Создаём календарь
        EventCalendar calendar = new EventCalendar();

        // 2️⃣ Создаём буфер и Цербера
        Buffer buffer = new Buffer(5);
        Cerberus cerberus = new Cerberus(buffer);

        // 3️⃣ Создаём Харонов
        List<Charon> charons = new ArrayList<>();
        charons.add(new Charon("Charon-1", mu));
        charons.add(new Charon("Charon-2", mu));

        // 4️⃣ Создаём Аида
        Hades hades = new Hades(buffer, cerberus, charons, calendar);

        // 5️⃣ Создаём источники душ
        List<Source> sources = new ArrayList<>();
        sources.add(new Source(1, 1.0, 3.0, calendar));
        sources.add(new Source(2, 2.0, 4.0, calendar));

        // 6️⃣ Создаём симуляцию
        Simulation sim = new Simulation(calendar, hades, sources);

        // 7️⃣ Инициализация
        sim.init();

        // 8️⃣ Запуск выбранного режима
        if (stepMode) {
            runStepMode(sim, buffer, charons, deltaTime);
        } else {
            runAutoMode(sim, buffer, charons, deltaTime);
        }
    }

    private static void runStepMode(Simulation sim, Buffer buffer, List<Charon> charons, double deltaTime) {
        Scanner scanner = new Scanner(System.in);
        int step = 0;
        System.out.println("=== START STEP MODE ===");
        while (!sim.isFinished()) {
            System.out.println("\nPress Enter to process next time unit...");
            scanner.nextLine();

            step++;
            List<Event> events = sim.tick(deltaTime);

            System.out.println("=== Step " + step + " | t=" + sim.getCurrentTime() + " ===");
            if (events.isEmpty()) {
                System.out.println("(no events)");
            } else {
                for (Event e : events) {
                    System.out.println(e.describe());
                }
            }

            System.out.println(buffer);
            printCharons(charons);
        }
        System.out.println("\n=== STEP SIMULATION END ===");
    }

    private static void runAutoMode(Simulation sim, Buffer buffer, List<Charon> charons, double deltaTime) {
        int step = 0;
        System.out.println("=== START AUTO MODE ===");
        while (!sim.isFinished()) {
            step++;
            List<Event> events = sim.tick(deltaTime);

            // можно собирать статистику для таблицы (например, количество доставленных, отказанных душ)
            // здесь просто печатаем кратко:
            System.out.println("Step " + step + " | t=" + sim.getCurrentTime() + " | Events: " + events.size());
        }
        System.out.println("\n=== AUTO SIMULATION END ===");
        System.out.println(buffer);
        printCharons(charons);
    }

    private static void printCharons(List<Charon> charons) {
        System.out.print("Charons: ");
        for (Charon c : charons) {
            System.out.print(c.getName() + (c.isBusy() ? "[BUSY] " : "[FREE] "));
        }
        System.out.println("\n");
    }
}
