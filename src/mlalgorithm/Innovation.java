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
package mlalgorithm;

import utils.Vec2;

/**
 *
 * @author lucas
 */
public class Innovation<T> {
    public static enum innovation_type {neuron,link,none};
    
    private innovation_type innovationType;
    private int id;
    
    private T neuralElement;
    private int inNeuron;
    private int outNeuron;
    private int neuronId;
    
    private NeuronGene.neuron_type neuronType;
    
    private Vec2 position;
    
    public Innovation(){
        this.innovationType = innovation_type.none;
        this.id = -1;
        this.inNeuron = -1;
        this.outNeuron = -1;
        this.neuronId = -1;
        this.neuronType = NeuronGene.neuron_type.none;
        this.position = new Vec2(0,0);
    }
    
    public Innovation(int in, int out, innovation_type type, int innovationId){
        this.innovationType = type;
        this.id = innovationId;
        this.inNeuron = in;
        this.outNeuron = out;
        this.neuronId = -1;
        this.neuronType = NeuronGene.neuron_type.none;
        this.position = new Vec2(0,0);
    }
            
    public Innovation(NeuronGene neuron, int innovationId, int neuronId){
        this.innovationType = innovation_type.neuron;
        this.id = innovationId;
        this.inNeuron = -1;
        this.outNeuron = -1;
        this.neuronId = neuronId;
        this.neuronType = neuron.getNeuronType();
        this.position = new Vec2(neuron.getCoordinates());
    }
    
    public Innovation(innovation_type type,int id, int inNeuron, int outNeuron,int neuronId,NeuronGene.neuron_type neuronType){
        this.innovationType = type;
        this.id = id;
        this.inNeuron = inNeuron;
        this.outNeuron = outNeuron;
        this.neuronId = neuronId;
        this.neuronType = neuronType;
        this.position = new Vec2(-1,-1);
    }
    
    public Innovation(int in, int out, innovation_type type, int innovationId, NeuronGene.neuron_type neuronType, Vec2 coord){
        this.innovationType = type;
        this.id = innovationId;
        this.inNeuron = in;
        this.outNeuron = out;
        this.neuronId = -1;
        this.neuronType = neuronType;
        this.position = coord;
    }
    
    /**
     * @return the innovationType
     */
    public innovation_type getInovationType() {
        return innovationType;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the inNeuron
     */
    public int getInNeuron() {
        return inNeuron;
    }

    /**
     * @return the outNeuron
     */
    public int getOutNeuron() {
        return outNeuron;
    }

    /**
     * @param neuronId the neuronId to set
     */
    public void setNeuronId(int neuronId) {
        this.neuronId = neuronId;
    }
    
    /**
     * @return the neuronId
     */
    public int getNeuronId() {
        return neuronId;
    }

    /**
     * @return the neuronType
     */
    public NeuronGene.neuron_type getNeuronType() {
        return neuronType;
    }
    
    /**
     * @return the position
     */
    public Vec2 getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Vec2 position) {
        this.position = position;
    }
}
