/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlalgorithm;

import java.util.ArrayList;
import carlearndriveenn.*;
import java.util.Random;

/**
 *
 * @author lucas
 */
public abstract class Genome {
    private int genId;
    private ArrayList<NeuronGene> listNeuronGenes;
    private ArrayList<LinkGene> listLinkGenes;
    
    private NeuralNet genNeuralNet;
    
    private double fitness;
    private double adjustedSpecieFit;
    private double numAmountSpawn;
    private int numInputs;
    private int numOutputs;
    private int specieId;
    
    public Genome(){
        listNeuronGenes = new ArrayList<>();
        listLinkGenes = new ArrayList<>();
        
        genNeuralNet = new NeuralNet();
        
        fitness = -1;
        adjustedSpecieFit = -1;
        numAmountSpawn = -1;
        numInputs = -1;
        numOutputs = -1;
        specieId = -1;
    }
    
    public abstract void Genome(int id, int inputs, int outputs);
    public abstract void Genome(int id, ArrayList<LinkGene> links, ArrayList<NeuronGene> neurons, int inputs, int outputs);
    public abstract NeuralNet createNeuralNet(int depth);
    
    public void addLink(double mutationRate, double probRecurrent, InnovationsDataBase inovationsDB,int numTrysLoop, int numTrysAddLink){
        Random rand = new Random(System.currentTimeMillis());
        if(rand.nextDouble() > mutationRate) return;
        
        int randNeuronId1 = -1;
        int randNeuronId2 = -1;
        
        boolean recLinkSelected = false;
        
        if(rand.nextDouble() < probRecurrent){
            while(numTrysLoop-- != 0){
                int neuronPos = rand.nextInt(getListNeuronGenes().size() - (getNumInputs()+1)) + getNumInputs()+1;
                NeuronGene.neuron_type neuronType = getListNeuronGenes().get(neuronPos).getNeuronType();
                if(!listNeuronGenes.get(neuronPos).isIsRecurent() && neuronType != NeuronGene.neuron_type.bias && neuronType != NeuronGene.neuron_type.input){
                    randNeuronId1 = getListNeuronGenes().get(neuronPos).getNeuronId();
                    randNeuronId2 = getListNeuronGenes().get(neuronPos).getNeuronId();
                    
                    getListNeuronGenes().get(neuronPos).setIsRecurent(true);
                    recLinkSelected = true;
                    
                    numTrysLoop = 0;
                }
            }
        }else{
            while(numTrysAddLink-- != 0){
                int randNeuronPos = rand.nextInt(getListNeuronGenes().size()-1);
                randNeuronId1 = getListNeuronGenes().get(randNeuronPos).getNeuronId();
                
                randNeuronPos = rand.nextInt(getListNeuronGenes().size() - (getNumInputs()+1)) + getNumInputs()+1;
                randNeuronId2 = getListNeuronGenes().get(randNeuronPos).getNeuronId();
                
                if(randNeuronId2 == 2){
                    continue;
                }
                
                if(!(duplicatedLink(randNeuronId1, randNeuronId2) || randNeuronId1 == randNeuronId2)){
                    break;
                }else{
                    randNeuronId1 = -1;
                    randNeuronId2 = -1;
                }
            }
            
            if(randNeuronId1 == -1 || randNeuronId2 == -1){
                return;
            }
            
            int id = inovationsDB.checkInnovation(randNeuronId1, randNeuronId2, Innovation.innovation_type.link);
            
            if(getListNeuronGenes().get(getNeuronPos(randNeuronId1)).getCoordinates().y > getListNeuronGenes().get(getNeuronPos(randNeuronId2)).getCoordinates().y)
                recLinkSelected = true;
            
            if(id < 0){
                inovationsDB.createInnovation(randNeuronId1,randNeuronId2,Innovation.innovation_type.link);
                
                //MODIFICADO AQUI------------------------------------------
                id = inovationsDB.nextNumber();
            }
            
            LinkGene link = new LinkGene(randNeuronId1,randNeuronId2,id,rand.nextDouble(),true,recLinkSelected);
            getListLinkGenes().add(link);
        }
    }
    
