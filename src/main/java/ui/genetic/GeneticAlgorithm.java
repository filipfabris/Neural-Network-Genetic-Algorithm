package ui.genetic;

import ui.utils.DataSet;
import ui.neural.Layer;
import ui.neural.NeuralNetwork;
import ui.neural.Neuron;

import java.util.*;

public class GeneticAlgorithm {

    public static Comparator<NeuralNetwork> sortPopulation = Comparator.comparingDouble( nt -> nt.squaredError );
    public static Random random = new Random();

    private List<NeuralNetwork> population;
    private final int populationSize;
    private final int numberOfIterations;
    private final int elitism;
    private final double mutationProbability;
    private final double mutationStandardDeviation;
    private final DataSet trainDataSet;

    private final DataSet testDataSet;

    public GeneticAlgorithm(DataSet trainDataSet, DataSet testDataSet, List<Integer> arhitecture, int populationSize, int elitism, double mutationProbability, double mutationStandardDeviation, int numberOfIterations) {
        this.population = new ArrayList<>();
        this.populationSize = populationSize;
        this.numberOfIterations = numberOfIterations;
        this.elitism = elitism;
        this.mutationProbability = mutationProbability;
        this.mutationStandardDeviation = mutationStandardDeviation;
        this.trainDataSet = trainDataSet;
        this.testDataSet = testDataSet;

        this.initPopulation( arhitecture );
        this.algorithm();
    }

    public void algorithm(){
        NeuralNetwork bestNetworkSolution = null;

        for (int i = 0; i <= numberOfIterations; i++) {

            this.propagate();
            this.population.sort( sortPopulation );

            if(bestNetworkSolution == null || bestNetworkSolution.squaredError > population.get( 0 ).squaredError){
                bestNetworkSolution = population.get( 0 );
            }

            List<NeuralNetwork> newPopulation = new ArrayList<>();
            for (int j = 0; j < elitism; j++) {
                newPopulation.add( population.get( j ) );
            }

            // [Train error @2000]: 0.002106
            if(i % 2000 == 0){
                System.out.println( "[Train error @"+i+"]: " + population.get( 0 ).squaredError );
            }

            //Genes crossover
            while (newPopulation.size() < populationSize) {
                //Proportional selection
                newPopulation.add( this.proportionalSelection() );

                //Best selection
//                newPopulation.add( this.bestSelection( newPopulation ) );
            }

            //Genome mutation
//            this.mutateGenome( newPopulation );

            // Genes mutation
            this.mutateGenes( newPopulation );

            population = newPopulation;
        }

        //[Test error]: 0.000433
        bestNetworkSolution.train( testDataSet );
        System.out.println( "[Test error]: " + bestNetworkSolution.squaredError );

    }

    private void propagate() {
        for (NeuralNetwork neuralNetwork : population) {
            neuralNetwork.train( trainDataSet );
        }
    }

    private NeuralNetwork proportionalSelection(){
        float sum = 0;
        for(NeuralNetwork neuralNetwork: population){
            sum += 1/neuralNetwork.squaredError;
        }

        //First parent
        float randomValue = random.nextFloat(0, sum);
        NeuralNetwork parent1 = null;
        NeuralNetwork parent2 = null;

        float currentSum = 0;
        for(NeuralNetwork neuralNetwork: population){
            currentSum += 1/neuralNetwork.squaredError;
            if(currentSum >= randomValue){
                parent1 = neuralNetwork;
                break;
            }
        }

        //Second parent
        randomValue = random.nextFloat(0, sum);
        currentSum = 0;
        for(NeuralNetwork neuralNetwork: population){
            currentSum += 1/neuralNetwork.squaredError;
            if(currentSum >= randomValue){
                parent2 = neuralNetwork;
                break;
            }
        }

        NeuralNetwork child = this.crossoverGenes( parent1, parent2 );
        return child;
    }

    public NeuralNetwork bestSelection(List<NeuralNetwork> newPopulation){
        NeuralNetwork parent1 = newPopulation.get( random.nextInt( newPopulation.size()) );
        NeuralNetwork parent2 = newPopulation.get( random.nextInt( newPopulation.size()) );

        NeuralNetwork child = this.crossoverGenes( parent1, parent2 );
        return child;
    }

    private void initPopulation(List<Integer> arhitecture){
        for (int i = 0; i < populationSize; i++) {
            population.add( new NeuralNetwork(arhitecture) );
        }
    }

    public NeuralNetwork crossoverGenes(NeuralNetwork parent1, NeuralNetwork parent2){
        NeuralNetwork.FlattenNeuralNetwork flattenParent1 = parent1.flatten();
        NeuralNetwork.FlattenNeuralNetwork flattenParent2 = parent2.flatten();

        //Weight crossover
        for(int i = 0; i < flattenParent1.weights.size(); i++){
            flattenParent1.weights.set( i, (flattenParent1.weights.get( i ) + flattenParent2.weights.get( i )) / 2 );
        }
        //Bias crossover
        for(int i = 0; i < flattenParent1.biases.size(); i++){
            flattenParent1.biases.set( i, (flattenParent1.biases.get( i ) + flattenParent2.biases.get( i )) / 2 );
        }

        return flattenParent1.convertToNeuralNetwork();
    }

    //Mutate genes one by one - weight and bias
    private void mutateGenes(List<NeuralNetwork> newPopulation){
        for (NeuralNetwork neuralNetwork : newPopulation) {
                NeuralNetwork.FlattenNeuralNetwork flattenNeuralNetwork = neuralNetwork.flatten();
                for (int i = 0; i < flattenNeuralNetwork.weights.size(); i++) {
                    if (Math.random() < mutationProbability) {
                        float newWeight = (float) (flattenNeuralNetwork.weights.get( i ) + random.nextGaussian( 0, 0.1 ));
                        flattenNeuralNetwork.weights.set( i, newWeight );
                    }
                }
                for (int i = 0; i < flattenNeuralNetwork.biases.size(); i++) {
                    if (Math.random() < mutationProbability) {
                        float newBias = (float) (flattenNeuralNetwork.biases.get( i ) + random.nextGaussian( 0, mutationStandardDeviation ));
                        flattenNeuralNetwork.biases.set( i, newBias );
                    }
                }

                NeuralNetwork mutatedNetwork = flattenNeuralNetwork.convertToNeuralNetwork();
                neuralNetwork.layers = mutatedNetwork.layers;   //new reference to layers from mutated network
        }
    }

    //Mutate whole genome - layer
    private void mutateGenome(List<NeuralNetwork> newPopulation){
        for(NeuralNetwork neuralNetwork: newPopulation) {
            for (Layer layer : neuralNetwork.layers) {
                if (Math.random() < mutationProbability) {
                    for (Neuron neuron : layer.neurons) {
                        for (int i = 0; i < neuron.weights.size(); i++) {
                            float newWeight = (float) (neuron.weights.get( i ) + random.nextGaussian( 0, mutationStandardDeviation ));
                            neuron.weights.set( i, newWeight );
                        }
                        float bias = (float) (neuron.bias + random.nextGaussian( 0, mutationStandardDeviation ));
                        neuron.bias = bias;
                    }
                }
            }
        }
    }


}
