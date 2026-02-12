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
        double mu = 1.15; // placeholder

        // 1️⃣ Создаём календарь
        EventCalendar calendar = new EventCalendar();

        // 2️⃣ Создаём буфер и Цербера
        Buffer buffer = new Buffer(5);
        Cerberus cerberus = new Cerberus(buffer);

        // 3️⃣ Создаём Харонов
        List<Charon> charons = new ArrayList<>();
        charons.add(new Charon("Charon-1", mu));
        charons.add(new Charon("Charon-2", mu));
        charons.add(new Charon("Charon-3", mu));  // добавляем
        charons.add(new Charon("Charon-4", mu));  // добавляем



        // 5️⃣ Создаём источники душ
        List<Source> sources = new ArrayList<>();
        sources.add(new Source(1, 0.2, 0.3, calendar));  // высокий приоритет, прибытие каждые 0.3-0.7
        sources.add(new Source(2, 0.2, 0.3, calendar));  // низкий приоритет, прибытие каждые 0.5-1.0
        sources.add(new Source(3, 0.2, 0.3, calendar));  // низкий приоритет, прибытие каждые 0.5-1.0

        // 4️⃣ Создаём Аида
        Hades hades = new Hades(buffer, cerberus, charons, calendar, sources);
        // 6️⃣ Создаём симуляцию
        Simulation sim = new Simulation(calendar, hades, sources);

        // 7️⃣ Инициализация
        sim.init();

        // 8️⃣ Запуск выбранного режима
        if (stepMode) {
            runStepMode(sim, buffer, charons);
        } else {
            runAutoMode(sim, buffer, charons, deltaTime);
        }
    }

    private static void runStepMode(Simulation sim, Buffer buffer, List<Charon> charons) {
        Scanner scanner = new Scanner(System.in);
        int step = 0;
        System.out.println("=== START STEP MODE (EVENT-DRIVEN) ===");

        while (!sim.isFinished()) {
            System.out.println("\nPress Enter to process next event...");
            scanner.nextLine();

            step++;
            boolean processed = sim.processNextEvent(); // обрабатываем ОДНО событие

            System.out.println("=== Event " + step + " | t=" + String.format("%.3f", sim.getCurrentTime()) + " ===");
            if (!processed) {
                System.out.println("No more events!");
                break;
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
