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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;


class Drawing extends JPanel implements Runnable{
    private final float roadSize;
    private CarProperties carProp;
    
    private BufferedImage bigrass;
    private BufferedImage bicar;
    private BufferedImage biground;
    private BufferedImage bifinish;
    private TexturePaint grasstp;
    private TexturePaint groundtp;
    private TexturePaint finishtp;
    
    private ArrayList<Vec2> vecOutBorder;
    private ArrayList<Vec2> vecInBorder;
    private ArrayList<Vec2> midPoints;
    private ArrayList<Vec2> roadEdgePoints;

    private ArrayList<Vec2> finishCoords;
    
    private AffineTransform carTrans;
    private Vec2 posImageCar;
    
    public Drawing(CarProperties car){
        carProp = car;
        roadSize = 50;
        posImageCar = new Vec2(-1,-1);

        loadImages();
        createPath();
    }
    
    private void createPath(){
        midPoints = new ArrayList<Vec2>();
        vecOutBorder = new ArrayList<Vec2>();
        vecInBorder = new ArrayList<Vec2>();
        
        midPoints.add(new Vec2(140,60));
        try{
            addRoadSegment(300, 70, 90);
            addRoadSegment(300, 25, 90);
            addRoadSegment(200, 80, 180);
            addRoadSegment(30, 50,-180);
//            addRoadSegment(50, 25, -45);
//            addRoadSegment(100, 80, 45);
//            addRoadSegment(50, 70, 45);
//            addRoadSegment(200, 100, -90);
//            addRoadSegment(300, 70, -30);
//            addRoadSegment(200, 25, -90);
//            addRoadSegment(50, 25, 120);
//            addRoadSegment(50, 70, 120);
//            addRoadSegment(150, 25, -120);
            
            createBorders();
            createFinishBorders();
            
        }catch(Exception ex){
            System.out.println(ex.toString());
        } 
    }
    
    /** Defines the initial straight street, and then the curve with the respective ray. OBS: Degrees increases in counter-clockwise (0 ~ 180). **/
    private void addRoadSegment(float streetLenght, float midRay, int degrees) throws Exception{
        if(midPoints.isEmpty()){
            throw new Exception("The road must have initial reference points!\n");
        }
        
        if(midRay < roadSize/2) midRay = roadSize/2;
        
        Vec2 vecIni = new Vec2();
        vecIni.x = midPoints.get(midPoints.size()-1).x;
        vecIni.y = midPoints.get(midPoints.size()-1).y;
        
        Vec2 lstUVec = new Vec2();
        if(midPoints.size() >= 2) {
            Vec2 vecSec = new Vec2();
            vecSec.x = midPoints.get(midPoints.size()-2).x;
            vecSec.y = midPoints.get(midPoints.size()-2).y;
            
            lstUVec = (vecIni.sub(vecSec)).normalized();
        } else {
            lstUVec.x = 1;
            lstUVec.y = 0;
        }
        
        float loadingStreet = 0;
        while(loadingStreet < streetLenght){
            loadingStreet += carProp.getWidth() < carProp.getHeight() ? carProp.getHeight()/2 : carProp.getWidth()/2;
            if(loadingStreet >= streetLenght)
                loadingStreet = streetLenght;
            
            Vec2 lstDirVec = new Vec2(lstUVec);
            lstDirVec.mulLocal(loadingStreet);   
            Vec2 finStPoint = new Vec2(vecIni.add(lstDirVec));
            midPoints.add(finStPoint);
        }
        
        addCurvePoints(lstUVec,midPoints.get(midPoints.size()-1), midRay,degrees);
    }
    
