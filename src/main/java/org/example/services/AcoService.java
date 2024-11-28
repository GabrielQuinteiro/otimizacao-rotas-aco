package org.example.services;

import org.example.algorithm.Ant;
import org.example.algorithm.Graph;
import org.example.dto.*;
import org.example.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AcoService {

    private static final int MAX_ITERATIONS = 100;
    private final int MAX_ITERACOES_SEM_MELHORA = 50;

    @Autowired
    private DistanceMatrixAPI distanceService;

    public OutputData executeACO(InputData inputData) throws Exception {
        System.out.println("Iniciando o algoritmo ACO");

        System.out.println("Parâmetros recebidos: alfa=" + inputData.getAlfa() + ", beta=" + inputData.getBeta() + ", taxaEvaporacao=" + inputData.getTaxaEvaporacao());
        System.out.println("Número de locais recebidos: " + inputData.getLocations().size());

        // 1. Obter lista de endereços
        List<Location> locations = inputData.getLocations();
        List<String> enderecos = locations.stream()
                .map(Location::getEndereco)
                .collect(Collectors.toList());

        System.out.println("Endereços extraídos: " + enderecos);

        // 2. Obter matriz de distâncias
        DistanceMatrixResult distanceMatrixResult = distanceService.getDistanceMatrix(enderecos);
        Long[][] distanceMatrix = distanceMatrixResult.getMatrizDistancias();

        System.out.println("Matriz de distâncias obtida com sucesso");

        // 3. Construção do grafo
        Graph graph = new Graph(locations, distanceMatrix);

        // 4. Inicialização das variáveis
        List<ConvergenceData> convergenceDataList = new ArrayList<>();
        Ant bestAnt = null;
        int iteracoesSemMelhoria = 0;
        double melhorComprimento = Double.MAX_VALUE;

        // 5. Iterações do algoritmo
        for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
            List<Ant> ants = createAnts(graph, inputData);

            for (Ant ant : ants) {
                ant.findTour(graph, inputData.getAlfa(), inputData.getBeta());
            }

            // 6. Atualização dos feromônios
            updatePheromones(graph, ants, inputData.getTaxaEvaporacao());

            // 7. Armazenamento dos dados de convergência
            Ant iterationBestAnt = findBestAnt(ants);
            if (bestAnt == null || iterationBestAnt.getTourLength() < bestAnt.getTourLength()) {
                bestAnt = iterationBestAnt;
            }

            convergenceDataList.add(new ConvergenceData(iteration, bestAnt.getTourLength()));

            if (iterationBestAnt.getTourLength() < melhorComprimento) {
                melhorComprimento = iterationBestAnt.getTourLength();
                iteracoesSemMelhoria = 0;
            } else {
                iteracoesSemMelhoria++;
            }

            if (iteracoesSemMelhoria >= MAX_ITERACOES_SEM_MELHORA) {
                System.out.println("Convergência atingida após " + iteration + " iterações.");
                break;
            }
        }

        System.out.println("Melhor tour encontrado: " + bestAnt.getTour());

        // 8. Preparação do OutputData
        OutputData outputData = prepareOutputData(graph, bestAnt, distanceMatrixResult);

        outputData.setConvergenceData(convergenceDataList);

        return outputData;
    }

    private List<Ant> createAnts(Graph graph, InputData inputData) {
        int numberOfAnts = graph.getNodes().size();
        List<Ant> ants = new ArrayList<>();
        int startNodeIndex = getStartingNodeIndex(graph.getNodes());

        for (int i = 0; i < numberOfAnts; i++) {
            ants.add(new Ant(startNodeIndex));
        }
        return ants;
    }

    private int getStartingNodeIndex(List<Location> locations) {
        for (int i = 0; i < locations.size(); i++) {
            if (locations.get(i).isStarting()) {
                return i;
            }
        }
        throw new IllegalArgumentException("Nenhum ponto de partida definido.");
    }

    private void updatePheromones(Graph graph, List<Ant> ants, double taxaEvaporacao) {
        // Evaporação dos feromônios
        int numNodes = graph.getNodes().size();
        for (int i = 0; i < numNodes; i++) {
            for (int j = 0; j < numNodes; j++) {
                graph.getPheromones()[i][j] *= (1 - taxaEvaporacao);
                if (graph.getPheromones()[i][j] < 0.0001) {
                    graph.getPheromones()[i][j] = 0.0001;
                }
            }
        }

        // Deposição de feromônios pelas formigas
        for (Ant ant : ants) {
            double contribution = 1.0 / ant.getTourLength();
            List<Integer> tour = ant.getTour();

            for (int i = 0; i < tour.size() - 1; i++) {
                int from = tour.get(i);
                int to = tour.get(i + 1);
                graph.getPheromones()[from][to] += contribution;
                // graph.getPheromones()[to][from] += contribution; // grafo direcionado
            }
        }
    }

    private Ant findBestAnt(List<Ant> ants) {
        return ants.stream().min(Comparator.comparingDouble(Ant::getTourLength)).orElse(null);
    }

    private OutputData prepareOutputData(Graph graph, Ant bestAnt, DistanceMatrixResult distanceMatrixResult) {
        OutputData outputData = new OutputData();
        List<OutputLocation> outputLocations = new ArrayList<>();

        List<Integer> bestTour = bestAnt.getTour();
        OutputLocation[][] outputLocationsMatrix = distanceMatrixResult.getOutputLocations();

        int totalDistance = 0;
        int totalTime = 0;

        for (int i = 0; i < bestTour.size() - 1; i++) {
            int currentIndex = bestTour.get(i);
            int nextIndex = bestTour.get(i + 1);

            Location currentLocation = graph.getNodes().get(currentIndex);
            OutputLocation matrixInfo = outputLocationsMatrix[currentIndex][nextIndex];

            OutputLocation outputLocation = new OutputLocation();
            outputLocation.setAddress(currentLocation.getEndereco());
            outputLocation.setIsStarting(currentLocation.isStarting());
            outputLocation.setDistanceToNextPoint(matrixInfo.getDistanceToNextPoint());
            outputLocation.setDurationInTrafficInSeconds(matrixInfo.getDurationInTrafficInSeconds());
            outputLocation.setTimeInSeconds(matrixInfo.getTimeInSeconds());
            outputLocation.setDistanceHumanReadable(matrixInfo.getDistanceHumanReadable());
            outputLocation.setTimeHumanReadable(matrixInfo.getTimeHumanReadable());

            totalDistance += outputLocation.getDistanceToNextPoint();
            totalTime += outputLocation.getTimeInSeconds();

            outputLocations.add(outputLocation);
        }

        // adiciona o último local (retorno ao ponto inicial)
        int lastIndex = bestTour.get(bestTour.size() - 1);
        Location lastLocation = graph.getNodes().get(lastIndex);
        OutputLocation lastOutputLocation = new OutputLocation();
        lastOutputLocation.setAddress(lastLocation.getEndereco());
        lastOutputLocation.setIsStarting(lastLocation.isStarting());
        lastOutputLocation.setDistanceToNextPoint(0L);
        lastOutputLocation.setTimeInSeconds(0L);
        lastOutputLocation.setDistanceHumanReadable("0 km");
        lastOutputLocation.setTimeHumanReadable("0 mins");
        outputLocations.add(lastOutputLocation);

        outputData.setLocations(outputLocations);
        outputData.setTotalDistance(totalDistance);
        outputData.setTotalTime(totalTime);
        outputData.setTotalDistanceHumanReadable(formatDistance(totalDistance));
        outputData.setTotalTimeHumanReadable(formatTime(totalTime));

        return outputData;
    }

    private String formatDistance(int distanceInMeters) {
        double distanceInKm = distanceInMeters / 1000.0;
        return String.format("%.1f km", distanceInKm);
    }

    private String formatTime(int timeInSeconds) {
        int minutes = timeInSeconds / 60;
        return String.format("%d mins", minutes);
    }
}

