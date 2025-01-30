package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.mercurial.Mercurial;
import org.firstinspires.ftc.teamcode.teleop.bot.Arm;
import org.firstinspires.ftc.teamcode.teleop.bot.Drive;
import org.firstinspires.ftc.teamcode.teleop.bot.Gripper;
import org.firstinspires.ftc.teamcode.teleop.bot.Slides;
import org.firstinspires.ftc.teamcode.util.BulkReads;
import org.firstinspires.ftc.teamcode.util.RevDistanceSensor;

@Mercurial.Attach
@Drive.Attach
@Slides.Attach
@BulkReads.Attach
@Gripper.Attach
@Arm.Attach
@TeleOp(name = "MainTeleOp")
public class MainTeleOp extends OpMode {
    private RevDistanceSensor sensor;

    public MainTeleOp() {
        FeatureRegistrar.checkFeatures();
    }

    @Override
    public void init() {
        RevDistanceSensor sensor = new RevDistanceSensor();
        sensor.init(FeatureRegistrar.getActiveOpMode().hardwareMap, "sensor");
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
        telemetry.addData("sensor", sensor.getDistanceMM());

        telemetry.update();
    }

    @Override
    public void stop() {
        // the rest is as normal
    }
}