    private void addCurvePoints(Vec2 lstUvec,Vec2 refPoint, float midRay, int degrees) {
        float curveRads = (float)degrees/180*Vec2.PI;
        
        Vec2 rayDir;
        if(curveRads > 0)
            rayDir = new Vec2(lstUvec.rotated(Vec2.PI/2));
        else
            rayDir = new Vec2(lstUvec.rotated(-Vec2.PI/2));
        
        rayDir.mulLocal(midRay);
        
        Vec2 centRayPoint = new Vec2(refPoint.add(rayDir));
        
        rayDir.rotate(Vec2.PI);
        
        int i = 5;
        while(i <= Math.abs(degrees)){
            int moveDir = i;
            
            if(degrees < 0)
                moveDir *= -1;
            
            Vec2 rayRotated = new Vec2(rayDir.rotated((float)moveDir/180*Vec2.PI));
            
            Vec2 actCurvePoint = new Vec2(rayRotated.add(centRayPoint));
            
            midPoints.add(actCurvePoint);
            
            i+=5;
            if(i > Math.abs(degrees) && i != Math.abs(degrees)+5)
                i = Math.abs(degrees);
        }
        
        rayDir.normalize();
        rayDir.rotate((float)degrees/180*Vec2.PI);
        if(degrees > 0)
            rayDir.rotate(Vec2.PI/2);
        else
            rayDir.rotate(-Vec2.PI/2);
        
        Vec2 lastCurPoint = new Vec2(midPoints.get(midPoints.size()-1));
        lastCurPoint.addLocal(rayDir.mul(1.0f));

        midPoints.add(lastCurPoint);
    }
    
    private void createBorders(){
        for(int i = 0; i < midPoints.size();i++){
            if(i + 1 == midPoints.size()) break;
            Vec2 vec1 = new Vec2(midPoints.get(i));
            Vec2 vec2 = new Vec2(midPoints.get(i+1));
            
            Vec2 uVec = new Vec2(vec2.sub(vec1).normalized());
            uVec.rotate(-Vec2.PI/2);
            uVec.mulLocal(roadSize/2);
            if(i == 0)vecOutBorder.add(new Vec2(uVec.add(vec1)));
            vecOutBorder.add(new Vec2(uVec.add(vec2)));
          
            uVec.rotate(Vec2.PI);
            if(i == 0)vecInBorder.add(new Vec2(uVec.add(vec1)));
            vecInBorder.add(new Vec2(uVec.add(vec2)));
            
        }
    }
    
    private void createFinishBorders(){
        roadEdgePoints = new ArrayList<>();
        finishCoords = new ArrayList<>();
        
        Vec2 uFinishLine = new Vec2(midPoints.get(midPoints.size()-1));
        uFinishLine.subLocal(midPoints.get(midPoints.size()-2));
        uFinishLine.normalize();
        Vec2 ptFinishLine = new Vec2(uFinishLine.mul(10));
        
        finishCoords.add(vecOutBorder.get(vecOutBorder.size()-1).add(ptFinishLine));
        finishCoords.add(vecOutBorder.get(vecOutBorder.size()-1));
        finishCoords.add(vecInBorder.get(vecInBorder.size()-1));
        finishCoords.add(vecInBorder.get(vecInBorder.size()-1).add(ptFinishLine));
        
        roadEdgePoints.add(midPoints.get(0));

        roadEdgePoints.addAll(vecOutBorder);
        
        for(int i = vecInBorder.size()-1; i >= 0;i--){
            roadEdgePoints.add(vecInBorder.get(i));
        }
    }

