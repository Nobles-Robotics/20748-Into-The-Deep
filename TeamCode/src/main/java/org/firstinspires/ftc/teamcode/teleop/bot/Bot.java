package org.firstinspires.ftc.teamcode.teleop.bot;

import com.pedropathing.pathgen.PathBuilder;
import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Race;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.StateMachine;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.firstinspires.ftc.teamcode.auto.Pathing.Paths;
import org.firstinspires.ftc.teamcode.util.States.StatePositions;

import java.util.Map;

public class Bot {

    public static volatile State currentState = State.HOME;

    public enum State {
        HOME,
        INTAKE_SPEC,
        INTAKE_SAM,
        OUTAKE_SAM,
        OUTTAKE_LOW,
        OUTTAKE_HIGH,
        SCORE_LOW,
        SCORE_HIGH,
    }

    public static StateMachine<State> stateMachine;

    public static OpModeMeta.Flavor flavor;

    static PathBuilder plusThreeBlueSpec = new PathBuilder();

    public static void init() {
        StatePositions init = new StatePositions(
                Arm.armHomePos,
                Slides.wall,
                Intake.raisePos,
                true,
                0
                );

        StatePositions home = new StatePositions(
                Arm.armHomePos,
                Slides.wall,
                Intake.storePos,
                false,
                0.1
        );

        StatePositions intakeSpec = new StatePositions(
                Arm.armHomePos,
                Slides.wall,
                Intake.storePos,
                true,
                0
        );

        StatePositions intakeSam = new StatePositions(
                Arm.armExtendPos,
                Slides.wall,
                Intake.dropPos,
                false,
                1
        );

        StatePositions outakeSam = new StatePositions(
                Arm.armExtendPos,
                Slides.wall,
                Intake.raisePos,
                false,
                -1
        );

        StatePositions outakeLow = new StatePositions(
                Arm.armHomePos,
                Slides.lowPos,
                Intake.storePos,
                false,
                0
        );

        StatePositions outakeHigh = new StatePositions(
                Arm.armHomePos,
                Slides.highPos,
                Intake.storePos,
                false,
                0
        );

        StatePositions scoreLow = new StatePositions(
                Arm.armHomePos,
                Slides.scoreLowPos,
                Intake.storePos,
                false,
                0
        );

        StatePositions scoreHigh = new StatePositions(
                Arm.armHomePos,
                Slides.scoreHighPos,
                Intake.storePos,
                false,
                0
        );

        Map<State, StatePositions> states = Map.of(
                State.HOME, home,
                State.INTAKE_SPEC, intakeSpec,
                State.INTAKE_SAM, intakeSam,
                State.OUTAKE_SAM, outakeSam,
                State.OUTTAKE_LOW, outakeLow,
                State.OUTTAKE_HIGH, outakeHigh,
                State.SCORE_LOW, scoreLow,
                State.SCORE_HIGH, scoreHigh
        );

        flavor = FeatureRegistrar.getActiveOpModeWrapper().getOpModeType();

        stateMachine = new StateMachine<>(State.HOME)
                .withState(State.HOME, (state, name) -> Lambda.from(
                        new Sequential(
                                new Parallel(
                                        Arm.home(),
                                        Slides.runToPosition(home.slidePos),
                                        Intake.storeIntake()
                                ),
                                Gripper.open()
                        )
                ))
                .withState(State.INTAKE_SPEC, (state, name) -> Lambda.from(
                        new Sequential(
                                new Parallel(
                                        Arm.home(),
                                        Slides.runToPosition(intakeSpec.slidePos),
                                        Intake.storeIntake()
                                ),
                                Gripper.close()
                                //Closing Gripper should happen after everything is all set
                        )
                ))
                .withState(State.INTAKE_SAM, (state, name) -> Lambda.from(
                        new Sequential(
                            new Parallel(
                                    Arm.extend(),
                                    Intake.dropIntake()
                            ),
                            Intake.spintake(intakeSam.intakeSpeed)
                        )
                ))
                .withState(State.OUTAKE_SAM, (state, name) -> Lambda.from(
                        new Sequential(
                                new Parallel(
                                        Arm.extend(),
                                        Intake.raiseIntake()
                                ),
                                Intake.spintake(outakeSam.intakeSpeed)
                        )
                ))
                .withState(State.OUTTAKE_HIGH, (state, name) -> Lambda.from(
                        new Sequential(
                                Arm.home(),
                                Intake.storeIntake(),
                            new Race(
                                    Slides.runToPosition(outakeHigh.slidePos),
                                    Gripper.close()
                            )
                        )
                ))
                .withState(State.OUTTAKE_LOW, (state, name) -> Lambda.from(
                        new Sequential(
                                Arm.home(),
                                Intake.storeIntake(),
                                new Race(
                                        Slides.runToPosition(outakeLow.slidePos),
                                        Gripper.close()
                                )
                        )
                ))
                .withState(State.SCORE_HIGH, (state, name) -> Lambda.from(
                        new Sequential(
                                Arm.home(),
                                Intake.storeIntake(),
                                new Race(
                                        Slides.runToPosition(scoreHigh.slidePos),
                                        Gripper.close()
                                ),
                                Gripper.open()
                        )
                ))
                .withState(State.SCORE_LOW, (state, name) -> Lambda.from(
                        new Sequential(
                                Arm.home(),
                                Intake.storeIntake(),
                                new Race(
                                        Slides.runToPosition(scoreLow.slidePos),
                                        Gripper.close()
                                ),
                                Gripper.open()
                        )
                ));


        Paths.init();
    }

    public static Lambda setState(State state) {
        return new Lambda("set-state")
                .setInit(() -> {
                    stateMachine.schedule(state);
                })
                .setFinish(() -> true);
    }
}
