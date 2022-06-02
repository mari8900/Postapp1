package com.example.postapp.classes;

public class ParcelInfo {
    String postalOffice;
    int trackingNb;

    public ParcelInfo() {

    }

    public ParcelInfo(String postalOffice, int trackingNb) {
        this.postalOffice = postalOffice;
        this.trackingNb = trackingNb;
    }

    public String getPostalOffice() {
        return postalOffice;
    }

    public void setPostalOffice(String postalOffice) {
        this.postalOffice = postalOffice;
    }

    public int getTrackingNb() {
        return trackingNb;
    }

    public void setTrackingNb(int trackingNb) {
        this.trackingNb = trackingNb;
    }
}
