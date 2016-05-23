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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import mlalgorithm.NeuralNetwork.NeuralNet;
import utils.CreateRandomNumber;
import utils.NeuralNetGraph;

/**
 *
 * @author lucas
 */
public class LearnDriveENN {
    private double crossOverRate;
    private double mutationRate;
    
    private CarProperties carProp;
    private InnovationsDataBase innovationDB;
    
    private ArrayList<Genome> listGenomes;
    private ArrayList<Genome> listBestGenomes;
    private ArrayList<Species> listSpecies;
  
    private ArrayList<NeuralNetGraph> tableSplitNNGraph;
    
    private double bestFitness;
    
    private int neuralNetInputs;
    private int neuralNetOutputs;
    private int numGenerations;
    private int sizePopulation;
    
    private int nextGenomeId;
    private int nextSpeciesId;
    
    private double totalFitAdjusted;
    private double averageFitAdj;
    
    public LearnDriveENN(int sizePop, int numInputs, int numOutputs){
        this.crossOverRate = 0.7;
        this.mutationRate = 0.001;
        
        this.neuralNetInputs = numInputs;
        this.neuralNetOutputs = numOutputs;
        this.sizePopulation = sizePop;
        this.numGenerations = 0;
        
        this. bestFitness = 0;
        this.totalFitAdjusted = 0;
        this.averageFitAdj = 0;
        
        this.nextGenomeId = 0;
        this.nextSpeciesId = 0;
        
        this.listGenomes = new ArrayList<>();
        this.listSpecies = new ArrayList<>();
        this.listBestGenomes = new ArrayList<>();
       
        for(int i = 0; i < sizePop;i++){
            listGenomes.add(new Genome(nextGenomeId,numInputs,numOutputs));
            nextGenomeId++;
        }
        
        Genome gen = new Genome(0, numInputs, numOutputs);
        this.innovationDB = new InnovationsDataBase(gen.getListNeuronGenes(), gen.getListLinkGenes());
        
        tableSplitNNGraph = splitNeuralNetGraph(0, 1, 0);
    }
    
    public ArrayList<NeuralNet> createNeuralNets(){
        ArrayList<NeuralNet> listNets = new ArrayList<>();
        
        for(int i = 0; i < sizePopulation;i++){
            int depth = calculateNetDepthGraph(listGenomes.get(i));
            
            NeuralNet net = listGenomes.get(i).createNeuralNet(depth);
            
            listNets.add(net);
        }
        
        return listNets;
    }
    
    public ArrayList<NeuralNet> epoch(ArrayList<Double> genomesFitness){
        
        nextGenomeId = 0;
        reset();
        
        for(int i = 0; i < listGenomes.size();i++){
            Genome gen = listGenomes.get(i);
            
            gen.setFitness(genomesFitness.get(i));
        }
        sortGenomesByFitness();
        
        if(listGenomes.get(0).getFitness() > this.bestFitness)
            this.bestFitness = listGenomes.get(0).getFitness();
        
        recordBestGenomes();
        
        setSpecieAndSpawn();

        shuffleGenomesById();
        ArrayList<Genome> newPop = new ArrayList<>();
        
        int numSpawned = 0;
        int maxNumSpawned = 150;
        
        Genome offspring = null;
        
        for(int i = 0; i < listSpecies.size();i++){
            Species specie = listSpecies.get(i);
            if(numSpawned < maxNumSpawned){
                int numToSpawn = specie.getNumSpawn();
                
                boolean chooseBest = false;
                
                while(numToSpawn != 0){
                    numToSpawn--;
                    if(!chooseBest){
                        offspring = specie.getLeader();
                        
                        chooseBest = true;
                    }else{
                        if(specie.getGenMembers().size() == 1){
                            offspring = specie.spawn();
                        }else{
                            Genome g1 = specie.spawn();
                            //Pode mudar ----------------------------
                            if(CreateRandomNumber.getRandom().nextDouble() < crossOverRate){
                                Genome g2 = specie.spawn();
                                
                                int numAtt = 8;
                                
                                while(g1.getGenId() == g2.getGenId() && numAtt != 0){
                                    numAtt--;
                                    g2 = specie.spawn();
                                }
                                    
                                
                                if(g1.getGenId() != g2.getGenId()){
                                    offspring = crossOver(g1,g2);
                                }
                            }else{
                                offspring = new Genome(g1);
                            }
                        }
                    
                    
                    
                    if(offspring != null)offspring.setGenId(nextGenomeId);
                    else System.out.println("Offspring appointing to null pointer!");
                    nextGenomeId++;
                    
                    int maxPermittedNeurons = 100;
                    double chanceAddNode = 0.05;
                    double chanceAddLink = 0.09;
                    double chanceRecurrent = 0.07;
                    double chanceMutWeights = 0.3;
                    double chanceRepWeight = 0.1;
                    double maxWeightPerturbation = 0.5;
                    double maxActivationPerturbation = 0.2;
                    double activationMutRate = 0.15;
                    int numTries = 8;
                    
                    if(offspring.getListNeuronGenes().size() < maxPermittedNeurons)
                        offspring.addNeuron(chanceAddNode, innovationDB, numTries);
                    
                    offspring.addLink(chanceAddLink, chanceRecurrent, innovationDB, numTries, numTries);

                    offspring.mutateWeights(chanceMutWeights, chanceRepWeight, maxWeightPerturbation);

                    offspring.mutateActvationResponse(activationMutRate, maxActivationPerturbation);
                    
                    
                    }
                    offspring.sortGenes();
                    
                    newPop.add(offspring);
                    
                    numSpawned++;
                    
                    if(numSpawned == maxNumSpawned)
                        numToSpawn = 0;
                }
            }
        }
        
        shuffleGenomesById();

        if(maxNumSpawned > numSpawned){
            int delta = maxNumSpawned - numSpawned;
            while(delta != 0){

                delta--;
                Genome gen = new Genome(tournamentSelection(this.sizePopulation/5));
                gen.setGenId(nextGenomeId);
                newPop.add(gen);
            }
        }
        
        listGenomes = newPop;
        
        ArrayList<NeuralNet> listNN = new ArrayList<>();
        for(int i = 0; i < listGenomes.size();i++){
            int depth = calculateNetDepthGraph(listGenomes.get(i));
            
            NeuralNet nn = listGenomes.get(i).createNeuralNet(depth);
            
            listNN.add(nn);
        }
        
        numGenerations++;
        
        return listNN;
    }
    
