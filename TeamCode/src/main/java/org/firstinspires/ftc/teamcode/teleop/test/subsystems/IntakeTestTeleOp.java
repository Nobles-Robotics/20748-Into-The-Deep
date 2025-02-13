package org.firstinspires.ftc.teamcode.teleop.test.subsystems;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import org.firstinspires.ftc.teamcode.teleop.bot.Bot;
import org.firstinspires.ftc.teamcode.teleop.bot.Intake;
import org.firstinspires.ftc.teamcode.util.Features.BulkReads;


@Mercurial.Attach
@Intake.Attach
@BulkReads.Attach
@TeleOp(name = "IntakeTestTeleOp")
public class IntakeTestTeleOp extends OpMode {

    BoundGamepad test;

    @Override
    public void init() {
        Bot.init();
        test = Mercurial.gamepad1();

        test.leftStickY().conditionalBindState().greaterThanEqualTo(0.5).bind().onTrue(
                Intake.runManual(10)
        );

        test.leftStickY().conditionalBindState().lessThanEqualTo(-0.5).bind().onTrue(
                Intake.runManual(-10)
        );

        test.x().onTrue(
                Intake.raiseIntake()
        );
        test.y().onTrue(
                Intake.dropIntake()
        );
        test.b().onTrue(
                Intake.storeIntake()
        );
        test.dpadUp().onTrue(
                Intake.spintake(1)
        );
        test.dpadDown().onTrue(
                Intake.spintake(-1)
        );
        test.dpadRight().or(test.dpadLeft()).onTrue(
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

