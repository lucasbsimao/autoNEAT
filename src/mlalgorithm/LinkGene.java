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
    
    public LinkGene(LinkGene link){
        this.enabled = link.enabled;
        this.fromNeuron = link.fromNeuron;
        this.inovationID = link.inovationID;
        this.recurrent = link .recurrent;
        this.toNeuron = link.toNeuron;
        this.weight = link.weight;
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
