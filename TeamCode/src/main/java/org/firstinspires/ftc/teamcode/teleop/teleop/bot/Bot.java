package org.firstinspires.ftc.teamcode.teleop.teleop.bot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class Bot {

    public static Bot instance;
    public OpMode opMode;
    public State state = State.IDLE;

    public Slides slides;
    public Gripper gripper;
    public Arm arm;
    public Drive drive;

    public enum State {
        IDLE,
        HIGH_BUCKET,
        HIGH_RUNG,
        WALL_INTAKE,
        ARM_INTAKE,
        GRIPPER_INTAKE,

    }
    /*
    public void idle(){
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}
        });
        thread.start();
    }
     */

    public void idle(){
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}
        });
        thread.start();
    }

    public void highBucket(){
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(100);
                state = State.HIGH_BUCKET;
            } catch (InterruptedException ignored) {}
        });
        thread.start();
    }

    public void highRung(){
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(100);
                state = State.HIGH_RUNG;
            } catch (InterruptedException ignored) {}
        });
        thread.start();
    }

    public void wallIntake(){
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(100);
                state = State.WALL_INTAKE;
            } catch (InterruptedException ignored) {}
        });
        thread.start();
    }

    public void armIntake(){
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(100);
                state = State.ARM_INTAKE;
            } catch (InterruptedException ignored) {}
        });
        thread.start();
    }

    public void gripperIntake(){
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(100);
                state = State.GRIPPER_INTAKE;
            } catch (InterruptedException ignored) {}
        });
        thread.start();
    }

    public static Bot getInstance() {
        if (instance == null) {
            throw new IllegalStateException("tried to getInstance of Bot when uninitialized!");
        }
        return instance;
    }

    public static Bot getInstance(OpMode opMode) {
        if (instance == null) {
            return instance = new Bot(opMode);
        }
        instance.opMode = opMode;
        return instance;
    }

    private Bot(OpMode opMode) {
        this.opMode = opMode;

        drive = new Drive(opMode);
        gripper = new Gripper(opMode);
        slides = new Slides(opMode);
        arm = new Arm(opMode);
    }

    public void initializeBot() {
        drive.initializeDrive();
        slides.initializeSlides();
        arm.initializeArm();
        gripper.initializeGripper();
        state = State.IDLE;
    }

    public void driveFieldCentric(double strafeSpeed, double forwardBackSpeed, double turnSpeed){
        drive.driveFieldCentric(strafeSpeed, forwardBackSpeed, turnSpeed);
    }
    public void driveRobotCentric(double strafeSpeed, double forwardBackSpeed, double turnSpeed){
        drive.driveRobotCentric(strafeSpeed, forwardBackSpeed, turnSpeed);
    }
}

