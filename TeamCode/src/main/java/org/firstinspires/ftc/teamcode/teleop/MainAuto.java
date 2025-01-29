package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import org.firstinspires.ftc.teamcode.teleop.bot.Arm;
import org.firstinspires.ftc.teamcode.teleop.bot.Drive;
import org.firstinspires.ftc.teamcode.teleop.bot.Gripper;
import org.firstinspires.ftc.teamcode.teleop.bot.Slides;
import org.firstinspires.ftc.teamcode.util.BulkReads;
import org.firstinspires.ftc.teamcode.util.MercurialAction;
import org.firstinspires.ftc.teamcode.util.SilkRoad;

@Autonomous(name="MainAuto")
@Mercurial.Attach
@Drive.Attach
@Slides.Attach
@BulkReads.Attach
@Gripper.Attach
@Arm.Attach
@SilkRoad.Attach
public class MainAuto extends OpMode {
    private final Pose2d initialPose = new Pose2d(-24, -62.5, Math.toRadians(90));
    private Action driveAction;
    double specimen1 = 46;
    double specimen2 = 56;
    double specimen3 = 63;
    double specimenMax = -10;
    double specimenClose = -57;
    double specimenReturn = -20;
    double specimenPickup = 45;
    double wall = -60;
    double specimenRung = 0;
    double specimenRung2 = -32;
    @Override
    public void init() {

    }
    @Override
    public void start(){
        SilkRoad.runAsync(driveAction);
    }

    @Override
    public void loop() {

    }
}
;