    private Genome crossOver(Genome mom, Genome dad){
        boolean momSelected = true;
        Random rand = CreateRandomNumber.getRandom();
        //System.out.println("CrossOver");
        
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
        
        ArrayList<Integer> listNeuronsId = new ArrayList<>();
        
        LinkGene linkSelected = null;
        
        ArrayList<LinkGene> dadLinks = new ArrayList<>(dad.getListLinkGenes());
        ArrayList<LinkGene> momLinks = new ArrayList<>(mom.getListLinkGenes());
        
        int curMom = 0;
        int curDad = 0;
        while(!(curMom == momLinks.size()) && !(curDad == dadLinks.size())){
            if(curMom == momLinks.size() && curDad != dadLinks.size()){
                if(!momSelected)
                    linkSelected = new LinkGene(dadLinks.get(curDad));
                
                curDad++;
            }else if(curMom != momLinks.size() && curDad == dadLinks.size()){
                if(momSelected)
                    linkSelected = new LinkGene(momLinks.get(curMom));
                
                curMom++;
            }else if(momLinks.get(curMom).getInovationID() < dadLinks.get(curDad).getInovationID()){
                if(momSelected)
                    linkSelected = new LinkGene(momLinks.get(curMom));
                
                curMom++;
            }else if(momLinks.get(curMom).getInovationID() > dadLinks.get(curDad).getInovationID()){
                if(!momSelected)
                    linkSelected = new LinkGene(dadLinks.get(curDad));
                
                curDad++;
            }else if(momLinks.get(curMom).getInovationID() == dadLinks.get(curDad).getInovationID()){
                if(momSelected){
                    linkSelected = new LinkGene(momLinks.get(curMom));
                }else{
                    linkSelected = new LinkGene(dadLinks.get(curDad));
                }
                
                curMom++;
                curDad++;
            }
            
            if(linksOffspring.isEmpty())
                linksOffspring.add(linkSelected);
            else{
                if(linksOffspring.get(linksOffspring.size()-1).getInovationID() != linkSelected.getInovationID())
                    linksOffspring.add(linkSelected);
            }
            
            addNeuronID(linkSelected.getFromNeuron(), listNeuronsId);
            addNeuronID(linkSelected.getToNeuron(), listNeuronsId);
        }
        
//        for(int j = 0; j < listGenomes.size();j++){
//            System.out.println("GenId1:" + listGenomes.get(j).getGenId());
//        }
        
        Collections.sort(listNeuronsId);
        
//        for(int j = 0; j < listGenomes.size();j++){
//            System.out.println("GenId2:" + listGenomes.get(j).getGenId());
//        }
        
        for(int i = 0; i < listNeuronsId.size();i++){
            neuronsOffspring.add(innovationDB.createNeuronFromId(listNeuronsId.get(i)));
        }

        Genome genome = new Genome(nextGenomeId,linksOffspring,neuronsOffspring,neuralNetInputs,neuralNetOutputs);
        nextGenomeId++;
        return genome;
    }
    
    private Genome tournamentSelection(int selection){
        double bestFitnessToun = 0;
        int choosen = 0;
        Random rand = CreateRandomNumber.getRandom();
        
        for(int i = 0; i < selection;i++){
            int tryId = rand.nextInt(listGenomes.size());
            if(listGenomes.get(tryId).getFitness() > bestFitnessToun){
                choosen = tryId;
                bestFitnessToun = listGenomes.get(tryId).getFitness();
            }
        }
        nextGenomeId++;
        
        
        return listGenomes.get(choosen);
    }
    
