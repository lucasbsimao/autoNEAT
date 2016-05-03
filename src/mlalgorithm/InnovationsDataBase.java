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
import java.util.ArrayList;

/**
 *
 * @author lucas
 */
public class InnovationsDataBase {
    private ArrayList<Innovation> listInnovations;
    
    private int nextInnovationNum;
    private int nextNeuronId;
    
    public InnovationsDataBase(ArrayList<NeuronGene> listNeurons, ArrayList<LinkGene> listLinks){
        this();
        for(int i = 0;i<listNeurons.size();i++){
            Innovation inn = new Innovation(listNeurons.get(i),nextInnovationNum,nextNeuronId);
            listInnovations.add(inn);
            nextNeuronId++;
            nextInnovationNum++;
        }
        
        for(int i = 0;i<listLinks.size();i++){
            listInnovations.add(new Innovation(listLinks.get(i).getFromNeuron(),
                    listLinks.get(i).getToNeuron(),
                    Innovation.innovation_type.link,
                    nextInnovationNum));
            
            nextInnovationNum++;
            //AQUI PODE DAR PROBLEMA------------------------------------------------
        }
    }
    
    public InnovationsDataBase(){
        listInnovations = new ArrayList<>();
        nextNeuronId = 0;
        nextInnovationNum = 0;
    }
    
    public int checkInnovation(int in, int out, Innovation.innovation_type innovType){
        for(int i = 0; i < getListInnovations().size();i++){
            if(getListInnovations().get(i).getInNeuron() == in && 
                    getListInnovations().get(i).getOutNeuron() == out && 
                    getListInnovations().get(i).getInovationType() == innovType){
                
                return getListInnovations().get(i).getId();
            }
        }
        
        return -1;
    }
    
    public int createInnovation(int in, int out, Innovation.innovation_type innovType){
        //MODIFICADO -------------------------------------
        return createInnovation(in, out, innovType, NeuronGene.neuron_type.none, new Vec2(-1,-1));
    }
    
    public int createInnovation(int in, int out, Innovation.innovation_type innovType, NeuronGene.neuron_type neuronType,Vec2 coord){
        Innovation innov = new Innovation(in, out, innovType,nextInnovationNum,neuronType,coord);
        if(innovType == Innovation.innovation_type.neuron){
            innov.setNeuronId(nextNeuronId);
            nextNeuronId++;
        }
        
        getListInnovations().add(innov);
        
        nextInnovationNum++;
        
        return (nextNeuronId-1);
    }
    
    public NeuronGene createNeuronFromId(int neuronId){
        NeuronGene neuron = null;
        //ESSE MÉTODO PODE ESTAR ERRADO------------------------------------------------
        for(int i = 0; i < getListInnovations().size(); i++){
            if(getListInnovations().get(i).getNeuronId() == neuronId){
                neuron = new NeuronGene(getListInnovations().get(i).getNeuronId(),
                        getListInnovations().get(i).getNeuronType(), 
                        false, 
                        1, 
                        getListInnovations().get(i).getPosition());
                
                return neuron;
            }
        }
        System.out.println("-------------------------------------------------------------------------");
        return neuron;
    } 
    
    public int getNeuronIdByInnovId(int innov){
        return getListInnovations().get(innov).getNeuronId();
    }
    
    public int nextNumber(){
        return nextInnovationNum;
    }

    /**
     * @return the listInnovations
     */
    public ArrayList<Innovation> getListInnovations() {
        return listInnovations;
    }
}
