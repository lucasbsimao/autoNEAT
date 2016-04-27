/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        
        for(int i = 0; i < flushCont;flushCont++){
            outputs.clear();
            
            int neuronId = 0;
            
            while(listNeurons.get(neuronId).getNeuronType() == NeuronGene.neuron_type.input){
                listNeurons.get(neuronId).setOutput(inputs.get(neuronId));
                neuronId++;
            }
            
            listNeurons.get(neuronId).setOutput(1);
            neuronId++;
            
            while(neuronId < listNeurons.size()){
                double sum = 0;
                
                Neuron neuron = listNeurons.get(neuronId);
                
                for(int l = 0; l < neuron.getListInLinks().size();l++){
                    Link link = neuron.getListInLinks().get(l);
                    sum += link.getNeuronIn().getOutput()*link.getWeight();
                }
                
                neuron.setOutput(sigmoid(sum,neuron.getSigmoidResponse()));
                
                if(neuron.getNeuronType() == NeuronGene.neuron_type.output)
                    outputs.add(neuron.getOutput());
                
                neuronId++;
            }
        }
        
        if(run_type.snapshot == running){
            for(int i = 0; i < listNeurons.size();i++){
                listNeurons.get(i).setOutput(0);
            }
        }
        
        return outputs;
    }
}
