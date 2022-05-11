package com.example.postapp;

import java.util.Date;

public class Appointment {
    private int trackingNumber;
    private String currentPO;
    private Date pickupDate;
    private String pickupHour;

    public Appointment(int trackingNumber, String currentPO, Date pickupDate, String pickupHour) {
        this.trackingNumber = trackingNumber;
        this.currentPO = currentPO;
        this.pickupDate = pickupDate;
        this.pickupHour = pickupHour;
    }

    public int getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(int trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getCurrentPO() {
        return currentPO;
    }

    public void setCurrentPO(String currentPO) {
        this.currentPO = currentPO;
    }

    public Date getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(Date pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getPickupHour() {
        return pickupHour;
    }

    public void setPickupHour(String pickupHour) {
        this.pickupHour = pickupHour;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Appointment{");
        sb.append("trackingNumber=").append(trackingNumber);
        sb.append(", currentPO='").append(currentPO).append('\'');
        sb.append(", pickupDate=").append(pickupDate);
        sb.append(", pickupHour='").append(pickupHour).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
