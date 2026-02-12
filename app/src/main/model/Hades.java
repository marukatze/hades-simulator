package main.model;

import main.simulation.Event;
import main.simulation.EventCalendar;
import main.simulation.EventType;
import main.utils.SoulStatus;

import java.util.List;

public class Hades {

    private final Buffer buffer;
    private final Cerberus cerberus;
    private final List<Charon> charons;
    private final EventCalendar calendar;
    private final List<Source> sources;  // ✅ добавляем список источников

    private int lastCharonIndex = -1;
    private double currentEventTime = 0.0;

    public Hades(Buffer buffer,
                 Cerberus cerberus,
                 List<Charon> charons,
                 EventCalendar calendar,
                 List<Source> sources) {  // ✅ добавляем параметр

        this.buffer = buffer;
        this.cerberus = cerberus;
        this.charons = charons;
        this.calendar = calendar;
        this.sources = sources;  // ✅ сохраняем
    }

    public void handle(Event event, double currentTime) {
        this.currentEventTime = currentTime;

        switch (event.getType()) {

            case SOUL_ARRIVED -> {
                Soul soul = event.getSoul();
                cerberus.handleArrival(soul, currentTime);

                // ✅ КЛЮЧЕВОЕ: планируем СЛЕДУЮЩУЮ душу от этого источника
                Source source = findSourceById(soul.getSourceId());
                if (source != null) {
                    source.scheduleNextSoul(currentTime);
                }

                calendar.add(new Event(
                        currentTime,
                        EventType.HADES_DECISION,
                        null
                ));
            }

            case HADES_DECISION -> {
                if (hasFreeCharon() && buffer.getCurrentSize() > 0) {
                    Soul soul = chooseSoulFromBuffer(currentTime);
                    Charon charon = chooseCharon();

                    if (soul != null && charon != null) {
                        soul.setStatus(SoulStatus.SENT_TO_CHARON);
                        soul.setServiceStartTime(currentTime);

                        System.out.println(
                                "👑 Hades sends soul " + soul.getId() +
                                        " to " + charon.getName() +
                                        " at t=" + String.format("%.3f", currentTime)
                        );

                        Event finish = charon.transport(soul, currentTime);
                        calendar.add(finish);
                    }
                }
            }

            case CHARON_FINISHED -> {
                Soul soul = event.getSoul();
                if (soul == null) {
                    System.err.println("❌ CHARON_FINISHED with null soul!");
                    break;
                }

                soul.setStatus(SoulStatus.DONE);
                soul.setServiceEndTime(currentTime);

                Charon finishedCharon = findCharonBySoul(soul);
                if (finishedCharon != null) {
                    finishedCharon.finish();
                    System.out.println(
                            "🏁 " + finishedCharon.getName() +
                                    " delivered soul " + soul.getId() +
                                    " at t=" + String.format("%.3f", currentTime)
                    );
                } else {
                    System.err.println("❌ Could not find Charon for soul " + soul.getId());
                }

                // Планируем решение Аида
                calendar.add(new Event(
                        currentTime,
                        EventType.HADES_DECISION,
                        null
                ));
            }
        }
    }

    // ✅ Метод для поиска источника по ID
    private Source findSourceById(int sourceId) {
        for (Source s : sources) {
            if (s.getSourceId() == sourceId) {
                return s;
            }
        }
        return null;
    }

    /**
     * Д2Б4 - выбор заявки из буфера по приоритету:
     * 1️⃣ Меньший sourceId = выше приоритет
     * 2️⃣ Среди одинакового приоритета - ПОСЛЕДНЯЯ поступившая в буфер
     */
    private Soul chooseSoulFromBuffer(double currentTime) {
        Soul best = null;
        int bestIndex = -1;

        for (int i = 0; i < buffer.getCapacity(); i++) {
            Soul s = buffer.getAt(i);

            if (s == null || s.getStatus() != SoulStatus.IN_BUFFER) continue;

            if (best == null) {
                best = s;
                bestIndex = i;
                continue;
            }

            // 1️⃣ Приоритет по номеру источника (меньше = выше)
            if (s.getSourceId() < best.getSourceId()) {
                best = s;
                bestIndex = i;
            }
            // 2️⃣ Если приоритет одинаковый - выбираем ПОСЛЕДНЮЮ поступившую в буфер
            else if (s.getSourceId() == best.getSourceId()) {
                if (s.getBufferEntryTime() > best.getBufferEntryTime()) {
                    best = s;
                    bestIndex = i;
                }
            }
        }

        if (best != null) {
            buffer.setAt(bestIndex, null);
            buffer.decrementCurrentSize();
            best.setStatus(SoulStatus.SENT_TO_CHARON);
            best.setServiceStartTime(currentTime);
        }

        return best;
    }

    /**
     * Д2П2 - выбор прибора ПО КОЛЬЦУ
     */
    private Charon chooseCharon() {
        int n = charons.size();
        if (n == 0) return null;

        int start = (lastCharonIndex + 1) % n;

        for (int i = 0; i < n; i++) {
            int index = (start + i) % n;
            Charon c = charons.get(index);

            if (!c.isBusy()) {
                lastCharonIndex = index;
                return c;
            }
        }
        return null;
    }

    private Charon findCharonBySoul(Soul soul) {
        for (Charon c : charons) {
            if (c.isBusy() && c.getCurrentSoul() != null &&
                    c.getCurrentSoul().equals(soul)) {
                return c;
            }
        }
        return null;
    }

    private boolean hasFreeCharon() {
        return charons.stream().anyMatch(c -> !c.isBusy());
    }

    public boolean isIdle() {
        return buffer.getCurrentSize() == 0 &&
                charons.stream().noneMatch(Charon::isBusy);
    }
}