package org.example;

import java.util.ArrayList;
import java.util.List;

public class ACOParametros {
    private Grafo grafo;
    private List<Formiga> formigas;
    private double[][] feromonio;
    private double alpha = 0.5; // influencia feromonio
    private double beta = 0.5; // influencia distancia
    private double taxaEvaporacao = 0.5; // rho
    private double intensidadeFeromonio = 1.0; // Q

    public ACOParametros(Grafo grafo, double alpha, double beta, double taxaEvaporacao, double intensidadeFeromonio) {
        this.grafo = grafo;
        this.formigas = new ArrayList<>();
        this.alpha = alpha;
        this.beta = beta;
        this.taxaEvaporacao = taxaEvaporacao;
        this.intensidadeFeromonio = intensidadeFeromonio;

        int qtdFormigas = grafo.getEnderecos().size();

        this.feromonio = new double[qtdFormigas][qtdFormigas];

        inicializaFormigas(qtdFormigas);
    }

    public void inicializaFormigas(int qtdFormigas) {
        for (int i = 0; i < qtdFormigas; i++) {
            formigas.add(new Formiga(grafo));
        }
    }

    public void evaporarFeromonio() {
        for (int i = 0; i < feromonio.length; i++) {
            for (int j = 0; j < feromonio[i].length; j++) {
                feromonio[i][j] *= (1 - taxaEvaporacao);
            }
        }
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
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

    public double getIntensidadeFeromonio() {
        return intensidadeFeromonio;
    }

    public void setIntensidadeFeromonio(double intensidadeFeromonio) {
        this.intensidadeFeromonio = intensidadeFeromonio;
    }
}
