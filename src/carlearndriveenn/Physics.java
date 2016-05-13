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

import utils.Vec2;
import java.util.ArrayList;
import utils.KeyPressed;

/**
 *
 * @author lucas
 */
public class Physics{
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
        if(!carReachFinish()){
            calculateCarSensorStage();
            detectCollision();
        }
    }
    
    private boolean carReachFinish(){
        Vec2 vecDistFinish = midPoints.get(midPoints.size()-1).sub(carProp.getPosition());
        if(vecDistFinish.length() < carProp.getHeight()){
            carProp.setChampion(true);
            carProp.setCrashed(true);
            return true;
        }
        return false;
    }
    
    private void calculateCarPosition(float timeStep){
        Vec2 deltaPosition = carProp.getLinVelocity().mul(timeStep);
        carProp.setPosition(carProp.getPosition().add(deltaPosition));
        
        float deltaAngle = carProp.getAngVelocity()*timeStep;
        //System.out.println("Physics:\t" + carProp.getAngle() + "\t" + deltaAngle);
        carProp.setAngle(carProp.getAngle()+(float)Math.toDegrees(deltaAngle));
    }
    
    public void detectCollision(){
        ArrayList<Vec2> carEdgePts = searchCarEdgePts();
        
        ArrayList<Vec2> nearRoadEdgePts = new ArrayList<>();
        ArrayList<Vec2> nearRoadMidPts = new ArrayList<>();
        for(int i = 0; i < carEdgePts.size();i++){
            Vec2 nearestPt = searchNearPointRoadEdge(carEdgePts.get(i));

            if(nearestPt != null)nearRoadEdgePts.add(nearestPt);
            
            int id = -1;
            if(getInEdge().contains(nearestPt)) id = getInEdge().indexOf(nearestPt);
            else id = getOutEdge().indexOf(nearestPt);
            nearRoadMidPts.add(getMidPoints().get(id));
        }

        for(int i = 0; i < carEdgePts.size();i++){
            if(tangentCollision(nearRoadMidPts.get(i),nearRoadEdgePts.get(i),carEdgePts.get(i))){
                carProp.isDebugCrash = false;
                carProp.setCrashed(true);
                
                carProp.setPosition(new Vec2(160,60));
                carProp.reset();
                break;
            }
        }
    }
    
    private boolean tangentCollision(Vec2 midPoint, Vec2 edgePoint,Vec2 carEdgePoint){
        Vec2 normalVec = midPoint.sub(edgePoint);
        //MODIFIQUEI
        normalVec.normalize();
        
        Vec2 relCarMidPt = carEdgePoint.sub(edgePoint);
        relCarMidPt.normalize();
        double absRad = Math.abs(normalVec.angle(relCarMidPt));
        double angThresrold = Vec2.PI/2;
        if(absRad > angThresrold){
//            carProp.debugEdPointCar = carEdgePoint;
//            carProp.debugEdPointRoad = edgePoint;
//            carProp.isDebugCrash = true;
            return true;
        }
        
        return false;
    }
    
    private ArrayList<Integer> searchCarVecArea(){
        float areaParam = carProp.getWidth() < carProp.getHeight() ? carProp.getHeight() : carProp.getWidth();
        ArrayList<Integer> carArea = new ArrayList<>();
        boolean achievePt = false;
        for(int i = 0; i < getMidPoints().size(); i++){
            Vec2 midPt = getMidPoints().get(i);
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
    
    private void calculateCarSensorStage() {
        ArrayList<Sensor> carSensors = carProp.getSensorsVec();
        double dSensReach = 0;
        for(int i = 0; i < carSensors.size();i++){
            Sensor sens = carSensors.get(i);
            dSensReach = sens.getSensorStage();
            Vec2 sensUnit = sens.getSensorUnitVec().mul((float)sens.getSensorLength());
            Vec2 sensorFinPt = sens.getSensorStartPosition().add(sensUnit);
            Vec2 nearestPt = searchNearPointRoadEdge(sensorFinPt);
            
            Vec2 axisXUVec = defineSensorRelativeCoordinate(sens,nearestPt);
            Vec2 axisYUVec = null;
            if(getOutEdge().contains(nearestPt)) 
                axisYUVec = new Vec2(axisXUVec.rotated(-Vec2.PI/2));
            else
                axisYUVec = new Vec2(axisXUVec.rotated(Vec2.PI/2));
            
            Vec2 relPosSensIniEdge = sens.getSensorStartPosition().sub(nearestPt);
            Vec2 relPosSensFinEdge = sensorFinPt.sub(nearestPt);
            
            if(axisYUVec.angle(relPosSensFinEdge) > Vec2.PI/2){
                sens.setSensorStage(0);
            }else{
                double radsSensUVecToXUVec = axisXUVec.angle(sens.getSensorUnitVec());
                double radsSensIniToXUVec = axisXUVec.angle(relPosSensIniEdge);
                
                double localYCompLen = Math.abs(sens.getSensorLength()*Math.sin(radsSensUVecToXUVec));
                double localYCompRemainLen = Math.abs(relPosSensIniEdge.length()*Math.sin(radsSensIniToXUVec));
                
                double remainLenght = Math.abs(localYCompLen - localYCompRemainLen);
                double percRemainLen = 0;
                if(localYCompLen != 0)percRemainLen = remainLenght/localYCompLen;
                dSensReach = -(dSensReach + percRemainLen);
                sens.setSensorStage((float)percRemainLen);
                sens.setTaxSensorStage((float)dSensReach);
                carProp.taxInfluence = carProp.getFitness()+dSensReach*CarProperties.constPunishSensor;
                carProp.setFitness(carProp.getFitness()+dSensReach*CarProperties.constPunishSensor);
            }
        }
    }

    private Vec2 searchNearPointRoadEdge(Vec2 refPt){
        float nearestDist = 9999;
        float minDistToReach = this.roadSize*1.1F;
        boolean reachNear = false;
        Vec2 nearestPt = null;
        for(int j = 0; j < getInEdge().size();j++){
            Vec2 vecDist = refPt.sub(getInEdge().get(j));
            if(vecDist.length() < nearestDist && vecDist.length() < minDistToReach){
                nearestDist = vecDist.length();
                nearestPt = new Vec2(getInEdge().get(j));
                reachNear = true;
            }else{
                if(reachNear){
                    reachNear = false;
                    break;
                }
            }
        }

        for(int j = 0; j < getOutEdge().size();j++){
            Vec2 vecDist = refPt.sub(getOutEdge().get(j));
            if(vecDist.length() < nearestDist && vecDist.length() < minDistToReach){
                nearestDist = vecDist.length();
                nearestPt = new Vec2(getOutEdge().get(j));
                reachNear = true;
            }else{
                if(reachNear){
                    reachNear = false;
                    break;
                }
            }
        }
        
        return nearestPt;
    }
    
    public Vec2 calculateRoadDirection(Vec2 refPt){
        float nearestDist = 9999;
        Vec2 nearestPt = null;
        int id = 0;
        for(int j = 0; j < midPoints.size();j++){
            Vec2 vecDist = refPt.sub(midPoints.get(j));
            if(vecDist.length() < nearestDist){
                nearestDist = vecDist.length();
                nearestPt = new Vec2(midPoints.get(j));
                id = j;
            }
        }
        
        Vec2 direction = null;
        if(id+1 != midPoints.size()){
            direction = midPoints.get(id+1).sub(nearestPt);
        }else{
            direction = nearestPt.sub(midPoints.get(id-1));
        }
        
        return direction;
    }
    
    private Vec2 defineSensorRelativeCoordinate(Sensor sens, Vec2 edgeNearPt){
        Vec2 sensorFinPt = sens.getSensorStartPosition().add(sens.getSensorUnitVec().mul((float)sens.getSensorLength()));
        
        ArrayList<Vec2> edge;
        if(getInEdge().contains(edgeNearPt)) edge = getInEdge();
        else edge = getOutEdge();
        
        int id = edge.indexOf(edgeNearPt);
        Vec2 relPosSensFinEdge = new Vec2();
        relPosSensFinEdge = sensorFinPt.sub(edgeNearPt);
        
        Vec2 axisUVec = null;
        if(id+1 != edge.size()) axisUVec = edge.get(id+1).sub(edgeNearPt);
        else axisUVec = edgeNearPt.sub(edge.get(id-1));
        
        float diffRads = axisUVec.angle(relPosSensFinEdge);
        if(diffRads > Vec2.PI/2 && id != 0)
            axisUVec = edgeNearPt.sub(edge.get(id-1));
        
        axisUVec.normalize();
        
        
        return axisUVec;
    }

//    @Override
//    public void run() {
//        final double GAME_HERTZ = 30.0;
//        final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
//        double lastUpdateTime = System.nanoTime();
//        double lastRenderTime = System.nanoTime();
//        final double TARGET_FPS = 60;
//        final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;
//      
//        while (true)
//        {
//            double now = System.nanoTime();
//
//            while( now - lastUpdateTime > TIME_BETWEEN_UPDATES)
//            {
//                stepSimulation((float)(1/GAME_HERTZ));
//                setCompleteStep(true);
//                lastRenderTime = now;
//                while ( now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS)
//                {
//                   Thread.yield();
//                   try {Thread.sleep(1);} catch(Exception e) {} 
//                   now = System.nanoTime();
//                }
//            }
//        }
//    }

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

    /**
     * @return the roadSize
     */
    public float getRoadSize() {
        return roadSize;
    }

    /**
     * @return the inEdge
     */
    public ArrayList<Vec2> getInEdge() {
        return inEdge;
    }

    /**
     * @return the outEdge
     */
    public ArrayList<Vec2> getOutEdge() {
        return outEdge;
    }

    /**
     * @return the midPoints
     */
    public ArrayList<Vec2> getMidPoints() {
        return midPoints;
    }
}
