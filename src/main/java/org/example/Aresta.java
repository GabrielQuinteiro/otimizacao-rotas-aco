package org.example;

public class Aresta {
    private final String origem;
    private final String destino;
    private final Long custo;
    private double feromonio;

    public Aresta(String origem, String destino, Long custo) {
        this.origem = origem;
        this.destino = destino;
        this.custo = custo;
    }

    public Long getCusto() {
        return custo;
    }

    public double getFeromonio() {
        return feromonio;
    }

    public void setFeromonio(double feromonio) {
        this.feromonio = feromonio;
    }
}
