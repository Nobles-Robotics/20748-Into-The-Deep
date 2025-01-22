package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.auto.PinPointDrive;
import org.firstinspires.ftc.teamcode.teleop.bot.Arm;
import org.firstinspires.ftc.teamcode.teleop.bot.Drive;
import org.firstinspires.ftc.teamcode.teleop.bot.Gripper;
import org.firstinspires.ftc.teamcode.teleop.bot.Slides;
import org.firstinspires.ftc.teamcode.util.BulkReads;
import org.firstinspires.ftc.teamcode.util.MercurialAction;
import org.firstinspires.ftc.teamcode.util.SilkRoad;

import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;

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
    private PinPointDrive drive;
    private Action driveAction;
    double specimen1 = 46;
    double specimen2 = 56;
    double specimen3 = 63;
    double specimenMax = -10;
    double specimenClose = -65;
    double specimenReturn = -20;
    @Override
    public void init() {
        drive = new PinPointDrive(hardwareMap, initialPose);
        driveAction = drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(0, -43))
                .strafeTo(new Vector2d(0, -34))
                .splineToConstantHeading(new Vector2d(24, -36), Math.toRadians(0))

                .splineToConstantHeading(new Vector2d(specimen1, specimenMax), Math.toRadians(0))
                .strafeTo(new Vector2d(specimen1, specimenClose))
                .strafeTo(new Vector2d(specimen1, specimenReturn))

                .splineToConstantHeading(new Vector2d(specimen2, specimenMax), Math.toRadians(0))
                .strafeTo(new Vector2d(specimen2, specimenClose))
                .strafeTo(new Vector2d(specimen2, specimenReturn))

                .splineToConstantHeading(new Vector2d(specimen3, specimenMax), Math.toRadians(0))
                .strafeTo(new Vector2d(specimen3, specimenClose))
                .build();
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
