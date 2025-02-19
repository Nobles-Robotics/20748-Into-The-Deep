package org.firstinspires.ftc.teamcode.teleop.test.subsystems;


import com.pedropathing.follower.FollowerConstants;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import dev.frozenmilk.mercurial.commands.Lambda;
import org.firstinspires.ftc.teamcode.teleop.bot.Bot;
import org.firstinspires.ftc.teamcode.teleop.bot.Drive;
import org.firstinspires.ftc.teamcode.util.Features.BulkReads;

@Mercurial.Attach
@Drive.Attach
@BulkReads.Attach
@TeleOp(name = "DriveTestTeleOp")
public class DriveTestTeleOp extends OpMode {
    BoundGamepad test;
    public static DcMotorEx fl, bl, fr, br;

    @Override
    public void init() {
        fl = hardwareMap.get(DcMotorEx.class, FollowerConstants.leftFrontMotorName);
        bl = hardwareMap.get(DcMotorEx.class, FollowerConstants.leftRearMotorName);
        fr = hardwareMap.get(DcMotorEx.class, FollowerConstants.rightFrontMotorName);
        br = hardwareMap.get(DcMotorEx.class, FollowerConstants.rightRearMotorName);
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

        test.a().onTrue(
                powertest()
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
        // the r
        //est is as normal
    }
    public static Lambda powertest() {
        return new Lambda("follow-path-chain")
                .setInit(() -> fl.setPower(1));
    }
}


