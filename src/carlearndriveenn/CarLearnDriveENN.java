/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carlearndriveenn;

import java.awt.EventQueue;

public class CarLearnDriveENN {

    public static void main(String[] args) {
        
        
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run(){
                Simulator simulator = new Simulator();
                simulator.initUI();
                simulator.setVisible(true);
            }
        });
        
        
    }
    
}
