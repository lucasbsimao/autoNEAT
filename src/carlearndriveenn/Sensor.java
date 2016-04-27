/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carlearndriveenn;

import utils.Vec2;

/**
 *
 * @author lucas
 */
public class Sensor {
    private float sensorLength;
    private Vec2 sensorStartPosition;
    private Vec2 sensorUnitVec;
    private float sensorStage;
    
    public Sensor(){
        sensorStage = 0;
        sensorStartPosition = new Vec2(-1,-1);
        sensorUnitVec = new Vec2(-1,-1);
        sensorLength = 0;
    }

    /**
     * @return the sensorLength
     */
    public float getSensorLength() {
        return sensorLength;
    }

    /**
     * @param sensorLength the sensorLength to set
     */
    public void setSensorLength(float sensorLength) {
        this.sensorLength = sensorLength;
    }

    /**
     * @return the sensorStartPosition
     */
    public Vec2 getSensorStartPosition() {
        return sensorStartPosition;
    }

    /**
     * @param sensorStartPosition the sensorStartPosition to set
     */
    public void setSensorStartPosition(Vec2 sensorStartPosition) {
        this.sensorStartPosition = sensorStartPosition;
    }

    /**
     * @return the sensorUnitVec
     */
    public Vec2 getSensorUnitVec() {
        return sensorUnitVec;
    }

    /**
     * @param sensorUnitVec the sensorUnitVec to set
     */
    public void setSensorUnitVec(Vec2 sensorUnitVec) {
        this.sensorUnitVec = sensorUnitVec;
    }

    /**
     * @return the sensorStage
     */
    public float getSensorStage() {
        return sensorStage;
    }

    /**
     * @param sensorStage the sensorStage to set
     */
    public void setSensorStage(float sensorStage) {
        this.sensorStage = sensorStage;
    }
}
