package org.example;

import com.google.gson.Gson;
import spark.Spark;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        /* Long[][] matrizDistancias = {
                // A   B   C   D   E   F   G   H   I   J
                {0L, 1L, 2L, 2L, 4L, 4L, 1L, 2L, 2L, 4L}, // A
                {1L, 0L, 1L, 2L, 4L, 4L, 1L, 2L, 2L, 4L}, // B
                {2L, 1L, 0L, 2L, 3L, 3L, 1L, 2L, 2L, 3L}, // C
                {2L, 2L, 2L, 0L, 2L, 2L, 1L, 2L, 2L, 2L}, // D
                {4L, 4L, 3L, 2L, 0L, 4L, 1L, 2L, 2L, 4L}, // E
                {4L, 4L, 3L, 2L, 4L, 0L, 1L, 2L, 2L, 4L}, // F
                {1L, 1L, 1L, 1L, 1L, 1L, 0L, 1L, 1L, 1L}, // G
                {2L, 2L, 2L, 2L, 2L, 2L, 1L, 0L, 2L, 2L}, // H
                {2L, 2L, 2L, 2L, 2L, 2L, 1L, 2L, 0L, 2L}, // I
                {4L, 4L, 3L, 2L, 4L, 4L, 1L, 2L, 2L, 0L}  // J
        }; */

        /* String[] enderecos = {
                "Av. Conselheiro Nébias, 300, Vila Matias, Santos, SP",  // A
                "Av. Conselheiro Nébias, 589, Boqueirão, Santos, SP",  // B
                "Rua Dr. Carvalho de Mendonça, 140, Vila Mathias, Santos, SP",  // C
                "Av. Gen. Francisco Glicério, 642, José Menino, Santos, SP",  // D
        }; */

        Gson gson = new Gson();

        Spark.port(4567);

        Spark.post("/run-aco", (request, response) -> {
            response.type("application/json");

            String[] enderecos = gson.fromJson(request.body(), String[].class);

            Long[][] matrizDistancias = DistanceMatrixAPI.getDistanceMatrix(enderecos);

            Grafo grafo = new Grafo(matrizDistancias.length);
            grafo.inicializaComMatriz(matrizDistancias, List.of(enderecos));

            System.out.println("Endereco do vértice 0: " + grafo.getEndereco(0));
            System.out.println("Custo entre Endereço A e endereço B: " + grafo.getCustoAresta("Av. Conselheiro Nébias, 300, Vila Matias, Santos, SP", "Av. Conselheiro Nébias, 589, Boqueirão, Santos, SP"));

            ACO aco = new ACO(grafo);

            aco.rodar();

            String melhorSolucao = aco.getMelhorSolucao();

            return gson.toJson(melhorSolucao);
        });
    }

}