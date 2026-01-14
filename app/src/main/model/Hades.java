package main.model;

import main.simulation.Event;
import main.simulation.EventCalendar;
import main.simulation.EventType;
import main.utils.SoulStatus;

import java.util.List;

public class Hades {

    private final Buffer buffer;
    private final List<Charon> charons;
    private final EventCalendar calendar;

    public Hades(Buffer buffer, List<Charon> charons, EventCalendar calendar) {
        this.buffer = buffer;
        this.charons = charons;
        this.calendar = calendar;
    }

    public void handle(Event event) {

        switch (event.getType()) {

            case SOUL_ARRIVED -> {
                Soul soul = event.getSoul();

                if (buffer.hasSpace()) {
                    buffer.addSoul(soul);
                    soul.setStatus(SoulStatus.IN_BUFFER);
                    System.out.println("➕ Душа " + soul.getId() + " попала в буфер");
                } else {
                    soul.setStatus(SoulStatus.REJECTED);
                    System.out.println("❌ Душа " + soul.getId() + " отклонена Цербером");
                }

                calendar.add(new Event(event.getTime(), EventType.HADES_DECISION, null));
            }

            case HADES_DECISION -> {
                Soul soul = chooseSoulFromBuffer();
                Charon charon = chooseFreeCharon();

                if (soul != null && charon != null) {
                    soul.setStatus(SoulStatus.SENT_TO_CHARON);
                    System.out.println("➡️ Аид отправил душу " + soul.getId() + " к " + charon.getName());

                    Event finishEvent = charon.transport(soul, event.getTime());
                    calendar.add(finishEvent);
                }
            }

            case CHARON_FINISHED -> {
                Soul soul = event.getSoul();
                soul.setStatus(SoulStatus.DONE);
                System.out.println("✅ Душа " + soul.getId() + " доставлена");

                for (Charon c : charons) {
                    if (c.isBusy()) {
                        c.finish();
                        break;
                    }
                }

                calendar.add(new Event(event.getTime(), EventType.HADES_DECISION, null));
            }
        }
    }

    private Soul chooseSoulFromBuffer() {
        for (Soul s : buffer.getSouls()) {
            if (s != null) return s;
        }
        return null;
    }

    private Charon chooseFreeCharon() {
        for (Charon c : charons) {
            if (!c.isBusy()) return c;
        }
        return null;
    }
}
