package org.firstinspires.ftc.teamcode.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import org.firstinspires.ftc.teamcode.teleop.bot.Bot;
import org.firstinspires.ftc.teamcode.teleop.bot.Gripper;
import org.firstinspires.ftc.teamcode.teleop.bot.Slides;
import org.firstinspires.ftc.teamcode.util.Features.BulkReads;

@Mercurial.Attach
@Slides.Attach
@Gripper.Attach
//@Drive.Attach
@BulkReads.Attach
@TeleOp(name = "CombinedOuttakeTestTeleOp")
public class CombinedOuttakeTestTeleOp extends OpMode {

    BoundGamepad test, test2;

    @Override
    public void init() {
        Bot.init();

        test2 = Mercurial.gamepad2();

        test2.leftStickY().conditionalBindState().greaterThanEqualTo(0.5).bind().whileTrue(
                Slides.setPowerUp(1)
        );
        test2.leftStickY().conditionalBindState().lessThanEqualTo(-0.5).bind().whileTrue(
                Slides.setPowerUp(-1)
        );
        test2.leftStickY().conditionalBindState().lessThan(0.5).greaterThan(-0.5).bind().whileTrue(
                Slides.setPowerUp(0)
        );
        test2.rightStickY().conditionalBindState().greaterThanEqualTo(0.5).bind().whileTrue(
                Slides.setPowerDown(1)
        );
        test2.rightStickY().conditionalBindState().lessThanEqualTo(-0.5).bind().whileTrue(
                Slides.setPowerDown(-1)
        );
        test2.rightStickY().conditionalBindState().lessThan(0.5).greaterThan(-0.5).bind().whileTrue(
                Slides.setPowerDown(0)
        );
        test2.a().onTrue(
                Slides.resetCommand()
        );
        test2.b().onTrue(
                Slides.removeSlack()
        );
        test2.x().onTrue(
                Slides.runToPosition(1000)
        );
        test2.y().onTrue(
                Slides.runToPosition(900)
        );


        test2.dpadUp().onTrue(
                Gripper.close()
        );
        test2.dpadDown().onTrue(
                Gripper.open()
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
        Slides.logTele();
        Gripper.logTele();
        telemetry.update();

    }

    @Override
    public void stop() {
        // the rest is as normal
    }
}
