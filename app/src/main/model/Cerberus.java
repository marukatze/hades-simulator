package main.model;

import main.utils.EventLogger;
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
        // Логируем прибытие (это уже делает Hades, но оставим для контекста)
        EventLogger.logSoulArrival(soul, currentTime);

        // Пытаемся найти свободное место по кольцу
        int insertIndex = findFreeSlot();

        if (insertIndex != -1) {
            // ✅ ЕСТЬ СВОБОДНОЕ МЕСТО - вставляем (Д1ОЗ1)
            buffer.setAt(insertIndex, soul);
            lastInsertIndex = insertIndex;
            soul.setStatus(SoulStatus.IN_BUFFER);
            soul.setBufferEntryTime(currentTime);

            // Логируем вставку
            EventLogger.logCerberusInsert(soul, insertIndex);

        } else {
            // ❌ НЕТ СВОБОДНОГО МЕСТА - вытесняем последнюю поступившую (Д1ОО4)
            Soul rejected = buffer.getAt(lastInsertIndex);

            // Помечаем вытесненную душу как отказанную
            rejected.setStatus(SoulStatus.REJECTED);
            rejected.setRejectionTime(currentTime);

            // Вставляем новую душу на место вытесненной
            buffer.setAt(lastInsertIndex, soul);
            soul.setStatus(SoulStatus.IN_BUFFER);
            soul.setBufferEntryTime(currentTime);

            // Логируем вытеснение
            EventLogger.logCerberusReject(rejected, soul, lastInsertIndex, currentTime);
        }
    }

    /**
     * Ищет свободное место ПО КОЛЬЦУ, начиная с lastInsertIndex + 1
     * @return индекс свободного места или -1, если мест нет
     */
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
        return -1;  // НЕТ СВОБОДНЫХ МЕСТ
    }

    /**
     * Для обратной совместимости - использует findFreeSlot()
     */
    private int findNextFreeSlot() {
        int index = findFreeSlot();
        if (index == -1) {
            throw new IllegalStateException("No free slot but should have space!");
        }
        return index;
    }

    public int getLastInsertIndex() {
        return lastInsertIndex;
    }
}