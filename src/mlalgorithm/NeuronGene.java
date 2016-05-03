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
import carlearndriveenn.*;

/**
 *
 * @author lucas
 */

public class NeuronGene {
    public static enum neuron_type{input,hidden,bias,output,none};
    private int neuronId;
    
    private neuron_type neuronType;
    
    private boolean isRecurent;
    
    private double sigmoidResponse;
    
    private Vec2 coordinates;
    
    public NeuronGene(){
        this.neuronId = -1;
        this.neuronType = neuron_type.none;
        this.isRecurent = false;
        this.sigmoidResponse = -1;
        this.coordinates = new Vec2(-1,-1);
    }
    
    public NeuronGene(int nId, neuron_type nType, boolean isRec, double sigResp, Vec2 coord){
        this.neuronId = nId;
        this.neuronType = nType;
        this.isRecurent = isRec;
        this.sigmoidResponse = sigResp;
        this.coordinates = coord;
    }
    
    public NeuronGene(NeuronGene toCopy){
        this(toCopy.neuronId,toCopy.neuronType,toCopy.isRecurent,toCopy.sigmoidResponse,toCopy.coordinates);
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
    public neuron_type getNeuronType() {
        return neuronType;
    }

    /**
     * @return the isRecurent
     */
    public boolean isIsRecurent() {
        return isRecurent;
    }

    /**
     * @param isRecurent the isRecurent to set
     */
    public void setIsRecurent(boolean isRecurent) {
        this.isRecurent = isRecurent;
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
     * @return the coordinates
     */
    public Vec2 getCoordinates() {
        return coordinates;
    }

    /**
     * @param coordinates the coordinates to set
     */
    public void setCoordinates(Vec2 coordinates) {
        this.coordinates = coordinates;
    }
}
