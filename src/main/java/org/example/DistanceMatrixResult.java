package org.example;

public class DistanceMatrixResult {
    public DistanciaInfo[][] distanciaInfos;
    public Long[][] matrizDistancias;

    public DistanceMatrixResult(DistanciaInfo[][] distanciaInfos, Long[][] matrizDistancias) {
        this.distanciaInfos = distanciaInfos;
        this.matrizDistancias = matrizDistancias;
    }
}
