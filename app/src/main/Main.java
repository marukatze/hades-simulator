package main;

import main.model.*;
import main.simulation.*;
import main.utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean stepMode = scanner.nextLine().equalsIgnoreCase("step");

        EventCalendar calendar = new EventCalendar();

        Buffer buffer = new Buffer(4);
        Cerberus cerberus = new Cerberus(buffer);

        double mu = 2.5;
        List<Charon> charons = new ArrayList<>();
        charons.add(new Charon("Charon-1", mu));
        charons.add(new Charon("Charon-2", mu));
        charons.add(new Charon("Charon-3", mu));
        charons.add(new Charon("Charon-4", mu));

        List<Source> sources = new ArrayList<>();
        sources.add(new Source(1, 0.2, 0.3, calendar));
        sources.add(new Source(2, 0.2, 0.3, calendar));
        sources.add(new Source(3, 0.2, 0.3, calendar));

        Hades hades = new Hades(buffer, charons);

        Statistics stats = new Statistics();
        Simulation simulation = new Simulation(
                calendar,
                cerberus,
                hades,
                sources,
                stepMode,
                stats
        );

        initSources(sources);

        if (stepMode) {
            runStepMode(simulation, buffer, charons);
        } else {
            runAutoMode(simulation, buffer, charons, 10.0);
        }
    }

    private static void initSources(List<Source> sources) {
        for (Source source : sources) {
            Event first = source.scheduleNext(0.0);
        }
    }

    private static void runStepMode(Simulation sim,
                                    Buffer buffer,
                                    List<Charon> charons) {

        Scanner scanner = new Scanner(System.in);
        int eventCount = 0;

        System.out.println("STEP MODE");
        System.out.println(EventLogger.SEPARATOR);
        EventLogger.logEventHeader(eventCount++, sim.getCurrentTime());

        while (sim.processNextEvent()) {
            EventLogger.logBufferState(buffer);
            EventLogger.logCharonsState(charons);

            System.out.println("\npress enter or q to quit");
            if(scanner.nextLine().equalsIgnoreCase("q")) {
                return;
            }
            EventLogger.logEventHeader(eventCount, sim.getCurrentTime());

            eventCount++;
        }
    }

    private static void runAutoMode(Simulation sim,
                                    Buffer buffer,
                                    List<Charon> charons,
                                    double maxTime) {

        int eventCount = 0;
        long startTime = System.currentTimeMillis();
        System.out.println("AUTO MODE");
        System.out.println(EventLogger.SEPARATOR);

        while (sim.getCurrentTime() < maxTime
                && sim.processNextEvent()) {
            System.out.println(sim.getCurrentTime());
            EventLogger.logBufferState(buffer);
            EventLogger.logCharonsState(charons);
            eventCount++;
        }

        long timeElapsed = System.currentTimeMillis() - startTime;

        System.out.println("\n✅ Симуляция завершена за " + timeElapsed + " мс");
        System.out.println("📊 Всего обработано событий: " + eventCount);
        sim.getStatistics().printFinalReport(timeElapsed);
    }
}
