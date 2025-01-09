package org.firstinspires.ftc.teamcode.teleop.bot;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class Slides {
    public static Bot instance;
    public static Slides slideInstance;
    public OpMode opMode;
    public boolean slideStop = false;
    public final MotorEx motorSlideEL, motorSlideRL, motorSlideER, motorSlideRR;
    public MotorGroup slideE, slideR;

    private PIDFController controller;
    double p,i,d,f;
    public static Slides getInstance() {
        if (slideInstance == null) {
            throw new IllegalStateException("tried to getInstance of Slides when uninitialized!");
        }
        return slideInstance;
    }
    public static Slides getInstance(OpMode opMode) {
        if (slideInstance == null) {
            return slideInstance = new Slides(opMode);
        }
        slideInstance.opMode = opMode;
        return slideInstance;
    }
    public Slides(OpMode opMode) {
        this.opMode = opMode;

        motorSlideEL = new MotorEx(opMode.hardwareMap, "motorSlideEL");
        motorSlideRL = new MotorEx(opMode.hardwareMap, "motorSlideRL");
        motorSlideER = new MotorEx(opMode.hardwareMap, "motorSlideER");
        motorSlideRR = new MotorEx(opMode.hardwareMap, "motorSlideRR");

        //motors on Right = Leader | left = Follower
        MotorGroup slideE = new MotorGroup(motorSlideER, motorSlideEL);
        MotorGroup slideR = new MotorGroup(motorSlideRR, motorSlideRL);

    }

    public void initializeSlides() {
        slideE.setInverted(false);
        slideE.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        slideE.setRunMode(Motor.RunMode.RawPower);

        slideR.setInverted(false);
        slideR.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        slideE.setRunMode(Motor.RunMode.RawPower);

        stopSlideMotors();
    }

    public void stopSlideMotors() {
        slideE.set(0);
        slideR.set(0);
    }

    public void moveSlides(double power) {
        slideE.set(power);
    }

    public void runTo(double pos){
        controller = new PIDFController(p, i, d, f);
        controller.setTolerance(5,10);
        controller.setSetPoint(pos);
    }

    public double convertToTicks(double mm) {
        return Math.toDegrees(mm/20) * -537.7 / 360;
    }
    public void runToMM(double posMM) {
        posMM = Math.max(posMM, 0);
        posMM = Math.min(posMM, 720);
        runTo(convertToTicks(posMM));
    }
    public double getPosition() {
        return slideE.getCurrentPosition();
    }
    public double getMMPosition() {
        return Math.toRadians(getPosition() * 360 / -537.7) * 20;
    }

    public double getIKMMPosition() {
        return Math.toRadians(getPosition() * 360 / -537.7) * 20;
    }
}
