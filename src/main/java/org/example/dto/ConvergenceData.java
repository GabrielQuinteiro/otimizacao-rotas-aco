package org.example.dto;

import lombok.Data;

@Data
public class ConvergenceData {
    private int iteration;
    private double bestSolutionCost;

    public ConvergenceData(int iteration, double bestSolutionCost) {
        this.iteration = iteration;
        this.bestSolutionCost = bestSolutionCost;
    }

}
