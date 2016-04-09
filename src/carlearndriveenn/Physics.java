/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carlearndriveenn;

import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lucas
 */
public class Physics implements Runnable{
    private CarProperties carProp;
    private ArrayList<Vec2> inBoard;
    private ArrayList<Vec2> outBoard;
    private float roadSize;
    
    private final double timeStep;

    public Physics(CarProperties carProp,ArrayList<Vec2> inBoard,ArrayList<Vec2> outBoard, float roadSize){
        this.roadSize = roadSize;
        this.outBoard = outBoard;
        this.inBoard = inBoard;
        this.carProp = carProp;
        
        timeStep = 1/60;
    }
    
    public void stepSimulation(float timeStep){
        calculateCarPosition(timeStep);
    }
    
    private void calculateCarPosition(float timeStep){
        Vec2 deltaPosition = carProp.getLinVelocity().mul(timeStep);
        carProp.setPosition(carProp.getPosition().add(deltaPosition));
        
        
    }
    
    public Vec2 getPointDistanceToBoard(Vec2 pt){
        double distance = 99999;
        
        while(distance < roadSize/2){
            
        }
        
        return null;
    }
    
    public boolean hasCollided(){
        return false;
    }

    @Override
    public void run() {
        float timeStep = 1/60F;
        while(true){
            try {
                Thread.sleep((long) (timeStep*1000));
            } catch (InterruptedException ex) {
                Logger.getLogger(Physics.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            stepSimulation(timeStep);
        }
    }
}