    private void setSpecieAndSpawn(){
        boolean addSpecie = false;
        double thresholdComp = 0.26;
        
        for(int i = 0; i < listGenomes.size();i++){
            //System.out.println("genID:" + listGenomes.get(i).getGenId());
            for(int j = 0; j < listSpecies.size();j++){
                double compability = listGenomes.get(i).getCompatibilityScore(listSpecies.get(j).getLeader());
                
                if(compability <= thresholdComp){
                    listSpecies.get(j).addMember(listGenomes.get(i));
                    listGenomes.get(i).setSpecieId(listSpecies.get(j).getSpecieId());
                    
                    addSpecie = true;
                    break;
                }
            }
            
            if(!addSpecie)
                listSpecies.add(new Species(listGenomes.get(i),this.nextSpeciesId++));
            
            addSpecie = false;
        }
        
        adjustGenomesFitness();
        
        for(int i = 0; i < listGenomes.size();i++){
            totalFitAdjusted += listGenomes.get(i).getAdjustedSpecieFit();
        }
        
        averageFitAdj = totalFitAdjusted/listGenomes.size();
        
        for(int i = 0; i < listGenomes.size();i++){
            double toSpawn = listGenomes.get(i).getAdjustedSpecieFit()/averageFitAdj;
            
            listGenomes.get(i).setNumAmountSpawn(toSpawn);
        }
        
        for(int i = 0; i < listSpecies.size();i++){
            listSpecies.get(i).calculateAmountSpawn();
        }
    }
    
    private void recordBestGenomes(){
        int numBestRecords = 5;
        
        listBestGenomes.clear();
        
        for(int i = 0; i < numBestRecords;i++){
            listBestGenomes.add(new Genome(listGenomes.get(i)));
        }
    }
    
    private void addNeuronID(int neuronId, ArrayList<Integer> listNeuronsId){
        for(int i = 0; i < listNeuronsId.size();i++){
            if(listNeuronsId.get(i) == neuronId)
                return;
        }
        
        listNeuronsId.add(neuronId);
    }
    
    private void adjustGenomesFitness(){
        for(int i = 0; i < listSpecies.size(); i++){
            listSpecies.get(i).adjustFitness();
        }
    }
    
    private int calculateNetDepthGraph(Genome gen){
        int max = 0;
        
        for(int i = 0; i < gen.getListNeuronGenes().size();i++){
            for(int j = 0; j < tableSplitNNGraph.size();j++){
                NeuralNetGraph graph = tableSplitNNGraph.get(j);
                if(gen.splitNNGraphY(i) == graph.getVal() && graph.getDepth() > max){
                    max = graph.getDepth();
                }
            }
        }
        return max + 2;
    }
    
    private ArrayList<NeuralNetGraph> splitNeuralNetGraph(double low, double high, int depth){
        ArrayList<NeuralNetGraph> table = new ArrayList<>();
        
        double split = high - low;
        
        table.add(new NeuralNetGraph(low+split/2, depth+1));
        
        if(depth > 6){
            return table;
        }else{
            splitNeuralNetGraph(low, low+split/2, depth+1);
            splitNeuralNetGraph(low+split/2, high, depth+1);
            
            return table;
        }
    }
    
    public void reset(){
        averageFitAdj = 0;
        totalFitAdjusted = 0;
        
        int numGenNoImprove = 15;
        
        for(int i = 0; i < listSpecies.size();i++){
            Species sp = listSpecies.get(i);
            sp.exclude();
            
            if(sp.getGensNoImprove() > numGenNoImprove && sp.getLeader().getFitness() < bestFitness){
                listSpecies.remove(sp);
                i--;
            }
        }
        
        for(int i = 0; i < listGenomes.size();i++){
            listGenomes.get(i).setGenNeuralNet(null);
        }
    }
    
    private void sortGenomesByFitness(){
        //Collections.sort(listGenomes,(a, b) -> a.getFitness()> b.getFitness() ? -1 : a.getFitness() < b.getFitness()? 1 : 0);
        for(int i = 0; i < listGenomes.size();i++){
            double curMax = -999999999;
            int idMax = -1;
            if(i+1 == listGenomes.size()) break;
            for(int j = i+1; j < listGenomes.size();j++){
                Genome curComp = listGenomes.get(j);
                if(curMax < curComp.getFitness()){
                    curMax = curComp.getFitness();
                    idMax = j;
                }
            }
            
            Genome temp = new Genome(listGenomes.get(i));
            
            listGenomes.set(i,listGenomes.get(idMax));
            listGenomes.set(idMax,temp);
        }
        
        if(listGenomes.get(0).getFitness() > this.bestFitness){
            this.bestFitness = listGenomes.get(0).getFitness();
        }
        
        recordBestGenomes();
    }
    
    private void shuffleGenomesById(){
        if(listGenomes.isEmpty()) return;
        for(int i = 0; i < listGenomes.size();i++){
            int idMax = 999999;
            if(i+1 == listGenomes.size()) break;
            for(int j = i+1; j < listGenomes.size();j++){
                Genome curComp = listGenomes.get(j);
                if(idMax > curComp.getGenId()){
                    idMax = j;
                }
            }
            
            Genome temp = new Genome(listGenomes.get(i));
            
            listGenomes.set(i,listGenomes.get(idMax));
            listGenomes.set(idMax,temp);
        }
    }
}
