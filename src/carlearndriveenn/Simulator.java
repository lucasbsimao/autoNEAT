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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.Timer;
import mlalgorithm.LearnDriveENN;
import utils.CreateRandomNumber;

/**
 *
 * @author lucas
 */
public class Simulator extends JFrame{
    
    Timer timer;

    public void initUI() {
        CreateRandomNumber.initRand();
        CarProperties car = new CarProperties(0,new Vec2(160,60));
        
        Drawing draw = new Drawing(car);
        add(draw);
        
        Thread thDraw = new Thread(draw);
        thDraw.start();
         
        
        while(draw.getVecInBorder().size() == 0 && draw.getVecOutBorder().size() == 0 ){
            try{
                Thread.sleep(60);
            }catch(InterruptedException ex){
                System.out.println(ex.toString());
            }
        }
        
        Controller control = new Controller(car,draw.getVecInBorder(),draw.getVecOutBorder(),draw.getVecMidPoints(),draw.getRoadSize());
        Thread th = new Thread(control);
        th.start();

        setTitle("Points");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
