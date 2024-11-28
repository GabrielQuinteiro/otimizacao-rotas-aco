package org.example;

import com.google.gson.Gson;
import spark.Spark;

import java.util.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Gson gson = new Gson();

        Spark.port(4567);

        Spark.post("/run-aco", (request, response) -> {
            response.type("application/json");

            // parse json
            RequestData requestData = gson.fromJson(request.body(), RequestData.class);

            if (requestData == null || requestData.enderecos == null || requestData.alfa == null || requestData.beta == null) {
                response.status(400);
                return gson.toJson(Collections.singletonMap("error", "Invalid input format."));
            }

            String[] enderecos = requestData.enderecos;
            Double alfa = requestData.alfa;
            Double beta = requestData.beta;

            DistanceMatrixResult distanceMatrixResult = DistanceMatrixAPI.getDistanceMatrix(enderecos);

            // recebe a matriz de distancias
            Long[][] matrizDistancias = distanceMatrixResult.matrizDistancias;
            DistanciaInfo[][] distanciaInfos = distanceMatrixResult.distanciaInfos;

            System.out.println(Arrays.deepToString(matrizDistancias));

            Grafo grafo = new Grafo(matrizDistancias.length);
            grafo.inicializaComMatriz(matrizDistancias, List.of(enderecos));

            ACO aco = new ACO(grafo, alfa, beta);
            aco.rodar();
            List<String> melhorSolucao = aco.getMelhorSolucao();

            // cria o mapa de endereços pra indices
            Map<String, Integer> addressToIndex = new HashMap<>();
            for (int i = 0; i < enderecos.length; i++) {
                addressToIndex.put(enderecos[i], i);
            }

            // cria a resposta do json
            RouteResponse routeResponse = new RouteResponse();
            routeResponse.enderecos = new ArrayList<>();

            long totalDistance = 0;
            long totalTime = 0;

            for (int i = 0; i < melhorSolucao.size(); i++) {
                String enderecoAtual = melhorSolucao.get(i);
                String proxEndereco = melhorSolucao.get((i + 1) % melhorSolucao.size()); // assume que endereco final é o endereco inicial

                Integer indiceAtual = addressToIndex.get(enderecoAtual);
                Integer proxIndice = addressToIndex.get(proxEndereco);

                if (indiceAtual == null || proxIndice == null) {
                    System.err.println("Endereco não encontrado no mapeamento: " + enderecoAtual + " ou " + proxEndereco);
                    continue;
                }

                DistanciaInfo info = distanciaInfos[indiceAtual][proxIndice];

                if (info != null) {
                    EnderecoInfo enderecoInfo = new EnderecoInfo();
                    enderecoInfo.address = enderecoAtual;
                    enderecoInfo.isStarting = (i == 0);
                    enderecoInfo.distanceToNextPoint = info.distanceInMeters;
                    enderecoInfo.timeInSeconds = info.durationInTrafficInSeconds;
                    enderecoInfo.distanceHumanReadable = info.distanceHumanReadable;
                    enderecoInfo.timeHumanReadable = info.timeHumanReadable;

                    routeResponse.enderecos.add(enderecoInfo);

                    totalDistance += info.distanceInMeters;
                    totalTime += info.durationInTrafficInSeconds;
                } else {
                    System.err.println("DistanceInfo é null para os indices: " + indiceAtual + ", " + proxIndice);
                }
            }

            routeResponse.totalDistance = totalDistance;
            routeResponse.totalTime = totalTime;
            routeResponse.totalDistanceHumanReadable = formatarDistancia(totalDistance);
            routeResponse.totalTimeHumanReadable = formatarDuracao(totalTime);

            return gson.toJson(routeResponse);
        });
    }

    public static String formatarDuracao(long durationInSeconds) {
        long hours = durationInSeconds / 3600;
        long minutes = (durationInSeconds % 3600) / 60;

        if (hours > 0) {
            return String.format("%d h %d mins", hours, minutes);
        } else if (minutes > 0) {
            return String.format("%d mins", minutes);
        } else {
            return String.format("%d secs", durationInSeconds);
        }
    }

    public static String formatarDistancia(long distanceInMeters) {
        if (distanceInMeters >= 1000) {
            double distanceInKm = distanceInMeters / 1000.0;
            return String.format(Locale.US, "%.1f km", distanceInKm);
        } else {
            return String.format("%d m", distanceInMeters);
        }
    }
}

