/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlalgorithm;

import java.util.ArrayList;
import java.util.Random;
import utils.CreateRandomNumber;

/**
 *
 * @author lucas
 */
public class Species {
    private Genome leader;
    
    private ArrayList<Genome> genMembers;
   
    private int specieId;
    private int gensNoImprove;
    
    private int age;
    private int numSpawn;
    
    public Species(Genome first,int id){
        leader = first;
        genMembers = new ArrayList<>();
        genMembers.add(first);
        
        this.specieId = id;
        this.gensNoImprove = 0;
        this.numSpawn = 0;
        this.age = 0;
    }
    
    public void addMember(Genome gen){
        if(gen.getFitness() > getLeader().getFitness()){
            setLeader(gen);
            setGensNoImprove(0);
        }
        
        getGenMembers().add(gen);
    }
    
    public void exclude(){
        getGenMembers().clear();
        setAge(getAge() + 1);
        setGensNoImprove(getGensNoImprove() + 1);
        setNumSpawn(0);
    }
    
    public void calculateAmountSpawn(){
        for(int i = 0; i < this.genMembers.size();i++){
            this.numSpawn += genMembers.get(i).getNumAmountSpawn();
        }
    }
    
    public void adjustFitness(){
        double total = 0;
        
        int youngBonusAgeThreshhold = 10;
        double youngFitnessBonus = 1.3;
        int oldAgeThreshold = 15;
        double oldFitnessPenalty = 0.7;
        
        for(int i = 0; i < getGenMembers().size();i++){
            double fitness = getGenMembers().get(i).getFitness();
            
            if(getAge() < youngBonusAgeThreshhold)
                fitness *= youngFitnessBonus;
            
            if(getAge() > oldAgeThreshold)
                fitness *= oldFitnessPenalty;
            
            total += fitness;
            
            double adjFit = fitness/(double)getGenMembers().size();
            
            getGenMembers().get(i).setAdjustedSpecieFit(adjFit);
        }
    }
    
    public Genome spawn(){
        Genome offspring = null;
        double chanceSurvive = 0.2;
        
        if(genMembers.size() == 1){
            offspring = genMembers.get(0);
        }else{  
            Double max = (chanceSurvive*genMembers.size())+1;
            int maxSurviveId = max.intValue();
            int id = CreateRandomNumber.getRandom().nextInt(maxSurviveId);
            
            offspring = new Genome(genMembers.get(id));
            
        }
        
        return offspring;
    }

    /**
     * @return the leader
     */
    public Genome getLeader() {
        return leader;
    }

    /**
     * @param leader the leader to set
     */
    public void setLeader(Genome leader) {
        this.leader = leader;
    }

    /**
     * @return the genMembers
     */
    public ArrayList<Genome> getGenMembers() {
        return genMembers;
    }

    /**
     * @param genMembers the genMembers to set
     */
    public void setGenMembers(ArrayList<Genome> genMembers) {
        this.genMembers = genMembers;
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
     * @return the gensNoImprove
     */
    public int getGensNoImprove() {
        return gensNoImprove;
    }

    /**
     * @param gensNoImprove the gensNoImprove to set
     */
    public void setGensNoImprove(int gensNoImprove) {
        this.gensNoImprove = gensNoImprove;
    }

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @return the numSpawn
     */
    public int getNumSpawn() {
        return numSpawn;
    }

    /**
     * @param numSpawn the numSpawn to set
     */
    public void setNumSpawn(int numSpawn) {
        this.numSpawn = numSpawn;
    }
}
