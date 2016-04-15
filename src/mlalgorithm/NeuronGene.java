/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlalgorithm;

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
