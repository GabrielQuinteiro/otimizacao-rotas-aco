package org.example;

import java.util.ArrayList;
import java.util.List;

public class Formiga {
    private final String cidade;
    private List<String> solucao;
    private Double custo;

    public Formiga(String cidade) {
        this.cidade = cidade;
        this.solucao = new ArrayList<>();
        this.custo = null;
    }

    public String getCidade() {
        return cidade;
    }

    public List<String> getSolucao() {
        return solucao;
    }

    public void setSolucao(List<String> solucao, double custo) {
        if (this.custo == null || custo < this.custo) {
            this.solucao = new ArrayList<>(solucao);
            this.custo = custo;
        }
    }

    public Double getCustoSolucao() {
        return custo;
    }
}
