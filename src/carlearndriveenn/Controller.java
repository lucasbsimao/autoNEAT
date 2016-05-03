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
package carlearndriveenn;

import java.util.ArrayList;
import mlalgorithm.LearnDriveENN;
import mlalgorithm.NeuralNetwork.NeuralNet;
import utils.Vec2;

/**
 *
 * @author Lucas Borsatto Simão
 */
public class Controller implements Runnable{
    private Physics physics;
    private LearnDriveENN learnDrive;
    private CarProperties carProp;
    
    private ArrayList<NeuralNet> listNets;
    
    private final double GAME_HERTZ = 30.0;
    private final int numGenerations = 250;
    
    public Controller(CarProperties carProp,ArrayList<Vec2> inEdge,ArrayList<Vec2> outEdge,ArrayList<Vec2> midPoints, float roadSize){
        this.physics = new Physics(carProp,inEdge,outEdge,midPoints,roadSize);
        //Sensors + lin velocity + ang velocity
        this.learnDrive = new LearnDriveENN(150, carProp.getSensorsVec().size()+1, 2);
        this.carProp = carProp;
        
        listNets = learnDrive.createNeuralNets();
    }
    
    private NeuralNet runAlgorithm(){
        
        ArrayList<Double> fitness = calculateNetsFitness();
            
        listNets = learnDrive.epoch(fitness);
        
        double maxValue = 0;
        int maxId = 0;
        for(int i = 0; i < fitness.size();i++){
            if(fitness.get(i) > maxValue){
                maxValue = fitness.get(i);
                maxId = i;
            }
        }
        try{
            Thread.sleep(60);
        }catch(Exception ex){
            
        }
        
        return listNets.get(maxId);
    }
    
    private ArrayList<Double> calculateNetsFitness(){
        ArrayList<Double> listFitness = new ArrayList<>();
        double minScore = 999999;
        
        for(int i = 0; i < listNets.size();i++){
            CarProperties carTest = new CarProperties(0,new Vec2(160,60));
            carTest.setCarDimensions(carProp.getWidth(), carProp.getHeight());
            Physics physicsTest = new Physics(carTest, physics.getInEdge(), physics.getOutEdge(), physics.getMidPoints(), physics.getRoadSize());
            
            double sum = 0;
            while(!carTest.isCrashed()){
                Vec2 prevPos = new Vec2(carTest.getPosition());
                
                ArrayList<Double> inputs = new ArrayList<>(carTest.getTaxSensorStages());
                //inputs.add((double)carTest.getLinVelocity().length());
                inputs.add((double)carTest.getAngVelocity());
                ArrayList<Double> outputs = listNets.get(i).feed(inputs, NeuralNet.run_type.active);
                
                carTest.setWheelVelocities(outputs.get(0).floatValue(), outputs.get(1).floatValue());
                
                physicsTest.stepSimulation((float)(1/GAME_HERTZ));
                Vec2 dDistTraveledVec = carTest.getPosition().sub(prevPos);
                double dDistTraveled = dDistTraveledVec.length();
                Vec2 uDirRoad = physicsTest.calculateRoadDirection(carTest.getPosition());
                double angle = uDirRoad.angle(carTest.getFrontVector());
                
                Double test = angle;
                if(test.isNaN()){
                    System.out.println("");
                }
                
                if(!carTest.isCrashed()){
                    sum += dDistTraveled*Math.cos(angle);
                }
                
            }
            //System.out.println("-------------------------------\n\n");
            
            listFitness.add(sum+carTest.getFitness());
            double totalScore = sum+carTest.getFitness();
            
            if(minScore > totalScore)
                minScore = totalScore;
            
        }
        int size = listFitness.size();
        
        ArrayList<Double> listTemp = new ArrayList<>();
        for(int i = 0;i < size;i++){
            double total = listFitness.get(i)+Math.abs(minScore);
            listTemp.add(total);
            //System.out.println("ID(" + i + "):" + listFitness.get(i));
        }
        listFitness = listTemp;
        
        return listFitness;
    }
    
    @Override
    public void run() {
        final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
        double lastUpdateTime = System.nanoTime();
        double lastRenderTime = System.nanoTime();
        final double TARGET_FPS = 60;
        final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;
        
        int actGeneration = 0;
        NeuralNet neuralNet = runAlgorithm();
        while (true)
        {
            double now = System.nanoTime();

            while( now - lastUpdateTime > TIME_BETWEEN_UPDATES)
            {
                if(carProp.isCrashed()){
                    System.out.println("\nPróximo:(" + actGeneration + ")");
                    neuralNet = runAlgorithm();
                    //if(actGeneration >= numGenerations)
                        carProp.setCrashed(false);
                    actGeneration++;
                }else{
                    ArrayList<Double> inputs = new ArrayList<>(carProp.getTaxSensorStages());
                    //inputs.add((double)carProp.getLinVelocity().length());
                    inputs.add((double)carProp.getAngVelocity()/CarProperties.maxAngVelocity);
                    ArrayList<Double> outputs = neuralNet.feed(inputs, NeuralNet.run_type.active);
                    
                    carProp.setWheelVelocities(outputs.get(0).floatValue(), outputs.get(1).floatValue());
                    physics.stepSimulation((float)(1/GAME_HERTZ));
                }
                
                lastRenderTime = now;
                while ( now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS)
                {
                   Thread.yield();
                   try {Thread.sleep(1);} catch(Exception e) {} 
                   now = System.nanoTime();
                }
            }
        }
    }
}
