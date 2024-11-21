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

    //  Cria as formigas colocando no mesmo ponto de partida
    private void inicializarFormigas() {
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

    public Long calcularCustoCaminho(List<String> caminho) {
        Long custo = 0L;
        for (int i = 0; i < caminho.size() - 1; i++) {
            custo+= grafo.getCustoAresta(caminho.get(i), caminho.get(i+1));
        }

        custo += grafo.getCustoAresta(caminho.get(caminho.size() - 1), caminho.get(0));
        return custo;
    }

    private void atualizarFeromonios(List<List<String>> cidadesVisitadas) {
        System.out.println("Atualizando feromônios:");
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
            System.out.println("  Aresta de " + origem + " para " + destino + " tem novo feromônio: " + novoFeromonio);
        }
    }

    private void encontrarMelhorSolucao() {
        List<String> melhorSolucao = null;
        Long melhorCusto = Long.MAX_VALUE;
        for (Formiga formiga : formigas) {
            if (formiga.getCustoSolucao() < melhorCusto) {
                melhorCusto = formiga.getCustoSolucao();
                melhorSolucao = formiga.getSolucao();
            }
        }
        System.out.println("Solução final: " + melhorSolucao + " | custo: " + melhorCusto);
    }

    public void rodar() {
        int maxIteracoes = 100;
        int maxSemMelhoria = maxIteracoes / 3; // numero máximo iterações sem melhoria, definido como 1/3 de maxIteracoes
        int iteracoesSemMelhoria = 0;
        Long melhorCustoGlobal = Long.MAX_VALUE;
        int epochs = 0;

        while (epochs < maxIteracoes && iteracoesSemMelhoria < maxSemMelhoria) {
            System.out.println("Iniciando epoch " + (epochs + 1) + " de " + epochs);
            List<List<String>> cidadesVisitadas = new ArrayList<>();
            for (Formiga formiga : formigas) {
                List<String> cidades = new ArrayList<>();
                String pontoPartida = grafo.getEndereco(0);
                cidades.add(pontoPartida);
                cidadesVisitadas.add(cidades);
            }

            for (int k = 0; k < numFormigas; k++) {
                System.out.println("  Formiga " + (k + 1) + " começando a construir solução");
                for (int i = 1; i < grafo.getQtdVertices(); i++) {
                    System.out.println("    Visitando cidade " + (i + 1));
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

                    // roleta probabilidades
                    double random = Math.random();
                    double acumulado = 0.0;
                    String cidadeEscolhida = null;
                    for (Map.Entry<String, Double> entry : probabilidades.entrySet()) {
                        acumulado += entry.getValue();
                        if (random <= acumulado) {
                            cidadeEscolhida = entry.getKey();
                            break;
                        }
                    }
                    if (cidadeEscolhida != null) {
                        cidadesVisitadas.get(k).add(cidadeEscolhida);
                    }
                }
                formigas.get(k).setSolucao(cidadesVisitadas.get(k), calcularCustoCaminho(cidadesVisitadas.get(k)));
                System.out.println("  Formiga " + (k + 1) + " completou a solução com custo: " + formigas.get(k).getCustoSolucao());
            }
            atualizarFeromonios(cidadesVisitadas);
            // verifica se houve melhoria na melhor solução
            Long melhorCustoIteracao = formigas.stream()
                    .map(Formiga::getCustoSolucao)
                    .min(Long::compare)
                    .orElse(Long.MAX_VALUE);

            if (melhorCustoIteracao < melhorCustoGlobal) {
                melhorCustoGlobal = melhorCustoIteracao;
                iteracoesSemMelhoria = 0; // Zera o contador se houver melhoria
            } else {
                iteracoesSemMelhoria++;
            }

            if (iteracoesSemMelhoria >= maxSemMelhoria) {
                System.out.println("Nenhuma melhoria na solução por " + maxSemMelhoria + " iterações consecutivas. Parando...");
                break;
            }

            epochs++;
        }
        encontrarMelhorSolucao();
    }
}