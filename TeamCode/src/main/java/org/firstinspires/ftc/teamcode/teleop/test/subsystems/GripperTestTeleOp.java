package org.firstinspires.ftc.teamcode.teleop.test.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import org.firstinspires.ftc.teamcode.teleop.bot.Bot;
import org.firstinspires.ftc.teamcode.teleop.bot.Gripper;
import org.firstinspires.ftc.teamcode.util.Features.BulkReads;


@Mercurial.Attach
@Gripper.Attach
@BulkReads.Attach
@TeleOp(name = "GripperTestTeleOp")
public class GripperTestTeleOp extends OpMode {

    BoundGamepad test;

    @Override
    public void init() {
        Bot.init();
        test = Mercurial.gamepad1();

        test.leftStickY().conditionalBindState().greaterThanEqualTo(0.5).bind().onTrue(
                Gripper.runManual(10)
        );
        test.leftStickY().conditionalBindState().lessThanEqualTo(-0.5).bind().onTrue(
                Gripper.runManual(-10)
        );

        test.x().onTrue(
                Gripper.close()
        );
        test.y().onTrue(
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
        Gripper.logTele();
        telemetry.update();

    }

    @Override
    public void stop() {
        // the rest is as normal
    }
}

