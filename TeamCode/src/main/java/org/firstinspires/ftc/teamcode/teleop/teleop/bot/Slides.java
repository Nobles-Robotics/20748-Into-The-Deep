package org.firstinspires.ftc.teamcode.teleop.teleop.bot;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.util.MotionProfiler;

public class Slides {
    public static Bot instance;
    public static Slides slideInstance;
    public OpMode opMode;
    public boolean slideStop = false;
    public final MotorEx motorSlideEL, motorSlideRL, motorSlideER, motorSlideRR;
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
        MotorGroup SlideE = new MotorGroup(motorSlideER, motorSlideEL);
        MotorGroup SlideR = new MotorGroup(motorSlideRR, motorSlideRL);

    }

    public void initializeSlides() {

    }

    public void stopSlideMotors() {
        slide0.set(0);
    }

    public void moveSlides(double power) {
        slide0.set(power);
    }

    public void runTo(double pos){
        controller = new PIDFController(p, i, d, f);
        controller.setTolerance(5,10);
        controller.setSetPoint(pos);

        while (!controller.atSetPoint() && !slideStop){
            double output = controller.calculate(slide0.getCurrentPosition());
            slide0.setVelocity(output);
        }
        slide0.stopMotor();
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
        return slide0.getCurrentPosition();
    }
    public double getMMPosition() {
        return Math.toRadians(getPosition() * 360 / -537.7) * 20;
    }

    public double getIKMMPosition() {
        return Math.toRadians(getPosition() * 360 / -537.7) * 20;
    }
}
