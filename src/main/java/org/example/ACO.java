package org.example;

import java.util.*;

class ACO {
    private final Grafo grafo;
    private final int numFormigas;
    private final double alfa = 0.5; // influencia feromonio
    private final double beta = 0.5; // influencia distancia
    private final int epochs = 3;
    private final double taxaEvaporacao = 0.5; // rho
    private final List<Formiga> formigas;

    public ACO(Grafo grafo) {
        this.grafo = grafo;
        this.numFormigas = grafo.getQtdVertices();
        this.formigas = new ArrayList<>();

        inicializarFormigas();

        double custoGuloso = calculaCustoGuloso();
        grafo.inicializarFeromonio(custoGuloso);
    }

    // Cria as formigas colocando cada uma em uma cidade
    private void inicializarFormigas() {
//        List<String> listaCidades = new ArrayList<>(grafo.getEnderecos());
//
//        for (int i = 0; i < numFormigas; i++) {
//            String cidadeFormiga = listaCidades.remove((int) (Math.random() * listaCidades.size()));
//            formigas.add(new Formiga(cidadeFormiga));
//            if (listaCidades.isEmpty()) {
//                listaCidades.addAll(grafo.getEnderecos());
//            }
//        }
        String pontoPartida = grafo.getEndereco(0);
        for (int k = 0; k < numFormigas; k++) {
            formigas.add(new Formiga(pontoPartida));
        }
    }

    private double calculaCustoGuloso() {
        double custoGuloso = 0.0;
        String verticeInicial = grafo.getEndereco((int) (Math.random() * grafo.getQtdVertices()));
        String verticeCorrente = verticeInicial;
        Set<String> visitados = new HashSet<>();
        visitados.add(verticeCorrente);

        while (visitados.size() < grafo.getQtdVertices()) {
            String proximoVertice = null;
            double menorCusto = Double.MAX_VALUE;

            for (String vizinho : grafo.getVizinhos(verticeCorrente)) {
                if (!visitados.contains(vizinho)) {
                    double custo = grafo.getCustoAresta(verticeCorrente, vizinho);
                    if (custo < menorCusto) {
                        menorCusto = custo;
                        proximoVertice = vizinho;
                    }
                }
            }
            if (proximoVertice == null)
                break;

            custoGuloso += menorCusto;
            verticeCorrente = proximoVertice;
            visitados.add(verticeCorrente);
        }

        custoGuloso += grafo.getCustoAresta(verticeCorrente, verticeInicial);
        return custoGuloso;
    }

    public double calcularCustoCaminho(List<String> caminho) {
        double custo = 0;
        for (int i = 0; i < caminho.size() - 1; i++) {
            custo+= grafo.getCustoAresta(caminho.get(i), caminho.get(i+1));
        }

        custo += grafo.getCustoAresta(caminho.get(caminho.size() - 1), caminho.get(0));
        return custo;
    }

    private void atualizarFeromonios(List<List<String>> cidadesVisitadas) {
        for (String chaveAresta : grafo.getKeysArestas()) {
            double somatorioFeromonio = 0.0;
            for (int k = 0; k < numFormigas; k++) {
                List<String> caminho = cidadesVisitadas.get(k);
                for (int i = 0; i < caminho.size() - 1; i++) {
                    if (chaveAresta.equals(caminho.get(i) + "-" + caminho.get(i + 1))) {
                        somatorioFeromonio += 1.0 / calcularCustoCaminho(caminho);
                    }
                }
            }
            String[] vertices = chaveAresta.split("-");
            String origem = vertices[0];
            String destino = vertices[1];
            double novoFeromonio = (1.0 - taxaEvaporacao) * grafo.getFeromonioAresta(origem, destino) + somatorioFeromonio;
            grafo.setFeromonioAresta(origem, destino, novoFeromonio);
        }
    }

    private void encontrarMelhorSolucao() {
        List<String> melhorSolucao = null;
        double melhorCusto = Double.MAX_VALUE;
        for (Formiga formiga : formigas) {
            if (formiga.getCustoSolucao() < melhorCusto) {
                melhorCusto = formiga.getCustoSolucao();
                melhorSolucao = formiga.getSolucao();
            }
        }
        System.out.println("Solução final: " + melhorSolucao + " | custo: " + melhorCusto);
    }

    public void rodar() {
        for (int ep = 0; ep < epochs; ep++) {
            List<List<String>> cidadesVisitadas = new ArrayList<>();
            for (Formiga formiga : formigas) {
                List<String> cidades = new ArrayList<>();
                cidades.add(formiga.getCidade());
                cidadesVisitadas.add(cidades);
            }

            for (int k = 0; k < numFormigas; k++) {
                for (int i = 1; i < grafo.getQtdVertices(); i++) {
                    List<String> cidadesNaoVisitadas = new ArrayList<>(grafo.getVizinhos(formigas.get(k).getCidade()));
                    cidadesNaoVisitadas.removeAll(cidadesVisitadas.get(k));

                    double somatorio = 0.0;
                    Map<String, Double> probabilidades = new HashMap<>();

                    for (String cidade : cidadesNaoVisitadas) {
                        double feromonio = grafo.getFeromonioAresta(formigas.get(k).getCidade(), cidade);
                        double distancia = grafo.getCustoAresta(formigas.get(k).getCidade(), cidade);
                        somatorio += Math.pow(feromonio, alfa) * Math.pow(1.0 / distancia, beta);
                    }

                    for (String cidade : cidadesNaoVisitadas) {
                        double feromonio = grafo.getFeromonioAresta(formigas.get(k).getCidade(), cidade);
                        double distancia = grafo.getCustoAresta(formigas.get(k).getCidade(), cidade);
                        double probabilidade = (Math.pow(feromonio, alfa) * Math.pow(1.0 / distancia, beta)) / (somatorio > 0 ? somatorio : 1);
                        probabilidades.put(cidade, probabilidade);
                    }

                    double maxProbabilidade = -1;
                    String cidadeEscolhida = null;
                    for (Map.Entry<String, Double> entry : probabilidades.entrySet()) {
                        if (entry.getValue() > maxProbabilidade) {
                            maxProbabilidade = entry.getValue();
                            cidadeEscolhida = entry.getKey();
                        }
                    }
                    cidadesVisitadas.get(k).add(cidadeEscolhida);
                }
                formigas.get(k).setSolucao(cidadesVisitadas.get(k), calcularCustoCaminho(cidadesVisitadas.get(k)));
            }
            atualizarFeromonios(cidadesVisitadas);
        }
        encontrarMelhorSolucao();
    }
}