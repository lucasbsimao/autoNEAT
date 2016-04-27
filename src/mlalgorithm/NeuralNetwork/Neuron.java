/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    
    private double zResponseSum;
    private double output;
    
    private NeuronGene.neuron_type neuronType;
    
    private int neuronId;
    
    private double sigmoidResponse;
    
    private Vec2 pos;
    
    private Vec2 split;
    
    public Neuron(Neuron copy){
        this.neuronType = copy.neuronType;
        this.neuronId = copy.neuronId;
        this.sigmoidResponse = copy.sigmoidResponse;
        this.split = copy.split;
        this.listInLinks = copy.listInLinks;
        this.listOutLinks = copy.listOutLinks;
        this.output = copy.output;
        this.pos = copy.pos;
        this.zResponseSum = copy.zResponseSum;
    }
    
    public Neuron(int neuronId,NeuronGene.neuron_type type, double sigResp,Vec2 split){
        this.listInLinks = new ArrayList<>();
        this.listOutLinks = new ArrayList<>();
        this.output = 0;
        this.pos = new Vec2(0,0);
        this.zResponseSum = 0;
        
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
     * @return the zResponseSum
     */
    public double getZResponseSum() {
        return zResponseSum;
    }

    /**
     * @param zResponseSum the zResponseSum to set
     */
    public void setZResponseSum(double zResponseSum) {
        this.zResponseSum = zResponseSum;
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
