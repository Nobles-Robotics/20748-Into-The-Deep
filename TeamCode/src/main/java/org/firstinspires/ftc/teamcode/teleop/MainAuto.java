package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import org.firstinspires.ftc.teamcode.teleop.bot.*;
import org.firstinspires.ftc.teamcode.util.BulkReads;
import org.firstinspires.ftc.teamcode.util.Pathing.Paths;

@Mercurial.Attach
@Drive.Attach
@Slides.Attach
@BulkReads.Attach
@Gripper.Attach
@Arm.Attach
@Autonomous(name = "MainAuto")
public class MainAuto extends OpMode {

    public static long intakeDelay = 250;
    public static double intakePower = -0.5;
    public static long outtakeDelay = 500;
    public static double outtakePower = 1;

    @Override
    public void init() {
        Bot.init();

        Bot.stateMachine.setState(Bot.State.INTAKE_SPEC);
    }

    @Override
    public void loop() {
    }

    @Override
    public void start() {
        new Sequential(
                // Preload
                new Parallel(
                        Bot.setState(Bot.State.OUTTAKE_HIGH),
                        Drive.followPath(Paths.plusFourSpec.get(0))
                ),
                new Parallel(
                        Drive.push(outtakePower, outtakeDelay),
                        Bot.setState(Bot.State.SCORE_HIGH)
                ),
                Gripper.open(),

                // Pushing samples
                new Parallel(
                        Drive.followPath(Paths.plusFourSpec.get(1)),
                        Bot.setState(Bot.State.INTAKE_SPEC)
                ),
                Drive.followPath(Paths.plusFourSpec.get(2)),
                Drive.followPath(Paths.plusFourSpec.get(3)),
                Drive.followPath(Paths.plusFourSpec.get(4)),
                Drive.followPath(Paths.plusFourSpec.get(5)),
                Drive.followPath(Paths.plusFourSpec.get(6)),
                Drive.followPath(Paths.plusFourSpec.get(7)),

                // plus 1 intake
                Drive.push(intakePower, intakeDelay),
                Gripper.close(),

                // plus 1 outtake
                new Parallel(
                        Bot.setState(Bot.State.OUTTAKE_HIGH),
                        Drive.followPath(Paths.plusFourSpec.get(8))
                ),
                new Parallel(
                        Drive.push(outtakePower, outtakeDelay),
                        Bot.setState(Bot.State.SCORE_HIGH)
                ),
                Drive.followPath(Paths.plusFourSpec.get(9)),
                Gripper.open(),

                // plus 2 intake
                new Parallel(
                        Bot.setState(Bot.State.INTAKE_SPEC),
                        Drive.followPath(Paths.plusFourSpec.get(10))
                ),
                Drive.push(intakePower, intakeDelay),
                Gripper.close(),

                // plus 2 outtake
                new Parallel(
                        Bot.setState(Bot.State.OUTTAKE_HIGH),
                        Drive.followPath(Paths.plusFourSpec.get(11))
                ),
                new Parallel(
                        Drive.push(outtakePower, outtakeDelay),
                        Bot.setState(Bot.State.SCORE_HIGH)
                ),
                Drive.followPath(Paths.plusFourSpec.get(12)),
                Gripper.open(),

                // plus 3 intake
                new Parallel(
                        Bot.setState(Bot.State.INTAKE_SPEC),
                        Drive.followPath(Paths.plusFourSpec.get(13))
                ),
                Drive.push(intakePower, intakeDelay),
                Gripper.close(),

                // plus 3 outtake
                new Parallel(
                        Bot.setState(Bot.State.OUTTAKE_HIGH),
                        Drive.followPath(Paths.plusFourSpec.get(14))
                ),
                new Parallel(
                        Drive.push(outtakePower, outtakeDelay),
                        Bot.setState(Bot.State.SCORE_HIGH)
                ),
                Drive.followPath(Paths.plusFourSpec.get(15)),
                Gripper.open(),

                // plus 4 intake
                new Parallel(
                        Bot.setState(Bot.State.INTAKE_SPEC),
                        Drive.followPath(Paths.plusFourSpec.get(16))
                ),

                Drive.push(intakePower, intakeDelay),
                Gripper.close(),

                // plus 4 outtake
                new Parallel(
                        Bot.setState(Bot.State.OUTTAKE_HIGH),
                        Drive.followPath(Paths.plusFourSpec.get(17))
                ),
                new Parallel(
                        Drive.push(outtakePower, outtakeDelay),
                        Bot.setState(Bot.State.SCORE_HIGH)
                ),
                Gripper.open(),

                // park
                new Parallel(
                        Bot.setState(Bot.State.INTAKE_SPEC),
                        Drive.followPath(Paths.plusFourSpec.get(18))
                )
        ).schedule();
    }
}
