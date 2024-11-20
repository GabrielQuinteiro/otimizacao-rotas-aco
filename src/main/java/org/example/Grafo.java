package org.example;

import java.util.*;

public class Grafo {
    private final int qtdVertices;
    private final HashMap<String, Aresta> arestas;
    private final HashMap<String, List<String>> vizinhos;
    private final HashMap<Integer, String> mapaEnderecos;

    public Grafo(int qtdVertices) {
        this.qtdVertices = qtdVertices;
        this.arestas = new HashMap<>();
        this.vizinhos = new HashMap<>();
        this.mapaEnderecos = new HashMap<>();
    }

    public void inicializaComMatriz(Long[][] matrizDistancias, List<String> enderecos) {

        // Verifica se o número de endereços corresponde ao número de vértices
        if (enderecos.size() != qtdVertices) {
            throw new IllegalArgumentException("Número de endereços não corresponde ao tamanho da matriz de distâncias.");
        }

        for (int i = 0; i < qtdVertices; i++) {
            mapaEnderecos.put(i, enderecos.get(i));
        }

        for (int i = 0; i < qtdVertices; i++) {
            for (int j = 0; j < qtdVertices; j++) {
                if (i != j) {
                    addAresta(enderecos.get(i), enderecos.get(j), matrizDistancias[i][j]);
                }
            }
        }
    }

    public void inicializarFeromonio(double custoGuloso) {
        for (String chaveAresta : arestas.keySet()) {
            double feromonio = 1.0 / (qtdVertices * custoGuloso);
            String[] vertices = chaveAresta.split("-");
            String origem = vertices[0];
            String destino = vertices[1];
            setFeromonioAresta(origem, destino, feromonio);
        }
    }

    public void addAresta(String origem, String destino, Long custo) {
        Aresta aresta = new Aresta(origem, destino, custo);
        arestas.put(origem + "-" + destino, aresta);

        vizinhos.putIfAbsent(String.valueOf(origem), new ArrayList<>());
        vizinhos.get(origem).add(destino);
    }

    public Long getCustoAresta(String origem, String destino) {
        return arestas.get(origem + "-" + destino).getCusto();
    }

    public double getFeromonioAresta(String origem, String destino) {
        return arestas.get(origem + "-" + destino).getFeromonio();
    }
    public Set<String> getKeysArestas() {
        return arestas.keySet();
    }

    public void setFeromonioAresta(String origem, String destino, double feromonio) {
        arestas.get(origem + "-" + destino).setFeromonio(feromonio);
    }

    public int getQtdVertices() {
        return qtdVertices;
    }

    public String getEndereco(int indice) {
        return mapaEnderecos.get(indice);
    }

    public Collection<String> getEnderecos() {
        return mapaEnderecos.values();
    }

    public int getIndicePorEndereco(String endereco) {
        for (var entry : mapaEnderecos.entrySet()) {
            if (entry.getValue().equals(endereco)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public List<String> getVizinhos(String vertice) {
        return vizinhos.getOrDefault(vertice, new ArrayList<>());
    }

}
