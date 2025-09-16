package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import org.firstinspires.ftc.teamcode.teleop.bot.*;
import org.firstinspires.ftc.teamcode.util.Features.BulkReads;
import org.firstinspires.ftc.teamcode.util.Names;

@Mercurial.Attach
@Drive.Attach
@Slides.Attach
@BulkReads.Attach
@Gripper.Attach
@Intake.Attach
@TeleOp(name = "MainTeleOp")
public class MainTeleOp extends OpMode {

    Servo wrist2;
    @Override
    public void init() {

        Bot.init();
        BoundGamepad alex = Mercurial.gamepad1();
        BoundGamepad jeff = Mercurial.gamepad2();



        //alex.rightBumper().onTrue(Bot.setState(Bot.State.INTAKE_SAM));
        //alex.leftBumper().onTrue(Bot.setState(Bot.State.OUTTAKE_SAM));

//        jeff.rightBumper().onTrue(Bot.setState(Bot.State.INTAKE_SPEC));
//        jeff.leftBumper().onTrue(Bot.setState(Bot.State.HOME));




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

        alex.rightBumper().onTrue(
                Drive.slow()
        );
        alex.rightBumper().onFalse(
                Drive.fast()
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


