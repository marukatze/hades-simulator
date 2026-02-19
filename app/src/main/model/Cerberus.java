package main.model;

import main.utils.SoulStatus;

public class Cerberus {

    private final Buffer buffer;
    private int lastInsertIndex = -1;

    public Cerberus(Buffer buffer) {
        this.buffer = buffer;
    }

    public Soul handleArrival(Soul soul, double currentTime) {

        int insertIndex = findFreeSlot();

        if (insertIndex != -1) {
            buffer.setAt(insertIndex, soul);
            lastInsertIndex = insertIndex;

            soul.setStatus(SoulStatus.IN_BUFFER);
            soul.setBufferEntryTime(currentTime);
            soul.setBufferIndex(insertIndex);

            return null;
        }

        Soul rejected = buffer.getAt(lastInsertIndex);

        rejected.setStatus(SoulStatus.REJECTED);
        rejected.setRejectionTime(currentTime);
        rejected.setBufferIndex(-1);

        buffer.setAt(lastInsertIndex, soul);

        soul.setStatus(SoulStatus.IN_BUFFER);
        soul.setBufferEntryTime(currentTime);
        soul.setBufferIndex(lastInsertIndex);

        return rejected;
    }

    private int findFreeSlot() {

        int capacity = buffer.getCapacity();
        if (capacity == 0) return -1;

        int start = (lastInsertIndex + 1) % capacity;

        for (int i = 0; i < capacity; i++) {
            int index = (start + i) % capacity;

            if (buffer.getAt(index) == null) {
                return index;
            }
        }
        return -1;
    }
}
