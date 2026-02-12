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

    public Soul(String id, int sourceId, double arrivalTime) {
        this.id = id;
        this.sourceId = sourceId;
        this.arrivalTime = arrivalTime;
        this.status = SoulStatus.CREATED;
    }

    // Перегруженный конструктор для обратной совместимости
    public Soul(int sourceId, double arrivalTime) {
        this(sourceId + "-0", sourceId, arrivalTime);
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

    // В класс Soul.java добавить:
    public double getTimeInQueue() {
        if (serviceStartTime > 0 && bufferEntryTime > 0) {
            return serviceStartTime - bufferEntryTime;
        }
        return 0.0;
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