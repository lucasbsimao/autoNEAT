/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carlearndriveenn;

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
    private float travelledDist;
    
    private boolean hasCrashed;
    
    public CarProperties(float initAng, Vec2 initPos){
        sensorsVec = new ArrayList();
        position = initPos;
        this.setAngle(initAng);
        linVelocity = new Vec2(10,-2);
        angVelocity = 0.0F;
        travelledDist = 0.0F;
        
        hasCrashed = false;
    }
    
    public void setCarDimensions(float width, float height){
        this.width = width;
        this.height = height;
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
        float centVelocity = (float)Math.abs((left + right)/2);
        this.linVelocity = frontVec.mul(centVelocity);
        
        if(right < left) centVelocity *= -1;
        
        this.angVelocity = centVelocity/(height/2);
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
        float sensorInterval = Vec2.PI/5;
        
        float sensParam = this.width < this.height ? this.width : this.height;
        float actSensAng = 0;
        while(actSensAng <= Vec2.PI){
            Sensor sens = new Sensor();
            sens.setSensorLength(sensParam);
            Vec2 vecActSensPt = sideVec.rotated(actSensAng);
            sens.setSensorUnitVec(new Vec2(vecActSensPt));
            Vec2 sensorPt = vecActSensPt.mul(sensParam);
            sens.setSensorStartPosition(this.position.add(frontVec.mul(sensParam)));
            
            sensorsVec.add(sens);
            
            actSensAng += sensorInterval;
        }
    }

    /**
     * @return the sensorsVec
     */
    public ArrayList<Sensor> getSensorsVec() {
        return sensorsVec;
    }

    /**
     * @return the travelledDist
     */
    public float getTravelledDist() {
        return travelledDist;
    }

    /**
     * @param travelledDist the travelledDist to set
     */
    public void setTravelledDist(float travelledDist) {
        this.travelledDist = travelledDist;
    }
}
