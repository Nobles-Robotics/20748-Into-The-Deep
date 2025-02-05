package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import org.firstinspires.ftc.teamcode.teleop.bot.Slides;
import org.firstinspires.ftc.teamcode.util.Features.BulkReads;


@Mercurial.Attach
@Slides.Attach
@BulkReads.Attach
@TeleOp(name = "TestTeleOp")
public class TestTeleOp extends OpMode {


    @Override
    public void init() {
        BoundGamepad test = Mercurial.gamepad1();
        test.leftStickY().conditionalBindState().greaterThanEqualTo(0.5).bind().onTrue(
                Slides.setPowerUp(0.5)
        );
        test.leftStickY().conditionalBindState().lessThanEqualTo(-0.5).bind().onTrue(
                Slides.setPowerDown(-0.5)
        );
        test.rightStickY().conditionalBindState().greaterThanEqualTo(0.5).bind().onTrue(
                Slides.setPowerUp(0.5)
        );
        test.rightStickY().conditionalBindState().lessThanEqualTo(-0.5).bind().onTrue(
                Slides.setPowerDown(-0.5)
        );
        test.a().onTrue(
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
        Slides.logTele();
        telemetry.update();

        //Red: red greatest
        //Yellow: green greatest (kinda green + red should be the same))
        //Blue: blue greatest

    }

    @Override
    public void stop() {
        // the rest is as normal
    }
}

