package main.model;

import main.simulation.Event;
import main.utils.SoulStatus;

import java.util.List;

public class Hades {

    private final Buffer buffer;
    private final List<Charon> charons;

    private int lastCharonIndex = -1;

    public Hades(Buffer buffer,
                 List<Charon> charons) {

        this.buffer = buffer;
        this.charons = charons;
    }

    public Event makeDecision(double time) {

        if (buffer.getCurrentSize() == 0) return null;
        if (!hasFreeCharon()) return null;

        Soul soul = chooseSoulFromBuffer();
        Charon charon = chooseCharon();

        if (soul == null || charon == null) return null;

        soul.setStatus(SoulStatus.SENT_TO_CHARON);
        soul.setServiceStartTime(time);
        soul.setCharon(charon);

        return charon.transport(soul, time);
    }

    public void finishService(Soul soul) {

        Charon charon = findCharonBySoul(soul);

        if (charon != null) {
            charon.finish();
        }
    }

    // =========================

    private boolean hasFreeCharon() {
        return charons.stream().anyMatch(c -> !c.isBusy());
    }

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
        return charons.stream()
                .filter(c -> c.isBusy()
                        && c.getCurrentSoul() != null
                        && c.getCurrentSoul().equals(soul))
                .findFirst()
                .orElse(null);
    }

    private Soul chooseSoulFromBuffer() {

        Soul best = null;
        int bestIndex = -1;

        for (int i = 0; i < buffer.getCapacity(); i++) {

            Soul s = buffer.getAt(i);

            if (s == null || s.getStatus() != SoulStatus.IN_BUFFER)
                continue;

            if (best == null ||
                    s.getSourceId() < best.getSourceId() ||
                    (s.getSourceId() == best.getSourceId()
                            && s.getBufferEntryTime() > best.getBufferEntryTime())) {

                best = s;
                bestIndex = i;
            }
        }

        if (best != null) {
            buffer.setAt(bestIndex, null);
        }

        return best;
    }

    public List<Charon> getCharons() {
        return charons;
    }

    public Buffer getBuffer() {
        return buffer;
    }
}
