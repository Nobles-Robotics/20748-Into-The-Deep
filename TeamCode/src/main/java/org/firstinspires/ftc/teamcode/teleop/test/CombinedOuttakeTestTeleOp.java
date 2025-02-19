package org.firstinspires.ftc.teamcode.teleop.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.teleop.bot.Bot;
import org.firstinspires.ftc.teamcode.teleop.bot.Drive;
import org.firstinspires.ftc.teamcode.teleop.bot.Gripper;
import org.firstinspires.ftc.teamcode.teleop.bot.Slides;
import org.firstinspires.ftc.teamcode.util.Features.BulkReads;
import org.firstinspires.ftc.teamcode.util.Features.LoopTimes;

@Mercurial.Attach
@Slides.Attach
@Gripper.Attach
@Drive.Attach
@BulkReads.Attach
@LoopTimes.Attach
@TeleOp(name = "CombinedOuttakeTestTeleOp")
public class CombinedOuttakeTestTeleOp extends OpMode {

    BoundGamepad test2;
    Telemetry dashboardTelemetry;

    @Override
    public void init() {
        Bot.init();

        dashboardTelemetry = FtcDashboard.getInstance().getTelemetry();


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
                Slides.runToPosition(Slides.wall)
        );
        test2.x().onTrue(
                new Sequential(
                        Gripper.close(),
                        Slides.runToPosition(Slides.highPos)
                )
        );
        test2.y().onTrue(
                new Sequential(
                        Slides.runToPosition(Slides.scoreHighPos),
                        Gripper.open()
                )
        );

        test2.dpadUp().onTrue(
                Gripper.close()
        );
        test2.dpadDown().onTrue(
                Gripper.open()
        );

        test2.dpadRight().onTrue(
                Gripper.toggle()
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
        Gripper.logTele(Bot.Logging.VERBOSE);
        telemetry.update();
        dashboardTelemetry.update();
    }

    @Override
    public void stop() {
        // the rest is as normal
    }
}
