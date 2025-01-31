package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.frozenmilk.dairy.core.FeatureRegistrar;
import org.firstinspires.ftc.teamcode.teleop.bot.ColorSensor;
import org.firstinspires.ftc.teamcode.util.RevColorSensor;

//@Mercurial.Attach
//@Drive.Attach
//@Slides.Attach
//@BulkReads.Attach
//@Gripper.Attach
//@Arm.Attach
@ColorSensor.Attach
@TeleOp(name = "MainTeleOp")
public class MainTeleOp extends OpMode {
    private RevColorSensor colorSensorTest;

    public MainTeleOp() {
        FeatureRegistrar.checkFeatures();
    }

    @Override
    public void init() {

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
    }

    @Override
    public void stop() {
        // the rest is as normal
    }
}


