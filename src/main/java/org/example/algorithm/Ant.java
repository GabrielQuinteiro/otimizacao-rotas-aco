package org.example.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ant {
    private List<Integer> tour;
    private double tourLength;

    public Ant(int startNodeIndex) {
        tour = new ArrayList<>();
        tour.add(startNodeIndex);
        tourLength = 0.0;
    }

    public void findTour(Graph graph, double alfa, double beta) {
        int numberOfNodes = graph.getNodes().size();
        boolean[] visited = new boolean[numberOfNodes];
        int currentNode = tour.get(0);
        visited[currentNode] = true;

        while (tour.size() < numberOfNodes) {
            int nextNode = selectNextNode(graph, currentNode, visited, alfa, beta);
            tour.add(nextNode);
            tourLength += graph.getMatrixDistances()[currentNode][nextNode];
            currentNode = nextNode;
            visited[currentNode] = true;
        }

        // retorna ao ponto de partida, se necessário
        tour.add(tour.get(0));
        tourLength += graph.getMatrixDistances()[currentNode][tour.get(0)];
    }

    private int selectNextNode(Graph graph, int currentNode, boolean[] visited, double alfa, double beta) {
        double[] probabilities = calculateProbabilities(graph, currentNode, visited, alfa, beta);
        double rand = new Random().nextDouble();
        double cumulative = 0.0;

        for (int i = 0; i < probabilities.length; i++) {
            cumulative += probabilities[i];
            if (rand <= cumulative) {
                return i;
            }
        }

        // caso não tenha selecionado (por segurança)
        for (int i = 0; i < visited.length; i++) {
            if (!visited[i]) {
                return i;
            }
        }

        // todos os nós foram visitados
        return -1;
    }

    private double[] calculateProbabilities(Graph graph, int currentNode, boolean[] visited, double alfa, double beta) {
        int numberOfNodes = graph.getNodes().size();
        double[] probabilities = new double[numberOfNodes];
        double sum = 0.0;

        for (int i = 0; i < numberOfNodes; i++) {
            if (!visited[i]) {
                double pheromone = graph.getPheromones()[currentNode][i];
                double distance = graph.getMatrixDistances()[currentNode][i];
                double desirability = 1.0 / distance;
                probabilities[i] = Math.pow(pheromone, alfa) * Math.pow(desirability, beta);
                sum += probabilities[i];
            } else {
                probabilities[i] = 0.0;
            }
        }

        // normaliza as probabilidades
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= sum;
        }

        return probabilities;
    }

    public List<Integer> getTour() {
        return tour;
    }

    public double getTourLength() {
        return tourLength;
    }
}