package ui.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Neuron {

    private static final Random random = new Random();

    public List<Float> weights;

    public float bias;

    public float value;

    public Neuron(int numOfWeights){
        weights = new ArrayList<>();
        for(int i = 0; i < numOfWeights; i++){
            weights.add( (float) random.nextGaussian(0, 0.01) );
        }
        bias = (float) random.nextGaussian(0, 0.01);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("weights=");
        stringBuilder.append(weights);
        stringBuilder.append(", bias=");
        stringBuilder.append(bias);
        stringBuilder.append(", value=");
        stringBuilder.append(value);
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Neuron neuron)) return false;
        return Objects.equals( weights, neuron.weights ) && Objects.equals( bias, neuron.bias ) && Objects.equals( value, neuron.value );
    }

    @Override
    public int hashCode() {
        return Objects.hash( weights, bias, value );
    }
}
