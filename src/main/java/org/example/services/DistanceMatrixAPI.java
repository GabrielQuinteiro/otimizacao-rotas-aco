package org.example.services;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import org.example.dto.DistanceMatrixResult;
import org.example.dto.OutputLocation;

import java.io.IOException;
import java.time.Instant;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DistanceMatrixAPI {

    private static final String API_KEY = System.getenv("API_KEY");

    public DistanceMatrixResult getDistanceMatrix(List<String> enderecos) throws Exception {

        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new IllegalStateException("API_KEY não configurada no ambiente");
        }

        int size = enderecos.size();
        OutputLocation[][] outputLocations = new OutputLocation[size][size];
        Long[][] matrizDistancias = new Long[size][size];

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();

        try {
            String[] enderecosArray = enderecos.toArray(new String[0]);

            DistanceMatrix result = DistanceMatrixApi.newRequest(context)
                    .origins(enderecosArray)
                    .destinations(enderecosArray)
                    .mode(TravelMode.DRIVING)
                    .departureTime(Instant.now())
                    .trafficModel(TrafficModel.BEST_GUESS)
                    .await();

            for (int i = 0; i < size; i++) {
                DistanceMatrixRow row = result.rows[i];
                for (int j = 0; j < size; j++) {
                    DistanceMatrixElement element = row.elements[j];

                    if (element.distance != null && element.durationInTraffic != null) {
                        OutputLocation info = new OutputLocation();
                        info.setDistanceToNextPoint(element.distance.inMeters);
                        info.setTimeInSeconds(element.duration.inSeconds);
                        info.setDurationInTrafficInSeconds(element.durationInTraffic.inSeconds);
                        info.setDistanceHumanReadable(element.distance.humanReadable);
                        info.setTimeHumanReadable(element.durationInTraffic.humanReadable);
                        outputLocations[i][j] = info;

                        // Popula matrizDistancias
                        matrizDistancias[i][j] = element.distance.inMeters;
                    } else {
                        System.out.printf("Não foi possível calcular a distância entre %s e %s\n", enderecos.get(i), enderecos.get(j));
                        outputLocations[i][j] = null;
                        matrizDistancias[i][j] = null;
                    }
                }
            }
        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
            throw new Exception("Erro ao chamar a API do Google Distance Matrix");
        } finally {
            context.shutdown();
        }

        return new DistanceMatrixResult(outputLocations, matrizDistancias);
    }
}

