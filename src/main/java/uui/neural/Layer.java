package uui.neural;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Layer {
    public List<Neuron> neurons;

    public Layer(int numberOfNeurons, int numberOfWeights){
        this.neurons = new ArrayList<>();

        for(int i = 0; i < numberOfNeurons; i++){
            neurons.add( new Neuron(numberOfWeights) );
        }
    }

    public void emulate(Layer inputLayer, boolean sigmoid) {
        for(int i = 0; i < this.neurons.size(); i++){
            Neuron neuron = this.neurons.get(i);
            float value = 0;
            for(int j = 0; j < neuron.weights.size(); j++){
                value += inputLayer.neurons.get(j).value * neuron.weights.get(j);
            }
            value += neuron.bias;
            if(sigmoid)
                neuron.value = (float) (1 / (1 + Math.exp(-value)));
            else
                neuron.value = value;
        }
    }

    public void matrixEmulate(Layer inputLayer, boolean sigmoid){
        double[][] weights = new double[this.neurons.size()][inputLayer.neurons.size()];
        double[] biases = new double[this.neurons.size()];
        double[] values = new double[inputLayer.neurons.size()];

        for(int i = 0; i < this.neurons.size(); i++){
            Neuron neuron = this.neurons.get(i);
            for(int j = 0; j < neuron.weights.size(); j++){
                weights[i][j] = neuron.weights.get(j);
            }
            biases[i] = neuron.bias;
        }

        for(int i = 0; i < inputLayer.neurons.size(); i++){
            values[i] = inputLayer.neurons.get(i).value;
        }

        RealMatrix weightsMatrix = MatrixUtils.createRealMatrix(weights);
        RealMatrix valuesMatrix = MatrixUtils.createColumnRealMatrix(values);
        RealMatrix biasesMatrix = MatrixUtils.createColumnRealMatrix(biases);

        RealMatrix weightedSumMatrix = (weightsMatrix.multiply(valuesMatrix)).add(biasesMatrix);

        for (int i = 0; i < this.neurons.size(); i++) {
            if(sigmoid)
                this.neurons.get( i ).value = (float) (1 / (1 + Math.exp(-weightedSumMatrix.getEntry( i, 0 ))));
            else
                this.neurons.get( i ).value = (float) weightedSumMatrix.getEntry( i, 0 );
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(Neuron neuron : neurons){
            stringBuilder.append( neuron.toString() );
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Layer layer)) return false;
        return Objects.equals( neurons, layer.neurons );
    }

    @Override
    public int hashCode() {
        return Objects.hash( neurons );
    }

}
