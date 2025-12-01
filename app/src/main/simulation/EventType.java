package main.simulation;

public enum EventType {
    ARRIVAL,            // приход новой заявки
    SERVICE_START,      // начало обслуживания
    SERVICE_FINISH,     // завершение обслуживания
    REPLACEMENT,        // вытеснение заявки из буфера (D1OO4)
    BUFFER_INSERT       // запись заявки в буфер по кольцу (D1OЗ1)
}
