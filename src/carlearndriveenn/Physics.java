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
    private ArrayList<Vec2> inEdge;
    private ArrayList<Vec2> outEdge;
    private ArrayList<Vec2> midPoints;
    private float roadSize;
    private boolean completeStep;
    
    private final double timeStep;

    public Physics(CarProperties carProp,ArrayList<Vec2> inEdge,ArrayList<Vec2> outEdge,ArrayList<Vec2> midPoints, float roadSize){
        this.roadSize = roadSize;
        this.outEdge = outEdge;
        this.inEdge = inEdge;
        this.midPoints = midPoints;
        this.carProp = carProp;
        this.completeStep = false;
        
        timeStep = 1/60;
    }
    
    public void stepSimulation(float timeStep){
        calculateCarPosition(timeStep);
        hasCollided();
    }
    
    private void calculateCarPosition(float timeStep){
        Vec2 deltaPosition = carProp.getLinVelocity().mul(timeStep);
        
        carProp.setPosition(carProp.getPosition().add(deltaPosition));
        
        float deltaAngle = carProp.getAngVelocity()*timeStep;
        carProp.setAngle(carProp.getAngle()+deltaAngle);
    }
    
    public boolean hasCollided(){
        ArrayList<Vec2> carEdgePts = searchCarEdgePts();
        
        ArrayList<Vec2> nearRoadEdgePts = new ArrayList<>();
        ArrayList<Vec2> nearRoadMidPts = new ArrayList<>();
        for(int i = 0; i < carEdgePts.size();i++){
            float nearestDist = 9999;
            Vec2 nearestPt = null;
            int id = 0;
            for(int j = 0; j < inEdge.size();j++){
                Vec2 vecDist = carEdgePts.get(i).sub(inEdge.get(j));
                if(vecDist.length() < nearestDist){
                    nearestDist = vecDist.length();
                    nearestPt = new Vec2(inEdge.get(j));
                    id = j;
                }
            }
            
            for(int j = 0; j < outEdge.size();j++){
                Vec2 vecDist = carEdgePts.get(i).sub(outEdge.get(j));
                if(vecDist.length() < nearestDist){
                    nearestDist = vecDist.length();
                    nearestPt = new Vec2(outEdge.get(j));
                    id = j;
                }
            }

            if(nearestPt != null)nearRoadEdgePts.add(nearestPt);
            nearRoadMidPts.add(midPoints.get(id));
        }

        for(int i = 0; i < carEdgePts.size();i++){
            if(tangentCollision(nearRoadMidPts.get(i),nearRoadEdgePts.get(i),carEdgePts.get(i))){
                carProp.setPosition(new Vec2(160,60));
                break;
            }
        }
        
        return false;
    }
    
    private boolean tangentCollision(Vec2 midPoint, Vec2 edgePoint,Vec2 carEdgePoint){
        Vec2 normalVec = midPoint.sub(edgePoint);
        normalVec.normalize();
        
        Vec2 relCarMidPt = carEdgePoint.sub(edgePoint);
        relCarMidPt.normalize();
        float absRad = (float) Math.abs(normalVec.angle(relCarMidPt));
        System.out.println("Calculo");
        System.out.println(Math.toDegrees(absRad));
        System.out.println(midPoint);
        System.out.println(edgePoint);
        System.out.println(carEdgePoint);
        if(absRad > Vec2.PI/2){
            return true;
        }
        
        return false;
    }
    
    private ArrayList<Integer> searchCarVecArea(){
        float areaParam = carProp.getWidth() < carProp.getHeight() ? carProp.getHeight() : carProp.getWidth();
        ArrayList<Integer> carArea = new ArrayList<>();
        boolean achievePt = false;
        for(int i = 0; i < midPoints.size(); i++){
            Vec2 midPt = midPoints.get(i);
            Vec2 distPointCar = carProp.getPosition().sub(midPt);
            if(distPointCar.length() < areaParam){
                carArea.add(i);
                achievePt = true;
            }else{
                if(achievePt) break;
            }
        }
        return carArea;
    }
    
    private ArrayList<Vec2> searchCarEdgePts(){
        
        ArrayList<Vec2> carEdgePt = new ArrayList<>();
        Vec2 carFront = new Vec2(carProp.getFrontVector());
        
        Vec2 localSideAxis = new Vec2(carFront);
        localSideAxis.rotate(Vec2.PI/2);
        Vec2 edgePt = new Vec2(carProp.getPosition()); 
        edgePt.addLocal(carFront.mul(carProp.getWidth()/2));
        edgePt.addLocal(localSideAxis.mul(carProp.getHeight()/2));
        
        carEdgePt.add(new Vec2(edgePt));
        
        edgePt.subLocal(carProp.getFrontVector().mul(carProp.getWidth()));
        carEdgePt.add(new Vec2(edgePt));
        
        edgePt.subLocal(localSideAxis.mul(carProp.getHeight()));
        carEdgePt.add(new Vec2(edgePt));
        
        edgePt.addLocal(carProp.getFrontVector().mul(carProp.getWidth()));
        carEdgePt.add(new Vec2(edgePt));
        
        return carEdgePt;
    }

    @Override
    public void run() {
        final double GAME_HERTZ = 30.0;
        final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
        double lastUpdateTime = System.nanoTime();
        double lastRenderTime = System.nanoTime();
        final double TARGET_FPS = 60;
        final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;
      
        while (true)
        {
            double now = System.nanoTime();

            while( now - lastUpdateTime > TIME_BETWEEN_UPDATES)
            {
                stepSimulation((float)(1/GAME_HERTZ));
                setCompleteStep(true);
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

    /**
     * @return the completeStep
     */
    public boolean isCompleteStep() {
        return completeStep;
    }

    /**
     * @param completeStep the completeStep to set
     */
    public void setCompleteStep(boolean completeStep) {
        this.completeStep = completeStep;
    }
}
