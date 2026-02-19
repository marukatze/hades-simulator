package main;

import main.model.*;
import main.simulation.*;
import main.utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        double mu = Config.getCharonMu();
        int charonCount = Config.getCharonCount();
        int bufferCapacity = Config.getBufferCapacity();
        int sourceCount = Config.getSourceCount();
        double maxTime = Config.getSimulationMaxTime();
        boolean isStep = Config.getMode().equalsIgnoreCase("step");

        EventCalendar calendar = new EventCalendar();

        Buffer buffer = new Buffer(bufferCapacity);
        Cerberus cerberus = new Cerberus(buffer);

        List<Charon> charons = new ArrayList<>();
        for (int i = 1; i <= charonCount; i++) {
            charons.add(new Charon("Charon-" + i, mu));
        }

        List<Source> sources = new ArrayList<>();
        for (int i = 1; i <= sourceCount ; i++) {
            sources.add(new Source(i, new Random().nextDouble() * 0.5,
                    1 - new Random().nextDouble() * 0.5, calendar));
        }

        Hades hades = new Hades(buffer, charons);

        Statistics stats = new Statistics();
        Simulation simulation = new Simulation(
                calendar,
                cerberus,
                hades,
                sources,
                isStep,
                stats
        );

        initSources(sources);

        if (isStep) {
            runStepMode(simulation, buffer, charons);
        } else {
            runAutoMode(simulation, buffer, charons, maxTime);
        }
    }

    private static void initSources(List<Source> sources) {
        for (Source source : sources) {
            source.scheduleNext(0.0);
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
        ChartGenerator.showCharonLoadChart(sim.getStatistics(), charons.size());
        ChartGenerator.showBufferUsageChart(sim.getStatistics(), buffer.getCapacity());
        ChartGenerator.showRejectionRateBySourceChart(sim.getStatistics());
    }
}
