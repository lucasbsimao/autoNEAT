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

/**
 *
 * @author lucas
 */
public class CarProperties {
    private float width;
    private float height;
    private Vec2 position;
    private float angle;
    private Vec2 frontVec;
    private ArrayList<Sensor> sensorsVec;
    private Vec2 linVelocity;
    private float angVelocity;
    private double fitness;
    private boolean champion;
    
    private final double stepVelocity = 0.5;
    private final double maxVelocity = 20;
    
    public double getMaxVelocity(){
        return maxVelocity;
    }
    
    private double velLeft;
    private double velRight;
    
    public static final double constPunishSensor = 40;
    public static double maxAngVelocity;
    
    
    
    public Vec2 debugEdPointCar;
    public Vec2 debugEdPointRoad;
    public boolean isDebugCrash;
    
    private boolean hasCrashed;
    
    public CarProperties(float initAng, Vec2 initPos){
        sensorsVec = new ArrayList();
        position = initPos;
        this.setAngle(initAng);
        linVelocity = new Vec2(0,0);
        angVelocity = 0.0F;
        fitness = 0.0F;
        
        velLeft = 0;
        velRight = 0;
        
        hasCrashed = false;
        champion = false;
        
        debugEdPointCar = new Vec2(-1,-1);
        debugEdPointRoad = new Vec2(-1,-1);
        isDebugCrash = false;
    }
    
    public CarProperties(CarProperties car){
        this.angVelocity = car.angVelocity;
        this.angle = car.angle;
        this.frontVec = new Vec2(car.frontVec);
        this.hasCrashed = car.hasCrashed;
        this.height = car.height;
        this.linVelocity = new Vec2(car.linVelocity);
        this.position = new Vec2(car.position);
        this.sensorsVec = new ArrayList<>(car.sensorsVec);
        this.fitness = car.fitness;
        this.width = car.width;
    }
    
    public void setCarDimensions(float width, float height){
        this.width = width;
        this.height = height;
        CarProperties.maxAngVelocity = maxVelocity/(height/2);
    }
    
    public float getWidth(){
        return width;
    }
    
    public float getHeight(){
        return height;
    }

    /**
     * @return the position
     */
    public Vec2 getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Vec2 position) {
        this.position = position;
    }
    
    /**
     * @return the frontVec
     */
    public Vec2 getFrontVector() {
        return frontVec;
    }

    /**
     * @return the linVelocity
     */
    public Vec2 getLinVelocity() {
        return linVelocity;
    }

    public void setWheelVelocities(float left, float right) {
        left -= 0.5;
        right -= 0.5;
        
        velLeft += left;
        velRight += right;
        
        if(velLeft > maxVelocity) velLeft = maxVelocity;
        if(velRight > maxVelocity) velRight = maxVelocity;
        if(velLeft < -maxVelocity) velLeft = -maxVelocity;
        if(velRight < -maxVelocity) velRight = -maxVelocity;
        
        left = (float)(velLeft*stepVelocity);
        right = (float)(velRight*stepVelocity);
        
        float maxVel = left > right ? left : right;
        float minVel = left < right ? left : right;

        float incTan = (maxVel - minVel)/height;
        float rayMotion = incTan*minVel+height/2;
            
        float centVelocity = (left + right)/2;
        this.linVelocity = frontVec.mul(centVelocity);//new Vec2(frontVec.mul(centVelocity).x+linVelocity.y,frontVec.mul(centVelocity).x+linVelocity.y);
        
        float ray = (maxVel/minVel - 1)/(height/2);
        ray += height/2;
        
        if(right < left) centVelocity *= -1;
        
        this.angVelocity = centVelocity/(rayMotion);
        //if(this.angVelocity > maxVelocity/(height/2)) this.angVelocity = (float)maxVelocity/(height/2);
        //if(this.angVelocity < maxVelocity/(height/2)) this.angVelocity = (float)-maxVelocity/(height/2);
    }

    /**
     * @return the angVelocity
     */
    public float getAngVelocity() {
        return angVelocity;
    }

    /**
     * @return the hasCrashed
     */
    public boolean isCrashed() {
        return hasCrashed;
    }

    /**
     * @param hasCrashed the hasCrashed to set
     */
    public void setCrashed(boolean hasCrashed) {
        this.hasCrashed = hasCrashed;
    }

    /**
     * @return the angle
     */
    public float getAngle() {
        return angle;
    }

    /**
     * @param angle the angle to set
     */
    public void setAngle(float angle) {
        if(sensorsVec != null) sensorsVec.clear();

        Vec2 front = new Vec2();
        front.x = (float)Math.cos(Math.toRadians(angle));
        front.y = (float)Math.sin(Math.toRadians(angle));
        this.frontVec = front;
        this.angle = angle;

        Vec2 sideVec = new Vec2(frontVec.rotated(-Vec2.PI/2));
        float sensorIntervalAng = Vec2.PI/5;
        
        float sensParam = this.width > this.height ? this.width : this.height;
         sensParam = 20;
        float actSensAng = 0;
        while(actSensAng <= Vec2.PI){
            
            //float actSensParam = sensParam*(1+actSensLen);
            
            Sensor sens = new Sensor();
            sens.setSensorLength(sensParam);
            Vec2 vecActSensPt = sideVec.rotated(actSensAng);
            sens.setSensorUnitVec(new Vec2(vecActSensPt));
            //Vec2 sensorPt = vecActSensPt.mul(sensParam);
            sens.setSensorStartPosition(this.position.add(frontVec.mul(height)));

            sensorsVec.add(sens);

            actSensAng += sensorIntervalAng;
        }
    }

    /**
     * @return the sensorsVec
     */
    public ArrayList<Sensor> getSensorsVec() {
        return sensorsVec;
    }
    
    public ArrayList<Double> getSensorStages(){
        ArrayList<Sensor> sens = new ArrayList<>(sensorsVec);
        
        ArrayList<Double> sensStages = new ArrayList<>();
        
        for(int i = 0; i < sens.size();i++){
            sensStages.add(sens.get(i).getSensorStage());
        }
        
        return sensStages;
    }
    
    public ArrayList<Double> getTaxSensorStages(){
        ArrayList<Sensor> sens = new ArrayList<>(sensorsVec);
        
        ArrayList<Double> sensStages = new ArrayList<>();
        
        for(int i = 0; i < sens.size();i++){
            sensStages.add(sens.get(i).getTaxSensorStage());
        }
        
        return sensStages;
    }
    
    public void reset(){
        this.angVelocity = 0;
        this.setAngle(0);
        this.velLeft = 0;
        this.velRight = 0;
        this.linVelocity = new Vec2(0,0);
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
     * @return the champion
     */
    public boolean isChampion() {
        return champion;
    }

    /**
     * @param champion the champion to set
     */
    public void setChampion(boolean champion) {
        this.champion = champion;
    }
}
