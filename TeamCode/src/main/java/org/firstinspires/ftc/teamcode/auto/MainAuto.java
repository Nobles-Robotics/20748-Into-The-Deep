package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.Wait;
import org.firstinspires.ftc.teamcode.auto.Pathing.Paths;
import org.firstinspires.ftc.teamcode.teleop.bot.*;
import org.firstinspires.ftc.teamcode.util.Features.BulkReads;

@Mercurial.Attach
@Drive.Attach
@Slides.Attach
@BulkReads.Attach
@Gripper.Attach
@Arm.Attach
@Intake.Attach
@Autonomous
public class MainAuto extends OpMode {

    public static long intakeDelay = 250;
    public static double intakePower = -0.5;
    public static long outtakeDelay = 500;
    public static double outtakePower = 1;

    @Override
    public void init() {
        Bot.init();
        Gripper.runToPosition(Gripper.closePos);
        Slides.runToPosition(Slides.wall);
    }

    @Override
    public void loop() {
    }

    @Override
    public void start() {
        new Sequential(
                Slides.runToPosition(Slides.highPos),

                // Preload
                new Parallel(
                        Drive.followPath(Paths.fiveSpecs.get(0))
                ),
                new Parallel(
                        Drive.push(outtakePower, outtakeDelay),
                        Slides.runToPosition(Slides.scoreHighPos)
                ),

                // Pushing samples
                new Parallel(
                        Drive.followPath(Paths.fiveSpecs.get(1)),
                        new Sequential(
                                Gripper.runToPosition(Gripper.openPos),
                                new Wait(0.2),
                                Slides.runToPosition(Slides.wall)
                        )
                ),
                Drive.followPathChain(Paths.robotPush),
                Drive.followPath(Paths.fiveSpecs.get(7), true),

                // plus 1 intake
                Drive.push(intakePower, intakeDelay),
                Gripper.runToPosition(Gripper.closePos),

                // plus 1 outtake
                new Parallel(
                        Drive.followPath(Paths.fiveSpecs.get(8)),
                        Slides.runToPosition(Slides.highPos)
                ),
                new Parallel(
                        Drive.push(outtakePower, outtakeDelay),
                        Slides.runToPosition(Slides.scoreHighPos)
                ),
//                Drive.followPath(Paths.fiveSpecs.get(9)),
                Gripper.runToPosition(Gripper.openPos),

                // plus 2 intake
                new Parallel(
                        Drive.followPath(Paths.fiveSpecs.get(10)),
                        new Sequential(
                                Gripper.runToPosition(Gripper.openPos),
                                new Wait(0.2),
                                Gripper.runToPosition(Gripper.closePos)
                        )
                ),
                Drive.push(intakePower, intakeDelay),
                Gripper.runToPosition(Gripper.closePos),

                // plus 2 outtake
                new Parallel(
                        Drive.followPath(Paths.fiveSpecs.get(11)),
                        Slides.runToPosition(Slides.highPos)

                ),
                new Parallel(
                        Drive.push(outtakePower, outtakeDelay),
                        Slides.runToPosition(Slides.scoreHighPos)

                ),
//                Drive.followPath(Paths.fiveSpecs.get(12)),
                Gripper.runToPosition(Gripper.openPos),

                // plus 3 intake
                new Parallel(
                        Drive.followPath(Paths.fiveSpecs.get(13)),
                        new Sequential(
                                Gripper.runToPosition(Gripper.openPos),
                                new Wait(0.2),
                                Gripper.runToPosition(Gripper.closePos)
                        )
                ),
                Drive.push(intakePower, intakeDelay),
                Gripper.runToPosition(Gripper.closePos),

                // plus 3 outtake
                new Parallel(
                        Drive.followPath(Paths.fiveSpecs.get(14)),
                        Slides.runToPosition(Slides.highPos)

                ),
                new Parallel(
                        Drive.push(outtakePower, outtakeDelay),
                        Slides.runToPosition(Slides.scoreHighPos)

                ),
//                Drive.followPath(Paths.fiveSpecs.get(15)),
                Gripper.runToPosition(Gripper.openPos),

                // plus 4 intake
                new Parallel(
                        Drive.followPath(Paths.fiveSpecs.get(16)),
                        new Sequential(
                                Gripper.runToPosition(Gripper.openPos),
                                new Wait(0.2),
                                Gripper.runToPosition(Gripper.closePos)
                        )
                ),

                Drive.push(intakePower, intakeDelay),
                Gripper.runToPosition(Gripper.closePos),

                // plus 4 outtake
                new Parallel(
                        Drive.followPath(Paths.fiveSpecs.get(17)),
                        Slides.runToPosition(Slides.highPos)
                ),
                new Parallel(
                        Drive.push(outtakePower, outtakeDelay),
                        Slides.runToPosition(Slides.scoreHighPos)

                ),
                Gripper.runToPosition(Gripper.openPos),

                // park
                new Parallel(
                        Drive.followPath(Paths.fiveSpecs.get(18)),
                        new Sequential(
                                Gripper.runToPosition(Gripper.openPos),
                                new Wait(0.2),
                                Slides.runToPosition(Slides.wall)
                        )
                )
        ).schedule();
    }
}
