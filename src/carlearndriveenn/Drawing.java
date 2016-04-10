/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carlearndriveenn;

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

    private ArrayList<Vec2> finishCoords;
    
    private AffineTransform carTrans;
    private Vec2 posImageCar;
    
    public Drawing(CarProperties car){
        carProp = car;
        roadSize = 50;
        posImageCar = new Vec2(-1,-1);
        carProp.setPosition(new Vec2(160,60));
        carProp.setAngle(0);

        loadImages();
        createPath();
    }
    
    private void createPath(){
        midPoints = new ArrayList<Vec2>();
        vecOutBorder = new ArrayList<Vec2>();
        vecInBorder = new ArrayList<Vec2>();
        
        midPoints.add(new Vec2(140,60));
        try{
            addRoadSegment(920, 70, 90);
            addRoadSegment(400, 25, 90);
            addRoadSegment(100, 80, 90);
            addRoadSegment(200, 100, -90);
            addRoadSegment(300, 70, -30);
            addRoadSegment(200, 25, -90);
            addRoadSegment(50, 25, 120);
            addRoadSegment(50, 70, 120);
            addRoadSegment(150, 25, -120);
            
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
            loadingStreet += bicar.getHeight()/2;
            if(loadingStreet > streetLenght)
                loadingStreet = streetLenght - (loadingStreet - bicar.getHeight()/2); 
            Vec2 lstDirVec = new Vec2(lstUVec);
            lstDirVec.mulLocal(streetLenght);
            
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
        
        int i = 0;
        while(i <= Math.abs(degrees)){
            int moveDir = i;
            
            if(degrees < 0)
                moveDir *= -1;
            
            Vec2 rayRotated = new Vec2(rayDir.rotated((float)moveDir/180*Vec2.PI));
            
            Vec2 actCurvePoint = new Vec2(rayRotated.add(centRayPoint));
            
            midPoints.add(actCurvePoint);
            
            i++;
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
        finishCoords = new ArrayList<>();
        
        Vec2 uFinishLine = new Vec2(midPoints.get(midPoints.size()-1));
        uFinishLine.subLocal(midPoints.get(midPoints.size()-2));
        uFinishLine.normalize();
        Vec2 ptFinishLine = new Vec2(uFinishLine.mul(10));
        
        finishCoords.add(vecOutBorder.get(vecOutBorder.size()-1).add(ptFinishLine));
        finishCoords.add(vecOutBorder.get(vecOutBorder.size()-1));
        finishCoords.add(vecInBorder.get(vecInBorder.size()-1));
        finishCoords.add(vecInBorder.get(vecInBorder.size()-1).add(ptFinishLine));
        
        vecOutBorder.add(vecInBorder.get(vecInBorder.size()-1));
        
        ArrayList<Vec2> arrayAux = new ArrayList<>();
        arrayAux.add(vecInBorder.get(0));
        arrayAux.addAll(vecOutBorder);
        
        vecOutBorder = arrayAux;
    }

    private void loadImages() {
        
        try {
            bigrass = ImageIO.read(new File("Images/grama-1.jpg"));
            bicar = ImageIO.read(new File("Images/car2d2.png"));
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
        
        createFinishLine(g2d, vecOutBorder);
        
        g2d.setPaint(grasstp);
        
        createFinishLine(g2d, vecInBorder);
        
        g2d.setPaint(finishtp);
        
        createFinishLine(g2d, finishCoords);
   
        transformCar();
        
        
        AffineTransformOp op = new AffineTransformOp(carTrans, AffineTransformOp.TYPE_BILINEAR);
        // Drawing the rotated image at the required drawing locations
        g2d.drawImage(op.filter(bicar, null), (int)posImageCar.x, (int)posImageCar.y, null);
        
        g2d.setPaint(Color.blue);
        //g2d.drawLine((int)posCar.x, (int)posCar.y, (int)posCar.x+10, (int)posCar.y+10);
        
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
        
        System.out.println(carProp.getPosition().x);
        
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
      //We will need the last update time.
      double lastUpdateTime = System.nanoTime();
      //Store the last time we rendered.
      double lastRenderTime = System.nanoTime();
      
      //If we are able to get as high as this FPS, don't render again.
      final double TARGET_FPS = 60;
      final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;
      
      //Simple way of finding FPS.
      
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
