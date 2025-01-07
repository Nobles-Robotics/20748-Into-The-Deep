package org.firstinspires.ftc.teamcode.teleop.teleop.bot;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.util.MotionProfiler;

public class Gripper {

    public static Bot instance;
    public static Gripper gripperInstance;
    public OpMode opMode;
    public final MotorEx slide0;
    private PIDFController controller;
    double p,i,d,f;
    public static Gripper getInstance() {
        if (gripperInstance == null) {
            throw new IllegalStateException("tried to getInstance of Slides when uninitialized!");
        }
        return gripperInstance;
    }
    public static Gripper getInstance(OpMode opMode) {
        if (gripperInstance == null) {
            return gripperInstance = new Gripper(opMode);
        }
        gripperInstance.opMode = opMode;
        return gripperInstance;
    }
    public Gripper(OpMode opMode) {
        this.opMode = opMode;

        slide0 = new MotorEx(opMode.hardwareMap, "motorSlide0");
    }

    public void initializeGripper() {
        slide0.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        slide0.setInverted(false);
        slide0.setRunMode(Motor.RunMode.RawPower);
    }

    public void stopGripper() {
        slide0.set(0);
    }
}
