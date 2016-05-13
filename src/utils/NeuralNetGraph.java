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
package utils;

/**
 *
 * @author Lucas Borsatto Simão
 */
public class NeuralNetGraph{
    private Double val;
    private Integer depth;

    public NeuralNetGraph(Double a, Integer b){
        this.val = a;
        this.depth = b;
    }
    
    /**
     * @return the val
     */
    public Double getVal() {
        return val;
    }

    /**
     * @param val the val to set
     */
    public void setVal(Double val) {
        this.val = val;
    }

    /**
     * @return the depth
     */
    public Integer getDepth() {
        return depth;
    }

    /**
     * @param depth the depth to set
     */
    public void setDepth(Integer depth) {
        this.depth = depth;
    }


}
