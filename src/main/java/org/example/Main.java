package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Long[][] matrizDistancias = {
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