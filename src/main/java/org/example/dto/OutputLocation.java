package org.example.dto;

public class OutputLocation {


    private String address;
    private boolean isStarting;
    private Long distanceToNextPoint;
    private Long durationInTrafficInSeconds;
    private Long timeInSeconds;
    private String distanceHumanReadable;
    private String timeHumanReadable;

    public void setStarting(boolean starting) {
        isStarting = starting;
    }

    public Long getDurationInTrafficInSeconds() {
        return durationInTrafficInSeconds;
    }

    public void setDurationInTrafficInSeconds(Long durationInTrafficInSeconds) {
        this.durationInTrafficInSeconds = durationInTrafficInSeconds;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isStarting() {
        return isStarting;
    }

    public void setIsStarting(boolean isStarting) {
        this.isStarting = isStarting;
    }

    public Long getDistanceToNextPoint() {
        return distanceToNextPoint;
    }

    public void setDistanceToNextPoint(Long distanceToNextPoint) {
        this.distanceToNextPoint = distanceToNextPoint;
    }

    public Long getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(Long timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
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

    @Override
    public String toString() {
        return "OutputLocation{" +
                "address='" + address + '\'' +
                ", isStarting=" + isStarting +
                ", distanceToNextPoint=" + distanceToNextPoint +
                ", timeInSeconds=" + timeInSeconds +
                ", distanceHumanReadable='" + distanceHumanReadable + '\'' +
                ", timeHumanReadable='" + timeHumanReadable + '\'' +
                '}';
    }
}
