package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import org.firstinspires.ftc.teamcode.auto.PinPointDrive;
import org.firstinspires.ftc.teamcode.teleop.bot.Arm;
import org.firstinspires.ftc.teamcode.teleop.bot.Drive;
import org.firstinspires.ftc.teamcode.teleop.bot.Gripper;
import org.firstinspires.ftc.teamcode.teleop.bot.Slides;
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
        Mercurial.gamepad1().x().onTrue(new Sequential(Slides.goTo(10000)));
        Mercurial.gamepad1().b().onTrue(new Sequential(Slides.goTo(0)));
        Mercurial.gamepad1().a().onTrue(new Sequential(Slides.resetEncoder()));
        Mercurial.gamepad1().y().onTrue(new Sequential(Slides.climb()));

        //Manual Slide Control
        Mercurial.gamepad1().leftStickButton().onTrue(new Sequential(Slides.setManual(true)));
        Mercurial.gamepad1().leftStickButton().onFalse(new Sequential(Slides.setManual(false)));

        Mercurial.gamepad1().leftBumper().onTrue(new Sequential(Arm.runIntake()));
        Mercurial.gamepad1().rightBumper().onTrue(new Sequential(Arm.releaseIntake()));

        Mercurial.gamepad1().back().whileTrue(new Sequential(Gripper.open()));
        Mercurial.gamepad1().back().onFalse(new Sequential(Gripper.close()));

        PinPointDrive ppdrive = new PinPointDrive(hardwareMap, new Pose2d(0,0,0));
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
        telemetry.update();
    }

    @Override
    public void stop() {
        // the rest is as normal
    }
}


