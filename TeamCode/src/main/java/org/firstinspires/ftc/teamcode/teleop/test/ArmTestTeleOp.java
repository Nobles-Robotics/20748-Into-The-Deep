package org.firstinspires.ftc.teamcode.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import org.firstinspires.ftc.teamcode.teleop.bot.Arm;
import org.firstinspires.ftc.teamcode.teleop.bot.Bot;
import org.firstinspires.ftc.teamcode.util.Features.BulkReads;


@Mercurial.Attach
@Arm.Attach
@BulkReads.Attach
@TeleOp(name = "ArmTestTeleOp")
public class ArmTestTeleOp extends OpMode {

    BoundGamepad test;

    @Override
    public void init() {
        Bot.init();
        test = Mercurial.gamepad1();

        test.leftStickY().conditionalBindState().greaterThanEqualTo(0.5).bind().whileTrue(
                Arm.setPowerManual(0.5)
        );
        test.leftStickY().conditionalBindState().lessThanEqualTo(-0.5).bind().whileTrue(
                Arm.setPowerManual(-0.5)
        );

        test.a().onTrue(
                Arm.resetCommand()
        );

        test.x().onTrue(
                Arm.runToPosition(0)
        );
        test.y().onTrue(
                Arm.runToPosition(1000)
        );
        test.dpadUp().onTrue(
                Arm.extend()
        );
        test.dpadDown().onTrue(
                Arm.home()
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
        Arm.logTele();
        telemetry.update();

    }

    @Override
    public void stop() {
        // the rest is as normal
    }
}

