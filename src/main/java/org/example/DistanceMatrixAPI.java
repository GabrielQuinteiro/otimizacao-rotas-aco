package org.example;

import com.google.maps.GeoApiContext;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.errors.ApiException;
import com.google.maps.model.TrafficModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DistanceMatrixAPI {
    private static final String API_KEY = System.getenv("API_KEY");
    private static final Logger logger = LoggerFactory.getLogger(DistanceMatrixAPI.class);

    public static ResultadoACO getDistanceMatrixAPI(String[] enderecos) {
        Long[][] matrizDistancia = new Long[enderecos.length][enderecos.length];
        Long[][] matrizDurationTraffic = new Long[enderecos.length][enderecos.length];
        List<Endereco> listaEnderecos = new ArrayList<>();

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();

        try {
            logger.info("Iniciando requisição para a API de distância com " + enderecos.length + " endereços.");
            DistanceMatrix result = DistanceMatrixApi.newRequest(context)
                    .origins(enderecos)
                    .destinations(enderecos)
                    .departureTime(Instant.now())
                    .trafficModel(TrafficModel.BEST_GUESS)
                    .await();
            logger.info("Requisição bem-sucedida.");

            long totalTime = 0;
            long totalDistance = 0;

            for (int i = 0; i < result.rows.length; i++) {
                DistanceMatrixRow row = result.rows[i];
                for (int j = 0; j < row.elements.length; j++) {
                    DistanceMatrixElement element = row.elements[j];
                    if (element.distance != null) {
                        matrizDistancia[i][j] = element.distance.inMeters;
                        totalDistance += element.distance.inMeters;
                    }

                    if (element.durationInTraffic != null) {
                        matrizDurationTraffic[i][j] = element.durationInTraffic.inSeconds;
                        totalTime += element.durationInTraffic.inSeconds;
                    }

                    if (i != j) { // ignora o mesmo enderco para evitar loops desnecessarios
                        String distanceHumanReadable = element.distance != null ? element.distance.humanReadable : "N/A";
                        String timeHumanReadable = element.durationInTraffic != null ? element.durationInTraffic.humanReadable : "N/A";
                        Endereco endereco = new Endereco(
                                enderecos[j],
                                distanceHumanReadable,
                                timeHumanReadable,
                                i == 0,  // considera que o primeiro eh o endereco inicial
                                element.distance != null ? element.distance.inMeters : 0,
                                element.durationInTraffic != null ? element.durationInTraffic.inSeconds : 0
                        );
                        listaEnderecos.add(endereco);
                    }
                }
            }

            return new ResultadoACO(listaEnderecos, totalTime, totalDistance, matrizDistancia);

        } catch (ApiException | InterruptedException | IOException e) {
            logger.error("Erro ao processar matriz de distâncias", e);
        } catch (Exception e) {
            logger.error("Erro inesperado antes de iniciar a requisição: " + e.getMessage(), e);
        } finally {
            context.shutdown();
        }

        return null;
    }
}
