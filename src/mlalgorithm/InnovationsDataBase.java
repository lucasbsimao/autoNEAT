/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlalgorithm;

import carlearndriveenn.Vec2;
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
            listInnovations.add(new Innovation(listNeurons.get(i),nextInnovationNum,nextNeuronId++));
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
        for(int i = 0; i < listInnovations.size();i++){
            if(listInnovations.get(i).getInNeuron() == in && 
                    listInnovations.get(i).getOutNeuron() == out && 
                    listInnovations.get(i).getInovationType() == innovType){
                
                return listInnovations.get(i).getId();
            }
        }
        
        return -1;
    }
    
    public int createInnovation(int in, int out, Innovation.innovation_type innovType){
        //MODIFICADO -------------------------------------
        createInnovation(in, out, innovType, NeuronGene.neuron_type.none, new Vec2(-1,-1));
        
        return (nextNeuronId-1);
    }
    
    public int createInnovation(int in, int out, Innovation.innovation_type innovType, NeuronGene.neuron_type neuronType,Vec2 coord){
        Innovation innov = new Innovation(in, out, innovType,nextInnovationNum,neuronType,coord);
        if(innovType == Innovation.innovation_type.neuron){
            innov.setNeuronId(nextNeuronId);
            nextNeuronId++;
        }
        
        listInnovations.add(innov);
        
        nextInnovationNum++;
        
        return (nextNeuronId-1);
    }
    
    public NeuronGene createNeuronFromId(int neuronId){
        NeuronGene neuron = null;
        //ESSE MÉTODO PODE ESTAR ERRADO------------------------------------------------
        for(int i = 0; i < listInnovations.size(); i++){
            if(listInnovations.get(i).getNeuronId() == neuronId){
                neuron = new NeuronGene(listInnovations.get(i).getNeuronId(),
                        NeuronGene.neuron_type.hidden, 
                        false, 
                        0, 
                        listInnovations.get(i).getPosition());
            }
        }
        
        return neuron;
    } 
    
    public int getNeuronIdByInnovId(int innov){
        return listInnovations.get(innov).getNeuronId();
    }
    
    public int nextNumber(){
        return nextInnovationNum;
    }
}
