package org.example.dto;

import lombok.Data;
import java.util.List;

import lombok.Data;

@Data
public class OutputData {
    private List<OutputLocation> locations;
    private int totalTime;
    private int totalDistance;
    private String totalTimeHumanReadable;
    private String totalDistanceHumanReadable;
    private List<ConvergenceData> convergenceData;

    public List<OutputLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<OutputLocation> locations) {
        this.locations = locations;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(int totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getTotalTimeHumanReadable() {
        return totalTimeHumanReadable;
    }

    public void setTotalTimeHumanReadable(String totalTimeHumanReadable) {
        this.totalTimeHumanReadable = totalTimeHumanReadable;
    }

    public String getTotalDistanceHumanReadable() {
        return totalDistanceHumanReadable;
    }

    public void setTotalDistanceHumanReadable(String totalDistanceHumanReadable) {
        this.totalDistanceHumanReadable = totalDistanceHumanReadable;
    }

    public List<ConvergenceData> getConvergenceData() {
        return convergenceData;
    }

    public void setConvergenceData(List<ConvergenceData> convergenceData) {
        this.convergenceData = convergenceData;
    }
}
