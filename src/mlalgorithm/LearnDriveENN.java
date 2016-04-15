/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlalgorithm;

import carlearndriveenn.*;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author lucas
 */
public class LearnDriveENN {
    CarProperties carProp;
    
    public LearnDriveENN(CarProperties carProp){
        this.carProp = carProp;
    }
    
    public Genome crossOver(Genome mom, Genome dad){
        boolean momSelected = true;
        Random rand = new Random(System.currentTimeMillis());
        
        if(mom.getFitness() == dad.getFitness()){
            if(mom.getListLinkGenes().size() == dad.getListLinkGenes().size()){
                if(rand.nextInt(1) == 0) momSelected = false;
            }else{
                if(mom.getListLinkGenes().size() > dad.getListLinkGenes().size())
                    momSelected = false;
            }
        }else{
            if(dad.getFitness() > mom.getFitness())
                momSelected = false;
        }
        
        ArrayList<NeuronGene> neuronsOffspring = new ArrayList<>();
        ArrayList<LinkGene> linksOffspring = new ArrayList<>();
        
        ArrayList<Integer> listNeurosId = new ArrayList<>();
        
        LinkGene linkSelected = null;
        
        ArrayList<LinkGene> dadLinks = new ArrayList<>(dad.getListLinkGenes());
        ArrayList<LinkGene> momLinks = new ArrayList<>(mom.getListLinkGenes());
        
        int curMom = 0;
        int curDad = 0;
        while(!(curMom == momLinks.size()-1) && !(curDad == dadLinks.size()-1)){
            if(curMom == momLinks.size()-1 && curDad != dadLinks.size()-1){
                if(!momSelected)
                    linkSelected = dadLinks.get(curDad);
                
                curDad++;
            }else if(curMom != momLinks.size()-1 && curDad == dadLinks.size()-1){
                if(momSelected)
                    linkSelected = momLinks.get(curMom);
                
                curMom++;
            }else if(momLinks.get(curMom).getInovationID() < dadLinks.get(curDad).getInovationID()){
                if(momSelected)
                    linkSelected = momLinks.get(curMom);
                
                curMom++;
            }else if(momLinks.get(curMom).getInovationID() > dadLinks.get(curDad).getInovationID()){
                if(!momSelected)
                    linkSelected = dadLinks.get(curDad);
                
                curDad++;
            }else if(momLinks.get(curMom).getInovationID() == dadLinks.get(curDad).getInovationID()){
                if(rand.nextFloat()< 0.5F){
                    linkSelected = momLinks.get(curMom);
                }else{
                    linkSelected = dadLinks.get(curDad);
                }
                
                curMom++;
                curDad++;
            }
            
            if(linksOffspring.size() == 0)
                linksOffspring.add(linkSelected);
            else{
                if(linksOffspring.get(linksOffspring.size()-1).getInovationID() != linkSelected.getInovationID())
                    linksOffspring.add(linkSelected);
            }
            
            addNeuronID(linkSelected.getFromNeuron(), listNeurosId);
            addNeuronID(linkSelected.getToNeuron(), listNeurosId);   
        }
        
        
        
        return null;
    }
    
    public void addNeuronID(int neuronId, ArrayList<Integer> listNeuronsId){
        for(int i = 0; i < listNeuronsId.size();i++){
            if(listNeuronsId.get(i) == neuronId)
                return;
        }
        
        listNeuronsId.add(neuronId);
    }
    
}
