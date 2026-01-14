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

    public Hades(Buffer buffer,
                 Cerberus cerberus,
                 List<Charon> charons,
                 EventCalendar calendar) {

        this.buffer = buffer;
        this.cerberus = cerberus;
        this.charons = charons;
        this.calendar = calendar;
    }

    public void handle(Event event) {

        switch (event.getType()) {

            case SOUL_ARRIVED -> {
                Soul soul = event.getSoul();
                cerberus.handleArrival(soul);
            }

            case HADES_DECISION -> {
                Soul soul = chooseSoulFromBuffer();
                Charon charon = chooseFreeCharon();

                if (soul != null && charon != null) {
                    soul.setStatus(SoulStatus.SENT_TO_CHARON);

                    System.out.println(
                            "👑 Hades sends soul " + soul.getId() +
                                    " to " + charon.getName()
                    );

                    Event finish =
                            charon.transport(soul, event.getTime());

                    calendar.add(finish);
                }
            }

            case CHARON_FINISHED -> {
                Soul soul = event.getSoul();
                soul.setStatus(SoulStatus.DONE);

                System.out.println(
                        "🏁 Soul " + soul.getId() + " delivered"
                );

                for (Charon c : charons) {
                    if (c.isBusy()) {
                        c.finish();
                        break;
                    }
                }

                calendar.add(new Event(
                        event.getTime(),
                        EventType.HADES_DECISION,
                        null
                ));
            }
        }
    }

    private Soul chooseSoulFromBuffer() {
        for (Soul s : buffer.getSouls()) {
            if (s != null && s.getState() == SoulStatus.IN_BUFFER) {
                return s;
            }
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
