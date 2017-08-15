# autoNEAT

This is an algorithm to make a car learn to drive by NEAT (Neural Evolution by Augmenting Topology).
The goal of the algorithm is make the car reach the finish line:

![NEAT](https://github.com/lucasbsimao/autoNEAT/blob/master/Images/project/car.png)
![NEAT](https://github.com/lucasbsimao/autoNEAT/blob/master/Images/project/car2.png)

## Editing the road

You can edit the road to test the algorithm thou diferent paths. In Drawing.java, you set any segments of the road you want, as shown below:

```java
private void createPath(){
    ...
    midPoints.add(new Vec2(140,60));
    try{
        //addRoadSegment(float lenghtRoadStraight, float radiusCurve, float curveDegree);
        addRoadSegment(300, 70, 90);
        addRoadSegment(300, 50, 90);
        addRoadSegment(200, 80, 180);
        addRoadSegment(30, 50,-180);
        
        createBorders();
        createFinishBorders();
        
    }catch(Exception ex){
        System.out.println(ex.toString());
    } 
    ...
}
```

## NEAT Algorithm

The algorithm uses the six sensors in the agent, shown in the figure above, to develop an optimized crash avoidance logic. As in all genetic algorithms, it evolves throught the try and erros until it would able to reach the finish line.

Each colision gives an penalty of -800, while the positive score is calculate by how much the agent moved towards finish line direction.

It usually takes 2 or 3 epochs to complete the circuit. An example is seeing below:

| ------------- |:-------------:|
| Epoch 1 | ![Method-0](https://github.com/lucasbsimao/autoNEAT/blob/master/Images/project/evolv-car.png) |
| Epoch 2 | ![Method-0](https://github.com/lucasbsimao/autoNEAT/blob/master/Images/project/evolv-car2.png) |
| Epoch 3 | ![Method-0](https://github.com/lucasbsimao/autoNEAT/blob/master/Images/project/evolv-car3.png) |