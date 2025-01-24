package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import org.firstinspires.ftc.teamcode.auto.PinPointDrive;
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
    private PinPointDrive drive;
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
        drive = new PinPointDrive(hardwareMap, initialPose);
        driveAction = drive.actionBuilder(initialPose)
                .stopAndAdd(new MercurialAction(new Sequential(Slides.goTo(3000))))
                .strafeTo(new Vector2d(0, -43))
                .stopAndAdd(new MercurialAction(new Sequential(Slides.goTo(0))))

                .strafeTo(new Vector2d(0, -34))

                .splineToConstantHeading(new Vector2d(24, -36), Math.toRadians(0))

                .splineToConstantHeading(new Vector2d(specimen1, specimenMax), Math.toRadians(0))
                .strafeTo(new Vector2d(specimen1, specimenClose))
                .strafeToSplineHeading(new Vector2d(specimen1, specimenReturn), Math.toRadians(-90)) //reverse direction

                .splineToConstantHeading(new Vector2d(specimen2, specimenMax), Math.toRadians(0))
                .strafeTo(new Vector2d(specimen2, specimenClose))
                .strafeTo(new Vector2d(specimen2, specimenReturn))

                .splineToConstantHeading(new Vector2d(specimen3, specimenMax), Math.toRadians(0))
                .strafeTo(new Vector2d(specimen3, specimenClose))

                .strafeTo(new Vector2d(specimenPickup, wall))

                .strafeToSplineHeading(new Vector2d(specimenRung, specimenRung2-10), Math.toRadians(90))
                .strafeTo(new Vector2d(specimenRung, specimenRung2))
                .strafeTo(new Vector2d(specimenRung, specimenRung2-10))

                .strafeToSplineHeading(new Vector2d(specimenPickup, wall), Math.toRadians(-90))

                .strafeToSplineHeading(new Vector2d(specimenRung+3, specimenRung2-10), Math.toRadians(90))
                .strafeTo(new Vector2d(specimenRung+3, specimenRung2))
                .strafeTo(new Vector2d(specimenRung+3, specimenRung2-10))

                .strafeToSplineHeading(new Vector2d(specimenPickup, wall), Math.toRadians(-90))

                .strafeToSplineHeading(new Vector2d(specimenRung+6, specimenRung2-10), Math.toRadians(90))
                .strafeTo(new Vector2d(specimenRung+6, specimenRung2))
                .strafeTo(new Vector2d(specimenRung+6, specimenRung2-10))

                .strafeToSplineHeading(new Vector2d(specimenPickup, wall), Math.toRadians(-90))

                .strafeToSplineHeading(new Vector2d(specimenRung+9, specimenRung2-10), Math.toRadians(90))
                .strafeTo(new Vector2d(specimenRung+9, specimenRung2))
                .strafeTo(new Vector2d(specimenRung+9, specimenRung2-10))
                .build();
    }
    @Override
    public void start(){
        SilkRoad.runAsync(driveAction);
    }

    @Override
    public void loop() {
        telemetry.addData("x", drive.pose.position.x);
        telemetry.addData("y", drive.pose.position.y);
        telemetry.addData("heading (deg)", Math.toDegrees(drive.pose.heading.toDouble()));
        telemetry.update();
    }
}
;
