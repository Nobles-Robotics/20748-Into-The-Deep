package org.firstinspires.ftc.teamcode.util;

public class MotionProfiler {
    private double maxV, maxA;
    private double tempV, tempA;
    private double startPos, finalPos;
    private double distance, distanceHalf;
    private double cruiseDistance, cruisedT;
    private double accelerationdT, accelerationDistance;
    private double deaccelerationdT, deaccelerationTime;
    private double entiredT;
    private double timeElapsed;
    private boolean isOver, isDone;

    public MotionProfiler(double maxV, double maxA){
        this.maxV = maxV;
        this.maxA = maxA;
    }

    public void initialize(double startPos, double finalPos){
        this.startPos = startPos;
        this.finalPos = finalPos;
        distance = finalPos - startPos;

        //Check if going up or down & negate if going down | assign if going up
        if (distance < 0){ tempV = -maxV; tempA = -maxA;
        } else if (distance > 0){ tempV = maxV; tempA = maxA; }

        accelerationdT = tempV / tempA;

        distanceHalf = distance / 2;
        accelerationDistance = tempA * accelerationdT * accelerationdT / 2;

        //If we can't accelerate to maxV in time - recaulcuate accelerationDistance
        if (Math.abs(accelerationDistance) > Math.abs(distanceHalf)){
            accelerationdT = Math.sqrt(distanceHalf / (0.5 * tempA));
            accelerationDistance = tempA * accelerationdT * accelerationdT / 2;
        }

        maxV = maxA * accelerationdT;
        deaccelerationdT = accelerationdT;

        cruiseDistance = distance - 2 * accelerationDistance;
        cruisedT = cruiseDistance / maxV;
        deaccelerationTime = accelerationdT + cruisedT;

        entiredT = accelerationdT + cruisedT + deaccelerationdT;
    }

    public double pos(double timeElapsed){
        if (timeElapsed > entiredT){ isOver = true; isDone = true; return finalPos;}
        if (timeElapsed < accelerationdT){ return startPos + 0.5 * tempA * timeElapsed * timeElapsed;}
        if (timeElapsed < deaccelerationdT){
            accelerationDistance = 0.5 * tempA * accelerationdT * accelerationdT;
            double cruisedT = timeElapsed - accelerationdT;

            return startPos + accelerationDistance + maxV * cruisedT;
        }
        accelerationDistance = 0.5 * tempA * accelerationdT * accelerationdT;
        cruiseDistance = maxV * cruisedT;
        deaccelerationTime = timeElapsed - deaccelerationTime;
        return startPos + accelerationDistance + cruiseDistance + maxV * deaccelerationTime - 0.5 * tempA * deaccelerationTime * deaccelerationTime;
    }

    public double velocity() {
        if (timeElapsed > entiredT){ return 0;}
        if (timeElapsed < accelerationdT){ return tempA * timeElapsed; }
        if (timeElapsed < deaccelerationdT){ return maxV; }
        deaccelerationTime = timeElapsed - deaccelerationTime;
        return maxV - deaccelerationTime;
    }

    public double acceleration() {
        if (timeElapsed > entiredT){ return 0; }
        if (timeElapsed < accelerationdT){ return tempA; }
        if (timeElapsed < deaccelerationTime){ return 0; }
        return -tempA;
    }

    public boolean isDone() { return isDone; }
    public boolean isOver() { return isOver; }
}


