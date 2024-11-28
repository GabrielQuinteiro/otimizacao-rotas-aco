package org.example.dto;

import org.example.model.Location;

import java.util.List;

public class AcoRequest {
    private double alfa;
    private double beta;
    private double taxaEvaporacao;
    private List<Location> locations;

    public double getAlfa() {
        return alfa;
    }

    public void setAlfa(double alfa) {
        this.alfa = alfa;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public double getTaxaEvaporacao() {
        return taxaEvaporacao;
    }

    public void setTaxaEvaporacao(double taxaEvaporacao) {
        this.taxaEvaporacao = taxaEvaporacao;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
