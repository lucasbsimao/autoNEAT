/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        
        draw.addKeyListener(new KeyAdapter() {
            public void KeyPressed(KeyEvent evt){
                
            }
        });    
        
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
