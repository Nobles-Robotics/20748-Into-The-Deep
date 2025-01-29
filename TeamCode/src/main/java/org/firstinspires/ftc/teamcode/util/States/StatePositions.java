package org.firstinspires.ftc.teamcode.util.States;

public class StatePositions {

    public double armPos;
    public boolean isIntakeRunning;
    public boolean isGripperOpen;
    public double slidePos;
    public double intakePos;

    public StatePositions(double armPos, double slidePos, double intakePos, boolean isGripperOpen, boolean isIntakeRunning) {
        this.armPos = armPos;
        this.slidePos = slidePos;
        this.intakePos = intakePos;
        this.isGripperOpen = isGripperOpen;
        this.isIntakeRunning = isIntakeRunning;
    }
}
