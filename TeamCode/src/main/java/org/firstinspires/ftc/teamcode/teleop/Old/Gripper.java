/*
THIS FILE CONTAINS COLLECTION OF OLD CODE TO BE REPLACED
 */
package org.firstinspires.ftc.teamcode.teleop.Old;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Gripper {

    public static Bot instance;
    public static Gripper gripperInstance;
    public OpMode opMode;
    public final ServoEx servoGripperR, servoGripperL;
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

        servoGripperL = new SimpleServo(
                opMode.hardwareMap, "servoGripperL", 0, 0,
                AngleUnit.DEGREES
        );
        servoGripperR = new SimpleServo(
                opMode.hardwareMap, "servoGripperR", 0, 0,
                AngleUnit.DEGREES
        );
    }

    public void initializeGripper() {
        servoGripperL.setInverted(false);
        servoGripperR.setInverted(false);
    }
}
