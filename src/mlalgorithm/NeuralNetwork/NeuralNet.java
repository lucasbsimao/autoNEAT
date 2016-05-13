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

import java.util.ArrayList;
import mlalgorithm.NeuronGene;

/**
 *
 * @author lucas
 */
public class NeuralNet {
    private ArrayList<Neuron> listNeurons;
    
    public static enum run_type{snapshot, active}
    
    private int depth;
    
    public NeuralNet(){
        this.listNeurons = new ArrayList<>();
        this.depth = -1;
    }
    
    public NeuralNet(ArrayList<Neuron> listNeurons,int layers){
        this.listNeurons = listNeurons;
        this.depth = layers;
    }
    
    public double sigmoid(double netInput,double response){
        double out = 1/(1+Math.exp(-netInput/response));
        return out;
    }
    
    public ArrayList<Double> feed(ArrayList<Double> inputs, run_type running){
        
        ArrayList<Double> outputs = new ArrayList();
        
        int flushCont = 0;
        
        if(running == run_type.snapshot)
            flushCont = this.depth;
        else
            flushCont = 1;
        
        for(int i = 0; i < flushCont;i++){
            outputs.clear();
            
            int neuronId = 0;
            
            while(listNeurons.get(neuronId).getNeuronType() == NeuronGene.neuron_type.input){
                listNeurons.get(neuronId).setOutput(inputs.get(neuronId));
                neuronId++;
            }
            
            listNeurons.get(neuronId).setOutput(1);
            neuronId++;
            
            while(neuronId < listNeurons.size()){
                Double sum = 0.0;
                
                Neuron neuron = listNeurons.get(neuronId);
                
                for(int l = 0; l < neuron.getListInLinks().size();l++){
                    Link link = neuron.getListInLinks().get(l);
                    sum += link.getNeuronIn().getOutput()*link.getWeight();
                }
                
                neuron.setOutput(sigmoid(sum,neuron.getSigmoidResponse()));
                
                
                
                if(neuron.getNeuronType() == NeuronGene.neuron_type.output){
                    outputs.add(neuron.getOutput());
                    if(outputs.get(outputs.size()-1).isNaN())
                        System.out.println("");
                }
                    
                
                neuronId++;
            }
        }
        
        if(outputs.size() == 1){
            System.out.println("");
        }
        
        if(run_type.snapshot == running){
            for(int i = 0; i < listNeurons.size();i++){
                listNeurons.get(i).setOutput(0);
            }
        }
        
        return outputs;
    }
}
