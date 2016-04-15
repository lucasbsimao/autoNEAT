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
public class LinkGene {
    private int fromNeuron;
    private int toNeuron;
    private int inovationID;
    
    private double weight;
    
    private boolean enabled;
    private boolean recurrent;
    
    public LinkGene(){
        this.fromNeuron = -1;
        this.toNeuron = -1;
        this.inovationID = -1;
        
        this.weight = -1;
        this.enabled = false;
        this.recurrent = false;
    }
    
    public LinkGene(int from, int to, int iId, double weight, boolean enabled, boolean recurrent){
        this.fromNeuron = from;
        this.toNeuron = to;
        this.inovationID = iId;
        
        this.weight = weight;
        this.enabled = enabled;
        this.recurrent = recurrent;
    }
    
    public boolean compareIdInovation(LinkGene link){
        return (this.getInovationID() < link.getInovationID());
    }

    /**
     * @return the fromNeuron
     */
    public int getFromNeuron() {
        return fromNeuron;
    }

    /**
     * @param fromNeuron the fromNeuron to set
     */
    public void setFromNeuron(int fromNeuron) {
        this.fromNeuron = fromNeuron;
    }

    /**
     * @return the toNeuron
     */
    public int getToNeuron() {
        return toNeuron;
    }

    /**
     * @param toNeuron the toNeuron to set
     */
    public void setToNeuron(int toNeuron) {
        this.toNeuron = toNeuron;
    }

    /**
     * @return the inovationID
     */
    public int getInovationID() {
        return inovationID;
    }

    /**
     * @param inovationID the inovationID to set
     */
    public void setInovationID(int inovationID) {
        this.inovationID = inovationID;
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
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the recurrent
     */
    public boolean isRecurrent() {
        return recurrent;
    }

    /**
     * @param recurrent the recurrent to set
     */
    public void setRecurrent(boolean recurrent) {
        this.recurrent = recurrent;
    }
}
