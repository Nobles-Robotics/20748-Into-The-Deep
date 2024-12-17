package org.firstinspires.ftc.teamcode.teleop.teleop.bot;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.util.MotionProfiler;

public class Slides {

    public static Bot instance;
    public static Slides slideInstance;
    public OpMode opMode;
    private final MotorEx slide0;
    private PIDFController controller;
    double tolerance = 10;
    private MotionProfiler profiler = new MotionProfiler(maxVelo, maxAccel);
    public static double maxVelo = 30000, maxAccel = 20000;
    private final double powerUp = 0.1;
    private double powerDown = 0.05;
    private final double manualDivide = 2;
    private final double powerMin = 0.1;
    public double manualPower = 0;
    public boolean profiling;
    public double power;
    double p,i,d,f;
    private double target = 0;
    private boolean goingDown = false;
    private double profile_init_time = 0;

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

        slide0 = new MotorEx(opMode.hardwareMap, "motorSlide0");
    }

    public void initializeSlides() {
        slide0.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        slide0.setInverted(false);
        slide0.setRunMode(Motor.RunMode.RawPower);
    }

    public void stopSlideMotors() {
        slide0.set(0);
    }

    public void moveSlides(double power) {
        slide0.set(power);
    }

    public void runTo(double pos) {
        slide0.setRunMode(Motor.RunMode.RawPower);
        slide0.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);


        controller = new PIDFController(p, i, d, f);

        controller.setTolerance(tolerance);
        if (manualPower == 0) {
            resetProfiler();
            profiler.init_new_profile(slide0.getCurrentPosition(), pos);
            profile_init_time = opMode.time;
            profiling = true;
        }
        goingDown = pos > target;
        target = pos;
    }

    public void runManual(double manual) {
        if (manual > powerMin || manual < -powerMin) {
            manualPower = -manual;
        } else {
            manualPower = 0;
        }
    }
    public void periodic(double pivotAngleRadians) {
        slide0.setInverted(false);
        controller.setPIDF(p, i, d, f);
        double dt = opMode.time - profile_init_time;
        if (!profiler.isOver()) {
            controller.setSetPoint(profiler.motion_profile_pos(dt));
            power = powerUp * controller.calculate(slide0.getCurrentPosition());
            if (goingDown) {
                powerDown = powerUp - (0.05 * Math.sin(pivotAngleRadians));
                power = powerDown * controller.calculate(slide0.getCurrentPosition());
            }
        } else {
            if (profiler.isDone()) {
                resetProfiler();
                profiling = false;
            }
            controller.setSetPoint(slide0.getCurrentPosition());
            power = manualPower / manualDivide;
        }
        slide0.set(power);
    }
    public void resetProfiler() {
        profiler = new MotionProfiler(maxVelo, maxAccel);
    }
    public double convert2Ticks(double mm) {
        return Math.toDegrees(mm/20) * -537.7 / 360;
    }
    public void runToMM(double posMM) {
        posMM = Math.max(posMM, 0);
        posMM = Math.min(posMM, 720);
        runTo(convert2Ticks(posMM));
    }
    public double getPosition() {
        return slide0.getCurrentPosition();
    }
    public double getmmPosition() {
        return Math.toRadians(getPosition() * 360 / -537.7) * 20;
    }

    public double getIKmmPosition() {
        return Math.toRadians(getPosition() * 360 / -537.7) * 20;
    }
}
