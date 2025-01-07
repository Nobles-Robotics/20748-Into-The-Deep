package org.firstinspires.ftc.teamcode.teleop.teleop.bot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class Bot {

    public static Bot instance;
    public OpMode opMode;
    private double heading = 0.0;

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

        });
        thread.start();
    }
     */

    public void idle(){
        Thread thread = new Thread(() -> {

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
    }
}

