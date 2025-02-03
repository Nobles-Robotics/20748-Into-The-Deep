package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;


@TeleOp(name = "TestTeleOp")
public class TestTeleOp extends OpMode {

    public static ColorSensor colorSensor;
    @Override
    public void init() {

        colorSensor = hardwareMap.get(ColorSensor.class, "color");
    }

    @Override
    public void init_loop() {
        // the rest is as normal

    }

    @Override
    public void start() {
        // the rest is as normal
    }

    @Override
    public void loop() {
        if (colorSensor.red() > 2000 && colorSensor.green() > 2000) {
            telemetry.addLine("Yellow");
        } else if (colorSensor.red() > 1500 && colorSensor.green() < 1150) {
            telemetry.addLine("Red");
        } else if (colorSensor.red() < 900 && colorSensor.green() > 1000) {
            telemetry.addLine("Blue");
        }
        telemetry.addData("Red", colorSensor.red());
        telemetry.addData("Green", colorSensor.green());
        telemetry.addData("Blue", colorSensor.blue());

        telemetry.update();

        //Red: red greatest
        //Yellow: green greatest (kinda green + red should be the same))
        //Blue: blue greatest

    }

    @Override
    public void stop() {
        // the rest is as normal
    }
}


