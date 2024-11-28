package org.example.algorithm;

import org.example.model.Location;
import java.util.List;

public class Graph {
    private List<Location> nodes;
    private Long[][] matrixDistances;
    private double[][] pheromones;

    public Graph(List<Location> nodes, Long[][] matrixDistances) {
        this.nodes = nodes;
        this.matrixDistances = matrixDistances;
        this.pheromones = new double[nodes.size()][nodes.size()];
        initializePheromones();
    }

    private void initializePheromones() {
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.size(); j++) {
                pheromones[i][j] = 1.0;
            }
        }
    }

    public List<Location> getNodes() {
        return nodes;
    }

    public void setNodes(List<Location> nodes) {
        this.nodes = nodes;
    }

    public Long[][] getMatrixDistances() {
        return matrixDistances;
    }

    public void setMatrixDistances(Long[][] matrixDistances) {
        this.matrixDistances = matrixDistances;
    }

    public double[][] getPheromones() {
        return pheromones;
    }

    public void setPheromones(double[][] pheromones) {
        this.pheromones = pheromones;
    }
}

