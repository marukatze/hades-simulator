package main.model;

import java.util.ArrayList;
import java.util.List;

public class Buffer {

    private final int capacity;
    private final List<Soul> slots;
    private int currentSize = 0;  // ✅ добавляем поле для быстрого доступа

    public Buffer(int capacity) {
        this.capacity = capacity;
        this.slots = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            slots.add(null);
        }
    }

    // Проверка, есть ли свободное место
    public boolean hasSpace() {
        return currentSize < capacity;  // ✅ используем поле
    }

    // Текущее количество душ в буфере
    public int getCurrentSize() {
        return currentSize;  // ✅ O(1) вместо O(n)
    }

    // Вставка души в первый свободный слот
    public void addSoul(Soul soul) {
        for (int i = 0; i < capacity; i++) {
            if (slots.get(i) == null) {
                slots.set(i, soul);
                currentSize++;  // ✅ увеличиваем счетчик
                return;
            }
        }
        throw new IllegalStateException("Buffer full! Cerber должен был решить, кто уйдет в отказ.");
    }

    // Вытеснение последней вставленной души (Д1ОО4)
    public Soul evictLastInserted(int lastIndex) {
        Soul rejected = slots.get(lastIndex);
        slots.set(lastIndex, null);
        currentSize--;  // ✅ уменьшаем счетчик (душа уходит в отказ)
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

    // Получить душу по индексу (для Cerberus)
    public Soul getAt(int index) {
        return slots.get(index);
    }

    // Положить душу по индексу (для Cerberus)
    public void setAt(int index, Soul soul) {
        Soul old = slots.get(index);
        slots.set(index, soul);

        // Корректируем currentSize
        if (old == null && soul != null) {
            currentSize++;  // добавили душу
        } else if (old != null && soul == null) {
            currentSize--;  // удалили душу
        }
        // если old != null && soul != null - замена, размер не меняется
    }

    // ✅ Добавляем метод для явного уменьшения счетчика (для Hades)
    public void decrementCurrentSize() {
        if (currentSize > 0) {
            currentSize--;
        }
    }
}