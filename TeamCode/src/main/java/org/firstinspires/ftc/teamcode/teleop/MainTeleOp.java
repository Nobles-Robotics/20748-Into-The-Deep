package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import org.firstinspires.ftc.teamcode.teleop.bot.*;
import org.firstinspires.ftc.teamcode.util.Features.BulkReads;

@Mercurial.Attach
@Drive.Attach
@Slides.Attach
@BulkReads.Attach
@Gripper.Attach
@Arm.Attach
@Intake.Attach
@TeleOp(name = "MainTeleOp")
public class MainTeleOp extends OpMode {

    @Override
    public void init() {
        BoundGamepad alex = Mercurial.gamepad1();
        BoundGamepad jeff = Mercurial.gamepad2();

        alex.back().or(jeff.back()).onTrue(Bot.setState(Bot.State.HOME));

        alex.rightBumper().onTrue(Bot.setState(Bot.State.INTAKE_SAM));
        alex.leftBumper().onTrue(Bot.setState(Bot.State.OUTAKE_SAM));

        jeff.rightBumper().onTrue(Bot.setState(Bot.State.INTAKE_SPEC));
        jeff.leftBumper().onTrue(Bot.setState(Bot.State.HOME));

        alex.rightTrigger().conditionalBindState().greaterThanEqualTo(0.5).bind().onTrue(
                Arm.setPower(0.75)
        );
        alex.leftTrigger().conditionalBindState().greaterThanEqualTo(0.5).bind().onTrue(
                Arm.setPower(-0.75)
        );
        alex.a().or(jeff.a()).onTrue(
                Bot.setState(Bot.State.OUTTAKE_HIGH)
        );
        alex.b().or(jeff.b()).onTrue(
                Bot.setState(Bot.State.SCORE_HIGH)
        );
        alex.x().or(jeff.x()).onTrue(
                Bot.setState(Bot.State.OUTTAKE_LOW)
        );
        alex.y().or(jeff.y()).onTrue(
                Bot.setState(Bot.State.SCORE_LOW)
        );
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
        telemetry.update();
    }

    @Override
    public void stop() {
        // the rest is as normal
    }
}


