package uui;

import uui.neural.NeuralNetwork;
import uui.utils.DataSet;

import java.io.IOException;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) throws IOException {
        String testFile = "train.txt";
        DataSet testDataSet = new DataSet(testFile);
        testDataSet.readData();

        NeuralNetwork neuralNetwork = new NeuralNetwork( Arrays.asList(1,5,1) );
        System.out.println(neuralNetwork);

        NeuralNetwork.FlattenNeuralNetwork flattenNeuralNetwork = neuralNetwork.flatten();
        System.out.println(flattenNeuralNetwork);

        NeuralNetwork fromFlatten = flattenNeuralNetwork.convertToNeuralNetwork();
        System.out.println(fromFlatten);

        System.out.println( neuralNetwork.equals(fromFlatten));


    }
}
