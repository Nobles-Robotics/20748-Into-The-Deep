package org.firstinspires.ftc.teamcode.teleop;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import org.firstinspires.ftc.teamcode.teleop.bot.*;
import dev.frozenmilk.dairy.core.FeatureRegistrar;
import org.firstinspires.ftc.teamcode.util.BulkReads;

@Mercurial.Attach
@Bot.Attach
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
        Mercurial.gamepad1().a().onTrue(new Sequential(Slides.goTo(3500)));
        Mercurial.gamepad1().b().onTrue(new Sequential(Slides.goTo(0)));
        Mercurial.gamepad1().x().onTrue(new Sequential(Slides.resetEncoder()));
        Mercurial.gamepad1().y().onTrue(new Sequential(Slides.climb()));

        //Manual Slide Control
        Mercurial.gamepad1().leftStickButton().onTrue(new Sequential(Slides.setManual(true)));
        Mercurial.gamepad1().leftStickButton().onFalse(new Sequential(Slides.setManual(false)));


        Mercurial.gamepad1().leftBumper().onTrue(new Sequential(Arm.runIntake()));
        Mercurial.gamepad1().rightBumper().onTrue(new Sequential(Arm.releaseIntake()));

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
        telemetry.addData("Lift Pos", Slides.getLiftPosition());
        telemetry.addData("Actual Lift Pos", Slides.getActualLiftPosition());
        telemetry.addData("lift power", Slides.getPower());
        telemetry.addData("Wrist Position", Arm.getWrist());
        telemetry.update();
    }

    @Override
    public void stop() {
        // the rest is as normal
    }
}


