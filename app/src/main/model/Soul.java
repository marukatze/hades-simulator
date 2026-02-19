package main.model;

import main.utils.SoulStatus;

public class Soul {

    private final String id;
    private final int sourceId;
    private final double arrivalTime;
    private double serviceStartTime = 0.0;
    private double serviceEndTime = 0.0;
    private double serviceTime = 0.0;
    private SoulStatus status;
    private double bufferEntryTime;    // время входа в буфер (для выбора последней)
    private double rejectionTime;      // время отказа (для статистики)
    private int bufferIndex = -1;
    private Charon charon;

    public Soul(String id, int sourceId, double arrivalTime) {
        this.id = id;
        this.sourceId = sourceId;
        this.arrivalTime = arrivalTime;
        this.status = SoulStatus.CREATED;
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

    public double getServiceStartTime() {
        return serviceStartTime;
    }

    public void setCharon(Charon charon) {
        this.charon = charon;
    }

    public Charon getCharon() {
        return charon;
    }

    public void setBufferIndex(int bufferIndex) {
        this.bufferIndex = bufferIndex;
    }

    public int getBufferIndex() {
        return bufferIndex;
    }

    public void setServiceStartTime(double serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }

    public double getServiceEndTime() {
        return serviceEndTime;
    }

    public void setServiceEndTime(double serviceEndTime) {
        this.serviceEndTime = serviceEndTime;
    }

    public double getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(double serviceTime) {
        this.serviceTime = serviceTime;
    }

    public SoulStatus getStatus() {
        return status;
    }

    public void setStatus(SoulStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Soul{" +
                "id='" + id + '\'' +
                ", sourceId=" + sourceId +
                ", arrivalTime=" + arrivalTime +
                ", status=" + status +
                '}';
    }

    public double getBufferEntryTime() {
        return bufferEntryTime;
    }

    public void setBufferEntryTime(double bufferEntryTime) {
        this.bufferEntryTime = bufferEntryTime;
    }

    public double getRejectionTime() {
        return rejectionTime;
    }

    public void setRejectionTime(double rejectionTime) {
        this.rejectionTime = rejectionTime;
    }

    public double getTimeInSystem() {
        if (status == SoulStatus.DONE && serviceEndTime > 0) {
            return serviceEndTime - arrivalTime;
        }
        if (status == SoulStatus.REJECTED && rejectionTime > 0) {
            return rejectionTime - arrivalTime;
        }
        return 0.0;
    }
}