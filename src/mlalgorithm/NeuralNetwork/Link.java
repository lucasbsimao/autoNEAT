/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlalgorithm.NeuralNetwork;

/**
 *
 * @author lucas
 */
public class Link {
    private Neuron neuronIn;
    private Neuron neuronOut;
    
    private double weight;
    
    private boolean isRecurrent;
    
    public Link(){
        this.neuronIn = null;
        this.neuronOut = null;
        this.weight = 0;
        this.isRecurrent = false;
    }
    
    public Link(Neuron in, Neuron out, double w, boolean rec){
        this.neuronIn = in;
        this.neuronOut = out;
        this.weight = w;
        this.isRecurrent = rec;
    }

    /**
     * @return the neuronIn
     */
    public Neuron getNeuronIn() {
        return neuronIn;
    }

    /**
     * @param neuronIn the neuronIn to set
     */
    public void setNeuronIn(Neuron neuronIn) {
        this.neuronIn = neuronIn;
    }

    /**
     * @return the neuronOut
     */
    public Neuron getNeuronOut() {
        return neuronOut;
    }

    /**
     * @param neuronOut the neuronOut to set
     */
    public void setNeuronOut(Neuron neuronOut) {
        this.neuronOut = neuronOut;
    }

    /**
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * @return the isRecurrent
     */
    public boolean isRecurrent() {
        return isRecurrent;
    }

    /**
     * @param isRecurrent the isRecurrent to set
     */
    public void setIsRecurrent(boolean isRecurrent) {
        this.isRecurrent = isRecurrent;
    }
}