    private void loadImages() {
        
        try {
            bigrass = ImageIO.read(new File("Images/grama-1.jpg"));
            bicar = ImageIO.read(new File("Images/car2d2.png"));
            carProp.setCarDimensions(bicar.getWidth(), bicar.getHeight());
            biground = ImageIO.read(new File("Images/areia.jpg"));
            bifinish = ImageIO.read(new File("Images/finish.png"));
        } catch (IOException ex) {

            Logger.getLogger(Drawing.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
    }

    private void doDrawing(Graphics g) {  
        
        Graphics2D g2d = (Graphics2D) g.create();

        grasstp = new TexturePaint(bigrass, new Rectangle(0, 0, 90, 60));
        groundtp = new TexturePaint(biground, new Rectangle(0, 0, 90, 60));
        finishtp = new TexturePaint(bifinish, new Rectangle(0, 0, 100, 100));

        g2d.setPaint(grasstp);
        g2d.fillRect(0, 0, 1200, 700);
        
        g2d.setPaint(groundtp);
        
        createFinishLine(g2d, roadEdgePoints);
        
        g2d.setPaint(finishtp);
        
        createFinishLine(g2d, finishCoords);
   
        transformCar();
        
        AffineTransformOp op = new AffineTransformOp(carTrans, AffineTransformOp.TYPE_BILINEAR);

        g2d.drawImage(op.filter(bicar, null), (int)posImageCar.x, (int)posImageCar.y, null);
        
        if(carProp.isCrashed()){
            g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            g2d.setColor(Color.red);
            g2d.drawString("Recalculating best strategy...", 700, 100);
        }
        
        g2d.setStroke(new BasicStroke(1));
        ArrayList<Sensor> sensors = new ArrayList(carProp.getSensorsVec());
        if(sensors != null){
            for(int i = 0; i < sensors.size();i++){
                Sensor sens = sensors.get(i);
                try{
                    float stage = (float)sens.getSensorStage();
                    g2d.setPaint(new Color(1, 1-stage, 1-stage));
                    Vec2 sensPos = sens.getSensorStartPosition();
                    Vec2 sensFinPos = sensPos.add(sens.getSensorUnitVec().mul((float)sens.getSensorLength()));
                    g2d.draw(new Line2D.Float((int)sensPos.x, (int)sensPos.y, (int)sensFinPos.x,(int)sensFinPos.y));
                }catch(java.lang.IllegalArgumentException ex){
                    float stage = (float)sens.getSensorStage();
                }
            }
        }
        
        g2d.dispose();
    }
    
    public void transformCar(){
        double locationX =bicar.getWidth() / 2;
        double locationY = bicar.getHeight() / 2;
        
        double diff = Math.abs(bicar.getWidth() - bicar.getHeight());

        double rotationRequired = Math.toRadians(carProp.getAngle());
        double unitX = Math.abs(Math.cos(rotationRequired));
        double unitY = Math.abs(Math.sin(rotationRequired));
        
        double correctUx = unitX;
        double correctUy = unitY;
        if(bicar.getWidth() < bicar.getHeight()){
            correctUx = unitY;
            correctUy = unitX;
        }
        
        posImageCar.x = carProp.getPosition().x-(int)(locationX)-(int)(correctUx*diff);
        posImageCar.y = carProp.getPosition().y-(int)(locationY)-(int)(correctUy*diff);
        
        carTrans = new AffineTransform();
        carTrans.translate(correctUx*diff, correctUy*diff);
        carTrans.rotate(rotationRequired, locationX, locationY);
        
    }
    
    private void createFinishLine(Graphics2D g2d, ArrayList<Vec2> array){  
        GeneralPath gen = new GeneralPath();
        gen.moveTo(array.get(0).x,array.get(0).y);
        
        for(int i =0; i < array.size();i++){
            gen.lineTo(array.get(i).x, array.get(i).y);
        }
        
        gen.closePath();
        g2d.fill(gen);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
    
    public float getRoadSize(){
        return roadSize;
    }
    
    public ArrayList<Vec2> getVecOutBorder(){
        return vecOutBorder;
    }
    
    public ArrayList<Vec2> getVecInBorder(){
        return vecInBorder;
    }
    
    public ArrayList<Vec2> getVecMidPoints(){
        return midPoints;
    }

    @Override
    public void run() {
        final double GAME_HERTZ = 30.0;
        final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
        double lastUpdateTime = System.nanoTime();
        double lastRenderTime = System.nanoTime();
        final double TARGET_FPS = 60;
        final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;
      
        while (true)
        {
            double now = System.nanoTime();

            while( now - lastUpdateTime > TIME_BETWEEN_UPDATES)
            {
                this.repaint();
                lastRenderTime = now;
                while ( now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS)
                {
                   Thread.yield();
                   try {Thread.sleep(1);} catch(Exception e) {} 
                   now = System.nanoTime();
                }
            }
        }
    }
}
