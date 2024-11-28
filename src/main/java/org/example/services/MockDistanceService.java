package org.example.services;

import org.example.model.DistanceMatrixResult;

import java.util.List;
import java.util.Random;

public class MockDistanceService {
    public DistanceMatrixResult getDistanceMatrix(List<String> enderecos) {
        int size = enderecos.size();
        Long[][] distanceMatrix = new Long[size][size];

        Random random = new Random();

        // Preenche a matriz de distâncias
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    distanceMatrix[i][j] = 0L; // distancia de um nó para si mesmo é zero
                } else {
                    distanceMatrix[i][j] = (long) (random.nextInt(10000) + 1); // distancias aleatorias entre 1 e 10000
                }
            }
        }

        DistanceMatrixResult distanceMatrixResult = new DistanceMatrixResult();
        distanceMatrixResult.setMatrizDistancias(distanceMatrix);
        return distanceMatrixResult;
    }
}
