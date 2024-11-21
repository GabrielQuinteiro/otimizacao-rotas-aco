package org.example;

import com.google.gson.Gson;
import spark.Spark;

import java.util.Arrays;
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
            System.out.println(Arrays.toString(enderecos));

            ResultadoACO resultadoACO = DistanceMatrixAPI.getDistanceMatrixAPI(enderecos);

            Long[][] matrizDistancias = resultadoACO.getMatrizDistancias();

            System.out.println(matrizDistancias);

            Grafo grafo = new Grafo(matrizDistancias.length);
            grafo.inicializaComMatriz(matrizDistancias, List.of(enderecos));

            ACO aco = new ACO(grafo);

            aco.rodar();

            if (resultadoACO != null) {
                return gson.toJson(resultadoACO);
            } else {
                response.status(500);
                return gson.toJson("Erro ao calcular a solução.");
            }
        });
    }

}