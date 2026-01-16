package main.model;

import main.utils.SoulStatus;

public class Soul {

    private final long id;
    private final int sourceId;
    private final double arrivalTime;
    private SoulStatus state;

    private static long GLOBAL_ID = 0;

    public Soul(int sourceId, double arrivalTime) {
        this.id = ++GLOBAL_ID;
        this.sourceId = sourceId;
        this.arrivalTime = arrivalTime;
    }

    public String getId() {
        return sourceId + id + "";
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
