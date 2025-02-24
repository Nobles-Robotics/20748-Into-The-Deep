package org.firstinspires.ftc.teamcode.teleop.test.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import org.firstinspires.ftc.teamcode.teleop.bot.Bot;
import org.firstinspires.ftc.teamcode.teleop.bot.Slides;
import org.firstinspires.ftc.teamcode.util.Features.BulkReads;


@Mercurial.Attach
@Slides.Attach
@BulkReads.Attach
@TeleOp(name = "SlidesTestTeleOp")
public class SlidesTestTeleOp extends OpMode {

    BoundGamepad test;

    @Override
    public void init() {
        Bot.init();
        test = Mercurial.gamepad1();

        test.leftStickY().conditionalBindState().greaterThanEqualTo(0.5).bind().whileTrue(
                Slides.setPowerUp(1)
        );
        test.leftStickY().conditionalBindState().lessThanEqualTo(-0.5).bind().whileTrue(
                Slides.setPowerUp(-1)
        );
        test.leftStickY().conditionalBindState().lessThan(0.5).greaterThan(-0.5).bind().whileTrue(
                Slides.setPowerUp(0)
        );
        test.rightStickY().conditionalBindState().greaterThanEqualTo(0.5).bind().whileTrue(
                Slides.setPowerDown(1)
        );
        test.rightStickY().conditionalBindState().lessThanEqualTo(-0.5).bind().whileTrue(
                Slides.setPowerDown(-1)
        );
        test.rightStickY().conditionalBindState().lessThan(0.5).greaterThan(-0.5).bind().whileTrue(
                Slides.setPowerDown(0)
        );
        test.a().onTrue(
                Slides.resetCommand()
        );
        test.b().onTrue(
                Slides.runToPosition(Slides.wall)
        );
        test.x().onTrue(
                Slides.runToPosition(Slides.highPos)
        );
        test.y().onTrue(
                Slides.runToPosition(Slides.scoreHighPos)
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
        Slides.logTele(Bot.Logging.VERBOSE);
        telemetry.update();

    }

    @Override
    public void stop() {
        // the rest is as normal
    }
}

