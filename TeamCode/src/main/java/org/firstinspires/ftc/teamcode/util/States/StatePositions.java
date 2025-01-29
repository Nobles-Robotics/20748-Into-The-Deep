package org.firstinspires.ftc.teamcode.util.States;

public class StatePositions {

    public double armPos;
    public double pivotPos;
    public boolean isClawOpen;
    public int slidePos;

    public StatePositions(double armPos, double pivotPos, boolean isClawOpen, int slidePos) {
        this.armPos = armPos;
        this.pivotPos = pivotPos;
        this.isClawOpen = isClawOpen;
        this.slidePos = slidePos;
    }
}
