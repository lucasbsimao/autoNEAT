/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carlearndriveenn;

/**
 *
 * @author lucas
 */
public class CarProperties {
    private Vec2 position;
    private float angle;
    private Vec2 frontVec;
    private Vec2 linVelocity;
    private double angVelocity;
    
    private boolean hasCrashed;
    
    public CarProperties(){
        position = new Vec2(-1,-1);
        frontVec = new Vec2(0,0);
        linVelocity = new Vec2(2,0);
        angVelocity = 0.0;
        angle = 0;
        
        hasCrashed = false;
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
     * @param frontVec the front to set
     */
    public void setFrontVector(Vec2 frontVec) {
        this.frontVec = frontVec;
    }

    /**
     * @return the linVelocity
     */
    public Vec2 getLinVelocity() {
        return linVelocity;
    }

    /**
     * @param linVelocity the linVelocity to set
     */
    public void setLinVelocity(Vec2 linVelocity) {
        this.linVelocity = linVelocity;
    }

    /**
     * @return the angVelocity
     */
    public double getAngVelocity() {
        return angVelocity;
    }

    /**
     * @param angVelocity the angVelocity to set
     */
    public void setAngVelocity(double angVelocity) {
        this.angVelocity = angVelocity;
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
        this.angle = angle;
    }
}
