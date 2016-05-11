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

import utils.Vec2;
import java.util.ArrayList;
import mlalgorithm.NeuronGene;

/**
 *
 * @author lucas
 */
public class Neuron {
    private ArrayList<Link> listInLinks;
    private ArrayList<Link> listOutLinks;
    
    private double output;
    
    private NeuronGene.neuron_type neuronType;
    
    private int neuronId;
    
    private double sigmoidResponse;
    
    private Vec2 pos;
    
    private Vec2 split;
    
    
    public Neuron(){}
    
    public Neuron(Neuron copy){
        this.neuronType = copy.neuronType;
        this.neuronId = copy.neuronId;
        this.sigmoidResponse = copy.sigmoidResponse;
        this.split = copy.split;
        this.listInLinks = copy.listInLinks;
        this.listOutLinks = copy.listOutLinks;
        this.output = copy.output;
        this.pos = copy.pos;
    }
    
    public Neuron(int neuronId,NeuronGene.neuron_type type, double sigResp,Vec2 split){
        this.listInLinks = new ArrayList<>();
        this.listOutLinks = new ArrayList<>();
        this.output = 0;
        this.pos = new Vec2(0,0);
        
        this.neuronType = type;
        this.neuronId = neuronId;
        this.sigmoidResponse = sigResp;
        this.split = split;
    }

    /**
     * @return the listInLinks
     */
    public ArrayList<Link> getListInLinks() {
        return listInLinks;
    }

    /**
     * @param listInLinks the listInLinks to set
     */
    public void setListInLinks(ArrayList<Link> listInLinks) {
        this.listInLinks = listInLinks;
    }

    /**
     * @return the listOutLinks
     */
    public ArrayList<Link> getListOutLinks() {
        return listOutLinks;
    }

    /**
     * @param listOutLinks the listOutLinks to set
     */
    public void setListOutLinks(ArrayList<Link> listOutLinks) {
        this.listOutLinks = listOutLinks;
    }

    /**
     * @return the output
     */
    public double getOutput() {
        return output;
    }

    /**
     * @param output the output to set
     */
    public void setOutput(double output) {
        this.output = output;
    }

    /**
     * @return the neuronType
     */
    public NeuronGene.neuron_type getNeuronType() {
        return neuronType;
    }

    /**
     * @param neuronType the neuronType to set
     */
    public void setNeuronType(NeuronGene.neuron_type neuronType) {
        this.neuronType = neuronType;
    }

    /**
     * @return the neuronId
     */
    public int getNeuronId() {
        return neuronId;
    }

    /**
     * @return the sigmoidResponse
     */
    public double getSigmoidResponse() {
        return sigmoidResponse;
    }

    /**
     * @param sigmoidResponse the sigmoidResponse to set
     */
    public void setSigmoidResponse(double sigmoidResponse) {
        this.sigmoidResponse = sigmoidResponse;
    }

    /**
     * @return the pos
     */
    public Vec2 getPos() {
        return pos;
    }

    /**
     * @param pos the pos to set
     */
    public void setPos(Vec2 pos) {
        this.pos = pos;
    }

    /**
     * @return the split
     */
    public Vec2 getSplit() {
        return split;
    }
}
