/*
 * The MIT License
 *
 * Copyright 2016 Lucas Borsatto Simão.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
