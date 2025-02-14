package org.firstinspires.ftc.teamcode.teleop.test.subsystems;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import org.firstinspires.ftc.teamcode.teleop.bot.Bot;
import org.firstinspires.ftc.teamcode.teleop.bot.Drive;
import org.firstinspires.ftc.teamcode.util.Features.BulkReads;

@Mercurial.Attach
@Drive.Attach
@BulkReads.Attach
@TeleOp(name = "DriveTestTeleOp")
public class DriveTestTeleOp extends OpMode {
    BoundGamepad test;

    @Override
    public void init() {
        Bot.init();
        test = Mercurial.gamepad1();

        test.leftBumper().onTrue(
                Drive.slow()
        );
        test.rightBumper().onTrue(
                Drive.fast()
        );

        test.dpadUp().onTrue(
            Drive.turnToCommand(0)
        );
        test.dpadDown().onTrue(
                Drive.turnToCommand(180)
        );
        test.dpadLeft().onTrue(
                Drive.turnToCommand(90)
        );
        test.dpadRight().onTrue(
                Drive.turnToCommand(-90)
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
        telemetry.update();
    }

    @Override
    public void stop() {
        // the rest is as normal
    }
}