    public void addNeuron(double mutationRate,InnovationsDataBase innovationsDB, int numTrysOldLink){
        Random rand = new Random(System.currentTimeMillis());
        
        if(rand.nextDouble() > mutationRate)
            return;
        
        boolean validLink = false;
        int choosenLinkId = -1;
        
        final int totlHidNeuronsThresrold = getNumInputs() + 5;
        
        if(getListLinkGenes().size() < totlHidNeuronsThresrold){
            while(numTrysOldLink-- == 0){
                choosenLinkId = rand.nextInt(getListLinkGenes().size()-1-(int)Math.sqrt(getListLinkGenes().size()));
                
                int fromNeuron = getListLinkGenes().get(choosenLinkId).getFromNeuron();
                
                if(getListLinkGenes().get(choosenLinkId).isEnabled() &&
                        !listLinkGenes.get(choosenLinkId).isRecurrent() &&
                        getListNeuronGenes().get(getNeuronPos(fromNeuron)).getNeuronType() != NeuronGene.neuron_type.bias){
                    
                    validLink = true;
                    numTrysOldLink = 0;
                }
            }
            
            if(!validLink) return; 
        }else{
            while(!validLink){
                choosenLinkId = rand.nextInt(getListLinkGenes().size());
                
                int fromNeuron = getListLinkGenes().get(choosenLinkId).getFromNeuron();
                
                if(getListLinkGenes().get(choosenLinkId).isEnabled() &&
                        !listLinkGenes.get(choosenLinkId).isRecurrent() &&
                        getListNeuronGenes().get(getNeuronPos(fromNeuron)).getNeuronType() != NeuronGene.neuron_type.bias){
                    
                    validLink = true;
                }
            }
        }
        
        getListLinkGenes().get(choosenLinkId).setEnabled(false);
        
        //MODIFICAR ESTA LINHA, SUBSTITUIR POR SOMATÓRIOS PARA VER A EFICÁCIA ----------------------------
        double origWeight = getListLinkGenes().get(choosenLinkId).getWeight();
        
        int fromId = getListLinkGenes().get(choosenLinkId).getFromNeuron();
        int toId = getListLinkGenes().get(choosenLinkId).getToNeuron();
        
        Vec2 newCoord = getListNeuronGenes().get(fromId).getCoordinates().add(getListNeuronGenes().get(toId).getCoordinates());
        newCoord.divLocal(2);
        
        int id = innovationsDB.checkInnovation(fromId, toId, Innovation.innovation_type.neuron);
        
        int neuronId = innovationsDB.getNeuronIdByInnovId(id);

        if(neuronAlreadyExists(neuronId)) id = -1;
        
        if(id < 0){
            neuronId = innovationsDB.createInnovation(fromId, toId, Innovation.innovation_type.neuron, NeuronGene.neuron_type.hidden, newCoord);
            getListNeuronGenes().add(new NeuronGene(neuronId,NeuronGene.neuron_type.hidden,false,0,newCoord));
            
            int idLink1 = innovationsDB.nextNumber();
            innovationsDB.createInnovation(fromId, neuronId, Innovation.innovation_type.link);
            
            LinkGene link1 = new LinkGene(fromId,neuronId,idLink1,1.0,true,false);
            
            int idLink2 = innovationsDB.nextNumber();
            innovationsDB.createInnovation(neuronId, toId, Innovation.innovation_type.link);
            
            LinkGene link2 = new LinkGene(neuronId,toId,idLink2,origWeight,true,false);
            
            getListLinkGenes().add(link1);
            getListLinkGenes().add(link2);
        }else{
            neuronId = innovationsDB.getNeuronIdByInnovId(id);
            
            int idLink1 = innovationsDB.checkInnovation(fromId, neuronId, Innovation.innovation_type.link);
            int idLink2 = innovationsDB.checkInnovation(neuronId, toId, Innovation.innovation_type.link);
            
            LinkGene link1 = new LinkGene(fromId,neuronId,idLink1,1.0,true,false);
            LinkGene link2 = new LinkGene(neuronId,toId,idLink2,origWeight,true,false);
            
            getListLinkGenes().add(link1);
            getListLinkGenes().add(link2);
            
            getListNeuronGenes().add(new NeuronGene(neuronId,NeuronGene.neuron_type.hidden,false,0,newCoord));
        }
    }
    
