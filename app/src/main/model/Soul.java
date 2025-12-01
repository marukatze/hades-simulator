package main.model;

import main.utils.SoulState;

public class Soul {
    private int sourceId;
    private String id;
    private final double spawnTime;     // момент появления в мире Аида

    private Double bufferEnterTime;     // когда попала в буфер
    private Double serviceStartTime;    // когда попала на прибор
    private Double serviceFinishTime;   // когда освобождена/переправлена

    private SoulState state;

    public Soul(int sourceId, String id, double spawnTime){
        this.sourceId = sourceId;
        this.id = id;
        this.spawnTime = spawnTime;
        state = SoulState.NEW;
    }

    public String getId() {
        return id;
    }

    public int getSourceId() {
        return sourceId;
    }

    public double getSpawnTime() {
        return spawnTime;
    }

    public SoulState getState() {
        return state;
    }

    public void setState(SoulState newState) {
        this.state = newState;
    }

    public void setBufferEnterTime(double time) {
        this.bufferEnterTime = time;
    }

    public void setServiceStartTime(double time) {
        this.serviceStartTime = time;
    }

    public void setServiceFinishTime(double time) {
        this.serviceFinishTime = time;
    }
}
