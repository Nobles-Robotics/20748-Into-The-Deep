package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import org.firstinspires.ftc.teamcode.auto.PinPointDrive;
import org.firstinspires.ftc.teamcode.teleop.bot.*;
import org.firstinspires.ftc.teamcode.util.BulkReads;

@Mercurial.Attach
@Drive.Attach
@Slides.Attach
@BulkReads.Attach
@Gripper.Attach
@Arm.Attach
@TeleOp(name = "MainTeleOp")
public class MainTeleOp extends OpMode {

    public MainTeleOp() {
        FeatureRegistrar.checkFeatures();
    }

    @Override
    public void init() {
        //Normal Slide Controller
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
        telemetry.addData("Slide", Slides.getLiftPosition());
        telemetry.addData("Slide Actual", Slides.getActualLiftPosition());
        telemetry.addData("Slide Power", Slides.getPower());
        telemetry.addData("Slide isManual", Slides.getManual());
        telemetry.addData("GripperL", Gripper.getPositionGripperL());
        telemetry.addData("GripperR", Gripper.getPositionGripperR());

        telemetry.update();
    }

    @Override
    public void stop() {
        // the rest is as normal
    }
}


