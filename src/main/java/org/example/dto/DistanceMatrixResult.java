package org.example.dto;

import org.example.dto.OutputLocation;

public class DistanceMatrixResult {
    public OutputLocation[][] outputLocations;
    public Long[][] matrizDistancias;

    public DistanceMatrixResult(OutputLocation[][] outputLocations, Long[][] matrizDistancias) {
        this.outputLocations = outputLocations;
        this.matrizDistancias = matrizDistancias;
    }

    public OutputLocation[][] getOutputLocations() {
        return outputLocations;
    }

    public void setOutputLocations(OutputLocation[][] outputLocations) {
        this.outputLocations = outputLocations;
    }

    public Long[][] getMatrizDistancias() {
        return matrizDistancias;
    }

    public void setMatrizDistancias(Long[][] matrizDistancias) {
        this.matrizDistancias = matrizDistancias;
    }
}
