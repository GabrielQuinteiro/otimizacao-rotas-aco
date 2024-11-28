package org.example.dto;

import org.example.model.Location;
import lombok.Data;
import java.util.List;

@Data
public class InputData {
    private double alfa; // influencia feromonio
    private double beta; // influencia distancia
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
