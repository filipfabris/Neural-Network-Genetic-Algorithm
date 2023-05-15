package uui.neural;

import uui.utils.DataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NeuralNetwork {

     public static class FlattenNeuralNetwork {
        public List<Integer> arhitecture;
        public List<Float> weights;
        public List<Float> biases;

        public FlattenNeuralNetwork() {
            this.arhitecture = new ArrayList<>();
            this.weights = new ArrayList<>();
            this.biases = new ArrayList<>();
        }
        public NeuralNetwork convertToNeuralNetwork(){
            NeuralNetwork neuralNetwork = new NeuralNetwork();
            int prevLayerNeurons = 0;
            int weightIndex = 0;
            int biasIndex = 0;

            for (Integer integer : this.arhitecture) {
                neuralNetwork.layers.add( new Layer( integer, prevLayerNeurons ) );
                prevLayerNeurons = integer;
            }

            for(Layer layer : neuralNetwork.layers){
                for(Neuron neuron : layer.neurons){
                    for(int i = 0; i < neuron.weights.size(); i++){
                        neuron.weights.set(i, this.weights.get(weightIndex++));
                    }
                    neuron.bias = this.biases.get(biasIndex++);
                }
            }
            return neuralNetwork;
        }

        @Override
        public String toString() {
            return "FlattenNeuralNetwork" + "\n" + "arhitecture: " + arhitecture + "\n" +
                    "weights: " + weights + "\n" + "biases: " + biases + "\n";
        }
    }

    public float squaredError;
    public List<Layer> layers;

    public NeuralNetwork(){
        layers = new ArrayList<>();
    }

    public NeuralNetwork(List<Integer> architecture){
        layers = new ArrayList<>();

        //1,5,1 architecture
        int prevLayerNeurons = 0;
        for(int i = 0; i < architecture.size(); i++){
            layers.add( new Layer( architecture.get(i), prevLayerNeurons ) );
            prevLayerNeurons = architecture.get(i);
        }
    }

    public float emulate(float input){
        //input layer
        Layer inputLayer = layers.get(0);
        inputLayer.neurons.get(0).value = input;

        Layer prevLayer = inputLayer;
        for(int i = 1; i < layers.size(); i++){
            Layer layer = layers.get(i);

            if(i == layers.size() - 1)
                layer.emulate(prevLayer, false);
            else
                layer.emulate(prevLayer, true);

            prevLayer = layer;
        }
        return prevLayer.neurons.get(0).value;
    }

    public void train(DataSet dataSet){
        List<Float> inputs = dataSet.data.keySet().stream().toList();
        List<Float> outputs = dataSet.data.values().stream().toList();

        float sumSquaredError = 0;
        for(int i = 0; i < inputs.size(); i++){
            float input = inputs.get(i);
            float output = outputs.get(i);
            float emulatedOutput = emulate(input);
            float error = output - emulatedOutput;
            sumSquaredError += error * error;
        }
        squaredError = sumSquaredError / inputs.size();
//        System.out.println("Squared error: " + squaredError);
    }


    public FlattenNeuralNetwork flatten(){
        FlattenNeuralNetwork flattenNeuralNetwork = new FlattenNeuralNetwork();
        for(Layer layer : layers){
            flattenNeuralNetwork.arhitecture.add( layer.neurons.size() );
            for(Neuron neuron : layer.neurons){
                for(float weight : neuron.weights){
                    flattenNeuralNetwork.weights.add( weight );
                }
                flattenNeuralNetwork.biases.add( neuron.bias );
            }
        }
        return flattenNeuralNetwork;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int layerIndex = 0;
        for(Layer layer : layers){
            stringBuilder.append( "Layer: " + layerIndex++ + "\n" );
            stringBuilder.append(layer.toString());
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NeuralNetwork that)) return false;
        return Objects.equals( layers, that.layers );
    }

    @Override
    public int hashCode() {
        return Objects.hash( layers );
    }
}



//1,5,1 architecture
//        //input layer
//        Layer inputLayer = layers.get(0);
//        inputLayer.neurons.get(0).value = input;
//
//        //hidden layer
//        Layer hiddenLayer = layers.get(1);
//        hiddenLayer.emulate(inputLayer, true);
//
//        //output layer
//        Layer outputLayer = layers.get(2);
//        outputLayer.emulate(hiddenLayer, false);
//        return outputLayer.neurons.get(0).value;