    public abstract void mutateWeights(double mutationRate,int propNewMut,double maxPerturbation);
    public abstract void mutateActvationResponse(double mutationRate, double maxPerturbation);
    public abstract double getCompabilityScore(Genome gen);
    public abstract void sortGenes();
    
    public boolean duplicatedLink(int fromNeuron, int toNeuron){
        boolean duplicated = false;
        for(LinkGene link : getListLinkGenes()){
            if(link.getFromNeuron() == fromNeuron && link.getToNeuron() == toNeuron){
                duplicated = true;
                break;
            }
        }
        return duplicated;
    }
    
    public int getNeuronPos(int neuronId){
        int id = -1;
        for(int i = 0; i < getListNeuronGenes().size();i++){
            if(getListNeuronGenes().get(i).getNeuronId() == neuronId){
                id = i;
                break;
            }
        }
        
        return id;
    }
    
    public boolean neuronAlreadyExists(int neuronId){
        if(getNeuronPos(neuronId) == -1)
            return false;
        else
            return true;
    }

    /**
     * @return the fitness
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * @param fitness the fitness to set
     */
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    /**
     * @return the adjustedSpecieFit
     */
    public double getAdjustedSpecieFit() {
        return adjustedSpecieFit;
    }

    /**
     * @param adjustedSpecieFit the adjustedSpecieFit to set
     */
    public void setAdjustedSpecieFit(double adjustedSpecieFit) {
        this.adjustedSpecieFit = adjustedSpecieFit;
    }

    /**
     * @return the numAmountSpawn
     */
    public double getNumAmountSpawn() {
        return numAmountSpawn;
    }

    /**
     * @param numAmountSpawn the numAmountSpawn to set
     */
    public void setNumAmountSpawn(double numAmountSpawn) {
        this.numAmountSpawn = numAmountSpawn;
    }

    /**
     * @return the numInputs
     */
    public int getNumInputs() {
        return numInputs;
    }

    /**
     * @param numInputs the numInputs to set
     */
    public void setNumInputs(int numInputs) {
        this.numInputs = numInputs;
    }

    /**
     * @return the numOutputs
     */
    public int getNumOutputs() {
        return numOutputs;
    }

    /**
     * @param numOutputs the numOutputs to set
     */
    public void setNumOutputs(int numOutputs) {
        this.numOutputs = numOutputs;
    }

    /**
     * @return the specieId
     */
    public int getSpecieId() {
        return specieId;
    }

    /**
     * @param specieId the specieId to set
     */
    public void setSpecieId(int specieId) {
        this.specieId = specieId;
    }

    /**
     * @return the genId
     */
    public int getGenId() {
        return genId;
    }

    /**
     * @param genId the genId to set
     */
    public void setGenId(int genId) {
        this.genId = genId;
    }

    /**
     * @return the listNeuronGenes
     */
    public ArrayList<NeuronGene> getListNeuronGenes() {
        return listNeuronGenes;
    }

    /**
     * @param listNeuronGenes the listNeuronGenes to set
     */
    public void setListNeuronGenes(ArrayList<NeuronGene> listNeuronGenes) {
        this.listNeuronGenes = listNeuronGenes;
    }

    /**
     * @return the listLinkGenes
     */
    public ArrayList<LinkGene> getListLinkGenes() {
        return listLinkGenes;
    }

    /**
     * @param listLinkGenes the listLinkGenes to set
     */
    public void setListLinkGenes(ArrayList<LinkGene> listLinkGenes) {
        this.listLinkGenes = listLinkGenes;
    }

}
