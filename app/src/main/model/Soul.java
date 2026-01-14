package main.model;

import main.utils.SoulStatus;

public class Soul {

    private final String id;
    private final int sourceId;
    private final double arrivalTime;
    private SoulStatus state;

    public Soul(String id, int sourceId, double arrivalTime) {
        this.id = id;
        this.sourceId = sourceId;
        this.arrivalTime = arrivalTime;
        this.state = SoulStatus.NEW;
    }

    public String getId() {
        return id;
    }

    public int getSourceId() {
        return sourceId;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public SoulStatus getStatus() {
        return state;
    }

    public void setStatus(SoulStatus state) {
        this.state = state;
    }
}
