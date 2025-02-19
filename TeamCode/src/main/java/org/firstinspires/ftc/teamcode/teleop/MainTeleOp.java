package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
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
        Bot.init();
        BoundGamepad alex = Mercurial.gamepad1();
        BoundGamepad jeff = Mercurial.gamepad2();



        alex.rightBumper().onTrue(Bot.setState(Bot.State.INTAKE_SAM));
        alex.leftBumper().onTrue(Bot.setState(Bot.State.OUTTAKE_SAM));

//        jeff.rightBumper().onTrue(Bot.setState(Bot.State.INTAKE_SPEC));
//        jeff.leftBumper().onTrue(Bot.setState(Bot.State.HOME));

        alex.leftStickButton().and(alex.leftStickY().conditionalBindState().greaterThanEqualTo(0.5).bind()).whileTrue(
                Arm.setPower(0.5)
        );
        alex.leftStickButton().and(alex.leftStickY().conditionalBindState().lessThanEqualTo(-0.5).bind()).whileFalse(
                Arm.setPower(-0.5)
        );

        alex.rightTrigger().conditionalBindState().greaterThanEqualTo(0.5).bind().onTrue(
                Drive.slow()
        );
        alex.leftTrigger().conditionalBindState().greaterThanEqualTo(0.5).bind().onTrue(
                Drive.fast()
        );

        alex.a().or(jeff.a()).onTrue(
                //Bot.setState(Bot.State.INTAKE_SPEC)
                new Parallel(
                        Slides.runToPosition(Slides.wall),
                        Gripper.close()
                )
        );

        alex.b().or(jeff.b()).onTrue(
                new Parallel(
                        Slides.runToPosition(Slides.wall),
                        Gripper.open()
                )
        );

        alex.x().or(jeff.x()).onTrue(
                new Parallel(
                        Gripper.close(),
                        Slides.runToPosition(Slides.highPos)
                )
        );

        alex.y().or(jeff.y()).onTrue(
                new Sequential(
                        Slides.runToPosition(Slides.scoreHighPos),
                        Gripper.open()
                )
        );


//        alex.x().or(jeff.x()).onTrue(
//                Bot.setState(Bot.State.OUTTAKE_LOW)
//        );
//        alex.y().or(jeff.y()).onTrue(
//                Bot.setState(Bot.State.SCORE_LOW)
//        );


        jeff.leftStickY().conditionalBindState().greaterThanEqualTo(0.5).bind().whileTrue(
                Slides.setPowerUp(1)
        );
        jeff.leftStickY().conditionalBindState().lessThanEqualTo(-0.5).bind().whileTrue(
                Slides.setPowerUp(-1)
        );
        jeff.leftStickY().conditionalBindState().lessThan(0.5).greaterThan(-0.5).bind().whileTrue(
                Slides.setPowerUp(0)
        );
        jeff.rightStickY().conditionalBindState().greaterThanEqualTo(0.5).bind().whileTrue(
                Slides.setPowerDown(1)
        );
        jeff.rightStickY().conditionalBindState().lessThanEqualTo(-0.5).bind().whileTrue(
                Slides.setPowerDown(-1)
        );
        jeff.rightStickY().conditionalBindState().lessThan(0.5).greaterThan(-0.5).bind().whileTrue(
                Slides.setPowerDown(0)
        );

        jeff.dpadDown().onTrue(
                Slides.resetCommand()
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
        Arm.logTele(Bot.Logging.NORMAL);
        Gripper.logTele(Bot.Logging.NORMAL);
        Intake.logTele(Bot.Logging.NORMAL);
        Slides.logTele(Bot.Logging.NORMAL);
        telemetry.update();
    }

    @Override
    public void stop() {
        // the rest is as normal
    }
}


