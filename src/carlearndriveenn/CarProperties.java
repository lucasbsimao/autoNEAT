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
    private ArrayList<Vec2> sensorsVec;
    private Vec2 linVelocity;
    private float angVelocity;
    
    private boolean hasCrashed;
    
    public CarProperties(){
        position = new Vec2(-1,-1);
        frontVec = new Vec2(0,0);
        sensorsVec = new ArrayList<>(5);
        linVelocity = new Vec2(0,0);
        angVelocity = 0.0F;
        angle = 0;
        
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
        Vec2 front = new Vec2();
        front.x = (float)Math.cos(Math.toRadians(angle));
        front.y = (float)Math.sin(Math.toRadians(angle));
        this.frontVec = front;
        this.angle = angle;
    }
}
