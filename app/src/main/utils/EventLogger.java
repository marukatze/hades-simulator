package main.utils;

import main.model.*;
import main.simulation.Event;
import main.simulation.EventType;

import java.security.spec.RSAOtherPrimeInfo;
import java.util.List;

public class EventLogger {

    public static final String SEPARATOR = "═══════════════════════════════════════════════════";
    private static final String LINE = "───────────────────────────────────────────────────";

    public static void logEventHeader(int eventNumber, double time) {
        System.out.println("\n" + SEPARATOR);
        System.out.printf(" EVENT %d | t = %.3fs%n", eventNumber, time);
        System.out.println(SEPARATOR);
    }

    public static void logSoulArrival(Soul soul, double arrivalTime) {
        System.out.printf("▶ ARRIVAL: Soul %s%n",
                soul.getId(), soul.getSourceId(), soul.getSourceId());
    }

    public static void logCerberusInsert(Soul soul, int index) {
        System.out.printf("▶ CERBERUS: Soul %s inserted into buffer[%d]",
                soul.getId(), index);
    }

    public static void logCerberusReject(Soul rejected, Soul inserted, int index, double time) {
        System.out.printf("▶ CERBERUS: Buffer FULL! Rejected %s, inserted %s at buffer[%d]",
                rejected.getId(), inserted.getId(), index);
    }

    public static void logHadesDecision(Soul soul, Charon charon) {
        System.out.printf("▶ HADES: Soul %s sent to to %s%n",
                soul.getId(), charon.getName());
    }

    public static void logCharonStart(Charon charon, Soul soul, double serviceTime, double finishTime) {
        System.out.printf("▶ %-10s Started transport of Soul %s",
                charon.getName() + ":", soul.getId());
    }

    public static void logCharonFinish(Charon charon, Soul soul, double time) {
        System.out.printf("▶ %-10s DELIVERED Soul %s at t=%.3fs",
                charon.getName() + ":", soul.getId(), time);
    }

    public static void logBufferState(Buffer buffer) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(LINE).append("\n");
        sb.append(" BUFFER: ");

        for (int i = 0; i < buffer.getCapacity(); i++) {
            Soul s = buffer.getAt(i);
            if (s != null) {
                sb.append("[").append(s.getId()).append("]");
            } else {
                sb.append("[ ]");
            }
            if (i < buffer.getCapacity() - 1) sb.append(" ");
        }
        System.out.println(sb);
    }

    public static void logCharonsState(List<Charon> charons) {
        System.out.println(LINE);
        System.out.println(" CHARONS:");
        for (Charon c : charons) {
            StringBuilder sb = new StringBuilder();
            sb.append(c.getName()).append(": ");

            if (c.isBusy()) {
                Soul soul = c.getCurrentSoul();
                sb.append("BUSY (Soul ").append(soul.getId());
                sb.append(", ends at ").append(String.format("%.3fs)", soul.getServiceEndTime()));
            } else {
                sb.append("FREE");
            }
            System.out.println(sb);

        }
        System.out.println(LINE);
    }

    public static void logNextEvent(Event nextEvent) {
        System.out.println(LINE);
        if (nextEvent != null) {
            String desc = switch (nextEvent.getType()) {
                case SOUL_ARRIVED -> "Soul " + nextEvent.getSoul().getId() + " arrival";
                case CHARON_FINISHED -> "Charon finish for Soul " + nextEvent.getSoul().getId();
                case HADES_DECISION -> "Hades decision";
            };
            System.out.printf("⏱️  Next event at t=%.3fs (%s)%n",
                    nextEvent.getTime(), desc);
        } else {
            System.out.println("⏱️  No more events");
        }
        System.out.println(SEPARATOR);
    }
}