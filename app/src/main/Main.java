package main;

import main.model.*;
import main.simulation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // 1️⃣ Создаём календарь
        EventCalendar calendar = new EventCalendar();

        // 2️⃣ Создаём буфер и Цербера
        Buffer buffer = new Buffer(5);
        Cerberus cerberus = new Cerberus(buffer);

        // 3️⃣ Создаём Харонов
        List<Charon> charons = new ArrayList<>();
        charons.add(new Charon("Charon-1"));
        charons.add(new Charon("Charon-2"));

        // 4️⃣ Создаём Аида
        Hades hades = new Hades(buffer, cerberus, charons, calendar);

        // 5️⃣ Создаём источники душ
        List<Source> sources = new ArrayList<>();
        sources.add(new Source(1, 1.0, 3.0, calendar));
        sources.add(new Source(2, 2.0, 4.0, calendar));

        // 6️⃣ Создаём симуляцию
        Simulation sim = new Simulation(calendar, hades, sources);

        // 7️⃣ Инициализация (стартовые события)
        sim.init();

        System.out.println("=== START SIMULATION ===");

        // 8️⃣ Пошаговая симуляция (интерактивно)
        while (!calendar.isEmpty()) {
            System.out.println("\n--- Press Enter to process next event ---");
            scanner.nextLine(); // ждём Enter

            // обрабатываем следующий шаг
            sim.step();

            // вывод состояния буфера
            System.out.println(buffer);

            // вывод состояния Харонов
            System.out.print("Charons: ");
            for (Charon c : charons) {
                System.out.print(c.getName() + (c.isBusy() ? "[BUSY] " : "[FREE] "));
            }
            System.out.println();
        }

        System.out.println("\n=== SIMULATION END ===");
    }
}
