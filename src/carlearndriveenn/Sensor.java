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

/**
 *
 * @author lucas
 */
public class Sensor {
    private double sensorLength;
    private Vec2 sensorStartPosition;
    private Vec2 sensorUnitVec;
    private double sensorStage;
    private double taxSensorStage;
    
    public Sensor(){
        sensorStage = 0;
        sensorStartPosition = new Vec2(-1,-1);
        sensorUnitVec = new Vec2(-1,-1);
        sensorLength = 0;
        taxSensorStage = 0;
    }
    
    public Sensor(Sensor copy){
        sensorStage = copy.sensorStage;
        sensorStartPosition = copy.sensorStartPosition;
        sensorUnitVec = new Vec2(copy.sensorUnitVec);
        sensorLength = copy.sensorLength;
        taxSensorStage = copy.taxSensorStage;
    }

    /**
     * @return the sensorLength
     */
    public double getSensorLength() {
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
    public double getSensorStage() {
        return sensorStage;
    }

    /**
     * @param sensorStage the sensorStage to set
     */
    public void setSensorStage(float sensorStage) {
        this.sensorStage = sensorStage;
    }

    /**
     * @return the taxSensorStage
     */
    public double getTaxSensorStage() {
        return taxSensorStage;
    }

    /**
     * @param taxSensorStage the taxSensorStage to set
     */
    public void setTaxSensorStage(double taxSensorStage) {
        this.taxSensorStage = taxSensorStage;
    }
}
