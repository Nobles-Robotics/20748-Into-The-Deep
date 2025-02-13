package org.firstinspires.ftc.teamcode.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import org.firstinspires.ftc.teamcode.teleop.bot.Arm;
import org.firstinspires.ftc.teamcode.teleop.bot.Bot;
import org.firstinspires.ftc.teamcode.teleop.bot.Intake;
import org.firstinspires.ftc.teamcode.util.Features.BulkReads;

@Mercurial.Attach
@Intake.Attach
@Arm.Attach
@BulkReads.Attach
@TeleOp(name = "CombinedIntakeTestTeleOp")
public class CombinedIntakeTestTeleOp extends OpMode {

    BoundGamepad test2;

    @Override
    public void init() {
        Bot.init();
        test2 = Mercurial.gamepad2();

        test2.leftStickY().conditionalBindState().greaterThanEqualTo(0.5).bind().onTrue(
                Intake.runManual(10)
        );

        test2.leftStickY().conditionalBindState().lessThanEqualTo(-0.5).bind().onTrue(
                Intake.runManual(-10)
        );

        test2.x().onTrue(
                Intake.raiseIntake()
        );
        test2.y().onTrue(
                Intake.dropIntake()
        );
        test2.b().onTrue(
                Intake.storeIntake()
        );
        test2.dpadUp().onTrue(
                Intake.spintake(1)
        );
        test2.dpadDown().onTrue(
                Intake.spintake(-1)
        );
        test2.dpadRight().or(test2.dpadLeft()).onTrue(
                Intake.spintake(0)
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
        Intake.logTele();
        telemetry.update();

    }

    @Override
    public void stop() {
        // the rest is as normal
    }
}
