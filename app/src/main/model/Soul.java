package main.model;

import main.utils.SoulState;

public class Soul {
    private int sourceId;
    private int id;
    private final double spawnTime;     // момент появления в мире Аида

    private Double bufferEnterTime;     // когда попала в буфер
    private Double serviceStartTime;    // когда попала на прибор
    private Double serviceFinishTime;   // когда освобождена/переправлена

    private SoulState state;

    public Soul(int sourceIdid, int id, double spawnTime){
        this.sourceId = sourceIdid;
        this.id = id;
        this.spawnTime = spawnTime;
        state = SoulState.NEW;
    }
}
