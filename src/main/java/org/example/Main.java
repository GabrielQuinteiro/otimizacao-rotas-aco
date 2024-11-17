package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Long [][] matrizDistancias = {
                {0L, 1L, 2L, 2L, 4L, 5L, 1L, 2L, 2L, 4L},
                {1L, 0L, 1L, 2L, 4L, 5L, 1L, 2L, 2L, 4L},
                {2L, 1L, 0L, 2L, 3L, 5L, 1L, 2L, 2L, 4L},
                {2L, 2L, 2L, 0L, 2L, 5L, 1L, 2L, 2L, 4L},
                {4L, 4L, 3L, 2L, 0L, 5L, 1L, 2L, 2L, 4L},
                {4L, 4L, 3L, 2L, 4L, 0L, 1L, 2L, 2L, 4L},
                {4L, 4L, 3L, 2L, 7L, 5L, 0L, 2L, 2L, 4L},
                {4L, 4L, 3L, 2L, 8L, 5L, 1L, 0L, 2L, 4L},
                {4L, 4L, 3L, 2L, 9L, 5L, 1L, 2L, 0L, 4L},
                {4L, 4L, 3L, 2L, 6L, 5L, 1L, 2L, 2L, 0L}
        };

        List<String> enderecos = List.of("Endereço A", "Endereço B", "Endereço C", "Endereço D", "Endereço E", "Endereço F", "Endereço G", "Endereço H", "Endereço I", "Endereço J");

        Grafo grafo = new Grafo(matrizDistancias.length);
        grafo.inicializaComMatriz(matrizDistancias, enderecos);

        System.out.println("Endereco do vértice 0: " + grafo.getEndereco(0));
        System.out.println("Custo entre Endereço A e endereço B: " + grafo.getCustoAresta("Endereço A","Endereço B"));

        ACO aco = new ACO(grafo);

        aco.rodar();


    }

}