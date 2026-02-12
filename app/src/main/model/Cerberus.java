package main.model;

import main.utils.SoulStatus;

public class Cerberus {

    private final Buffer buffer;
    private int lastInsertIndex = -1;  // указатель для кольца

    public Cerberus(Buffer buffer) {
        this.buffer = buffer;
    }

    /**
     * Д1ОЗ1 - заполнение буфера ПО КОЛЬЦУ
     * Д1ОО4 - если буфер полон, вытесняем ПОСЛЕДНЮЮ ПОСТУПИВШУЮ
     */
    public void handleArrival(Soul soul, double currentTime) {
        System.out.println("🐶 Cerberus: processing soul " + soul.getId() +
                " at t=" + String.format("%.3f", currentTime));

        // ✅ 1. ПЫТАЕМСЯ НАЙТИ СВОБОДНОЕ МЕСТО
        int insertIndex = findFreeSlot();

        if (insertIndex != -1) {
            // ✅ ЕСТЬ СВОБОДНОЕ МЕСТО - вставляем
            buffer.setAt(insertIndex, soul);
            lastInsertIndex = insertIndex;
            soul.setStatus(SoulStatus.IN_BUFFER);
            soul.setBufferEntryTime(currentTime);
            System.out.println("🐶 Cerberus: soul " + soul.getId() +
                    " inserted at buffer[" + insertIndex + "]");
        } else {
            // ✅ НЕТ СВОБОДНОГО МЕСТА - вытесняем последнюю
            Soul rejected = buffer.getAt(lastInsertIndex);

            rejected.setStatus(SoulStatus.REJECTED);
            rejected.setRejectionTime(currentTime);

            buffer.setAt(lastInsertIndex, soul);
            soul.setStatus(SoulStatus.IN_BUFFER);
            soul.setBufferEntryTime(currentTime);

            System.out.println("🐶 Cerberus: buffer FULL, rejected " + rejected.getId() +
                    ", inserted " + soul.getId() + " at buffer[" + lastInsertIndex + "]");
        }
    }

    /**
     * Ищет свободное место ПО КОЛЬЦУ
     * @return индекс свободного места или -1, если мест нет
     */
    private int findFreeSlot() {
        int capacity = buffer.getCapacity();
        int start = (lastInsertIndex + 1) % capacity;

        for (int i = 0; i < capacity; i++) {
            int index = (start + i) % capacity;
            if (buffer.getAt(index) == null) {
                return index;
            }
        }
        return -1;  // НЕТ СВОБОДНЫХ МЕСТ
    }

    // Удалить findNextFreeSlot() или оставить для совместимости
    private int findNextFreeSlot() {
        int index = findFreeSlot();
        if (index == -1) {
            throw new IllegalStateException("No free slot but hasSpace() = true");
        }
        return index;
    }

    public int getLastInsertIndex() {
        return lastInsertIndex;
    }
}