package main.model;

import java.util.ArrayList;
import java.util.List;

public class Buffer {

    private final int capacity;
    private final List<Soul> slots;

    public Buffer(int capacity) {
        this.capacity = capacity;
        this.slots = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            slots.add(null);
        }
    }

    // Проверка, есть ли свободное место
    public boolean hasSpace() {
        return getCurrentSize() < capacity;
    }

    // Текущее количество душ в буфере
    public int getCurrentSize() {
        int count = 0;
        for (Soul s : slots) {
            if (s != null) count++;
        }
        return count;
    }

    // Вставка души в первый свободный слот
    public void addSoul(Soul soul) {
        for (int i = 0; i < capacity; i++) {
            if (slots.get(i) == null) {
                slots.set(i, soul);
                return;
            }
        }
        throw new IllegalStateException("Buffer full! Cerber должен был решить, кто уйдет в отказ.");
    }

    // Вытеснение последней вставленной души (Д1ОО4)
    public Soul evictLastInserted(int lastIndex) {
        Soul rejected = slots.get(lastIndex);
        slots.set(lastIndex, null);
        return rejected;
    }

    // Получить список всех душ (для приборов)
    public List<Soul> getSouls() {
        return new ArrayList<>(slots);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Buffer: ");
        for (Soul s : slots) {
            sb.append(s == null ? "[ ]" : "[" + s.getId() + "]");
        }
        return sb.toString();
    }

    public int getCapacity() {
        return capacity;
    }
}
