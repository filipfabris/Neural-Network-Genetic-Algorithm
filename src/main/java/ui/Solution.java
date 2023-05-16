package ui;

import ui.genetic.GeneticAlgorithm;
import ui.utils.DataSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Solution {
    public static void main(String[] args) throws IOException {

//        String testFile = "train.txt";
//        DataSet testDataSet = new DataSet(testFile);
//        testDataSet.readData();
//
//        //Messure time
//        long startTime = System.currentTimeMillis();
//        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(testDataSet, Arrays.asList( 1, 5, 1 ), 10, 1, 0.1, 0.1, 2000);
//        long endTime = System.currentTimeMillis();
//
//        System.out.println("Time: " + (endTime - startTime) + "ms");


        String trainFile = null;
        String testFile = null;
        String architecture = null;
        int populationSize = 0;
        int elitism = 0;
        float mutationRate = 0;
        float mutationStandardDeviation = 0;
        int iterations = 0;

        for(int i = 0; i < args.length; i++){
            switch (args[i]) {
                case "--train" -> trainFile = args[++i];
                case "--test" -> testFile = args[++i];
                case "--nn" -> architecture = args[++i];
                case "--popsize" -> populationSize = Integer.parseInt( args[++i] );
                case "--elitism" -> elitism = Integer.parseInt( args[++i] );
                case "--p" -> mutationRate = Float.parseFloat( args[++i] );
                case "--K" -> mutationStandardDeviation = Float.parseFloat( args[++i] );
                case "--iter" -> iterations = Integer.parseInt( args[++i] );
            }
        }

        //Key is result(y), value is input (x1,x2,...,xn)
        DataSet trainDataSet = new DataSet(trainFile);
        DataSet testDataSet = new DataSet(testFile);

        List<Integer> architectureList = parseArchitecture( architecture, trainDataSet );

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(trainDataSet, architectureList, populationSize, elitism, mutationRate, mutationStandardDeviation, iterations);

    }

    private static List<Integer> parseArchitecture(String architecture, DataSet dataSet){
        List<Integer> architectureList = new ArrayList<>();
        int inputSize = dataSet.data.values().iterator().next().length;
        architectureList.add( inputSize );

        String[] architectureArray = architecture.split( "s" );
        for(String s : architectureArray){
            architectureList.add( Integer.parseInt( s ) );
        }
        architectureList.add( 1 ); //output size is always 1
        return architectureList;
    }


}