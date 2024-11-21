package org.example;

import java.util.List;

public class ResultadoACO {
    private List<Endereco> enderecos;
    private long totalTime;
    private long totalDistance;
    private Long[][] matrizDistancias;


    public ResultadoACO(List<Endereco> enderecos, long totalTime, long totalDistance, Long[][] matrizDistancias) {
        this.enderecos = enderecos;
        this.totalTime = totalTime;
        this.totalDistance = totalDistance;
        this.matrizDistancias = matrizDistancias;
    }

    public Long[][] getMatrizDistancias() {
        return matrizDistancias;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public long getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(long totalDistance) {
        this.totalDistance = totalDistance;
    }
}
