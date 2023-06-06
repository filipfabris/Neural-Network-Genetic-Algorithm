package ui;

import ui.genetic.GeneticAlgorithm;
import ui.utils.DataSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//
//Examples
//
//--train examples/sine_train.txt --test examples/sine_test.txt --nn 5s --popsize 10 --elitism 1 --p 0.1 --K 0.1 --iter 10000
//--train examples/rosenbrock_train.txt --test examples/rosenbrock_test.txt --nn 20s --popsize 20 --elitism 3 --p 0.5 --K 10. --iter 10000
//--train examples/rastrigin_train.txt --test examples/rastrigin_test.txt --nn 5s --popsize 10 --elitism 1 --p 0.3 --K 0.5 --iter 10000
public class Solution {
    public static void main(String[] args) throws IOException {

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

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(trainDataSet, testDataSet, architectureList, populationSize, elitism, mutationRate, mutationStandardDeviation, iterations);

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