package main.model;

import main.utils.SoulStatus;

public class Cerberus {

    private final Buffer buffer;
    private int lastIndex = -1;

    public Cerberus(Buffer buffer) {
        this.buffer = buffer;
    }

    public void handleArrival(Soul soul) {
        System.out.println("🐶 Cerberus: processing soul " + soul.getId());

        if (buffer.hasSpace()) {
            int insertIndex = (lastIndex + 1) % buffer.getCapacity();

            while (buffer.getAt(insertIndex) != null) {
                insertIndex = (insertIndex + 1) % buffer.getCapacity();
            }

            buffer.setAt(insertIndex, soul);
            lastIndex = insertIndex;
            soul.setStatus(SoulStatus.IN_BUFFER);

            System.out.println(
                    "🐶 Cerberus: soul " + soul.getId() +
                            " inserted at buffer[" + insertIndex + "]"
            );

        } else {
            // Д1ОО4 — вытесняем последнюю вставленную
            Soul rejected = buffer.getAt(lastIndex);
            rejected.setStatus(SoulStatus.REJECTED);

            buffer.setAt(lastIndex, soul);
            soul.setStatus(SoulStatus.IN_BUFFER);

            System.out.println(
                    "🐶 Cerberus: buffer full, rejected " + rejected.getId() +
                            ", inserted " + soul.getId() +
                            " at buffer[" + lastIndex + "]"
            );
        }
    }

    public void printBufferState() {
        System.out.println(buffer);
    }
}
