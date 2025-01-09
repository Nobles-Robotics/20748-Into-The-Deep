/*
THIS FILE CONTAINS COLLECTION OF OLD CODE TO BE REPLACED
 */

package org.firstinspires.ftc.teamcode.teleop.Old;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.CRServo;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Arm {

    public static Bot instance;
    public static Arm armInstance;
    public OpMode opMode;
    public final ServoEx servoWrist;
    public final CRServo servoArm;
    public final MotorEx motorArmE, motorArmR;
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
        servoArm = new CRServo(opMode.hardwareMap, "servoArm");
        servoWrist = new SimpleServo(
                opMode.hardwareMap, "servoWrist", 0, 0,
                AngleUnit.DEGREES
        );
        motorArmE = new MotorEx(opMode.hardwareMap, "motorArmE");
        motorArmR = new MotorEx(opMode.hardwareMap, "motorArmR");
    }

    public void initializeArm() {
        servoArm.setInverted(false);
        servoWrist.setInverted(false);
    }

}
