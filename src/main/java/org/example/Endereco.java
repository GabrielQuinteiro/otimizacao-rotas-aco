package org.example;

public class Endereco {
    private String address;
    private String distanceHumanReadable;
    private String timeHumanReadable;
    private boolean isStarting;
    private long distanceToNextPoint;
    private long timeInSeconds;

    public Endereco(String address, String distanceHumanReadable, String timeHumanReadable, boolean isStarting, long distanceToNextPoint, long timeInSeconds) {
        this.address = address;
        this.distanceHumanReadable = distanceHumanReadable;
        this.timeHumanReadable = timeHumanReadable;
        this.isStarting = isStarting;
        this.distanceToNextPoint = distanceToNextPoint;
        this.timeInSeconds = timeInSeconds;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistanceHumanReadable() {
        return distanceHumanReadable;
    }

    public void setDistanceHumanReadable(String distanceHumanReadable) {
        this.distanceHumanReadable = distanceHumanReadable;
    }

    public String getTimeHumanReadable() {
        return timeHumanReadable;
    }

    public void setTimeHumanReadable(String timeHumanReadable) {
        this.timeHumanReadable = timeHumanReadable;
    }

    public boolean isStarting() {
        return isStarting;
    }

    public void setStarting(boolean starting) {
        isStarting = starting;
    }

    public long getDistanceToNextPoint() {
        return distanceToNextPoint;
    }

    public void setDistanceToNextPoint(long distanceToNextPoint) {
        this.distanceToNextPoint = distanceToNextPoint;
    }

    public long getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(long timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }
}
