package main.model;

import main.utils.SoulState;

import java.util.ArrayList;
import java.util.List;

public class Cerberus {

    private final Buffer buffer;
    private int lastIndex = -1; // индекс последней вставленной души

    public Cerberus(Buffer buffer) {
        this.buffer = buffer;
    }

    // Обработка души, пришедшей из Source
    public void handleArrival(Soul soul) {
        System.out.println("Cerber: Processing soul " + soul.getId());

        if (buffer.hasSpace()) {
            // есть место → вставляем душу
            int insertIndex = (lastIndex + 1) % buffer.getCapacity();
            while (buffer.getSouls().get(insertIndex) != null) {
                insertIndex = (insertIndex + 1) % buffer.getCapacity();
            }
            buffer.getSouls().set(insertIndex, soul);
            lastIndex = insertIndex;
            soul.setState(SoulState.IN_BUFFER);
            System.out.println("Cerber: Soul " + soul.getId() + " inserted into buffer at index " + insertIndex);
        } else {
            // буфер полон → вытесняем последнюю вставленную душу (Д1ОО4)
            Soul rejected = buffer.evictLastInserted(lastIndex);
            rejected.setState(SoulState.REJECTED);
            System.out.println("Cerber: Buffer full! Rejecting soul: " + rejected.getId());

            // вставляем новую душу на место вытесненной
            buffer.getSouls().set(lastIndex, soul);
            soul.setState(SoulState.IN_BUFFER);
            System.out.println("Cerber: Soul " + soul.getId() + " inserted into buffer at index " + lastIndex);
        }
    }

    // Получить текущее состояние буфера
    public void printBufferState() {
        System.out.println(buffer);
    }

}
