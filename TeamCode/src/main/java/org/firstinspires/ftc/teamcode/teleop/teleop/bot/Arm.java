package org.firstinspires.ftc.teamcode.teleop.teleop.bot;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.util.MotionProfiler;

public class Arm {

    public static Bot instance;
    public static Arm armInstance;
    public OpMode opMode;
    public final ServoEx servoArm, servoWrist;
    private PIDFController controller;
    double p,i,d,f;
    public static Arm getInstance() {
        if (armInstance == null) {
            throw new IllegalStateException("tried to getInstance of Slides when uninitialized!");
        }
        return armInstance;
    }
    public static Arm getInstance(OpMode opMode) {
        if (armInstance == null) {
            return armInstance = new Arm(opMode);
        }
        armInstance.opMode = opMode;
        return armInstance;
    }
    public Arm(OpMode opMode) {
        this.opMode = opMode;

        servoArm = new SimpleServo(
                opMode.hardwareMap, "servoArm", 0, 0,
                AngleUnit.DEGREES
        );
        servoWrist = new SimpleServo(
                opMode.hardwareMap, "servoWrist", 0, 0,
                AngleUnit.DEGREES
        );
    }

    public void initializeArm() {
        servoArm.setInverted(false);
        servoWrist.setInverted(false);
    }

    public void stopArm() {
    }
}
