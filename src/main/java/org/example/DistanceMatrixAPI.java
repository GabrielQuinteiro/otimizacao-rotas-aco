package org.example;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;

public class DistanceMatrixAPI {
    private static final String API_KEY = System.getenv("API_KEY");
    private static final Logger logger = LoggerFactory.getLogger(DistanceMatrixAPI.class);

    public static DistanceMatrixResult getDistanceMatrix(String[] enderecos) {

        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new IllegalStateException("API_KEY não configurada no ambiente");
        }

        // AIzaSyAZivE8czymKF9VH7CWYkUFQvc-xyl0HuE

        int size = enderecos.length;
        DistanciaInfo[][] distanciaInfos = new DistanciaInfo[size][size];
        Long[][] matrizDistancias = new Long[size][size];

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();

        try {
            DistanceMatrix result = DistanceMatrixApi.newRequest(context)
                    .origins(enderecos)
                    .destinations(enderecos)
                    .mode(TravelMode.DRIVING)
                    .departureTime(Instant.now())
                    .trafficModel(TrafficModel.BEST_GUESS)
                    .await();

            for (int i = 0; i < size; i++) {
                DistanceMatrixRow row = result.rows[i];
                for (int j = 0; j < size; j++) {
                    DistanceMatrixElement element = row.elements[j];
                    if (element.distance != null && element.durationInTraffic != null) {
                        DistanciaInfo info = new DistanciaInfo();
                        info.distanceInMeters = element.distance.inMeters;
                        info.durationInSeconds = element.duration.inSeconds;
                        info.durationInTrafficInSeconds = element.durationInTraffic.inSeconds;
                        info.distanceHumanReadable = element.distance.humanReadable;
                        info.timeHumanReadable = element.durationInTraffic.humanReadable;
                        distanciaInfos[i][j] = info;

                        // popula matrizDistancias
                        matrizDistancias[i][j] = info.distanceInMeters;
                    } else {
                        System.out.printf("Unable to calculate distance between %s and %s\n", enderecos[i], enderecos[j]);
                        distanciaInfos[i][j] = null;
                        matrizDistancias[i][j] = null;
                    }
                }
            }
        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            context.shutdown();
        }

        return new DistanceMatrixResult(distanciaInfos, matrizDistancias);
    }
}

