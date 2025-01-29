package org.firstinspires.ftc.teamcode.teleop.bot;

import com.pedropathing.pathgen.PathBuilder;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;

import java.util.Map;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.commands.groups.Parallel;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.IfElse;
import dev.frozenmilk.mercurial.commands.util.StateMachine;
import dev.frozenmilk.mercurial.commands.util.Wait;
import org.firstinspires.ftc.teamcode.util.States.StatePositions;

public class Bot {

    public static volatile State currentState = State.IDLE;

    public enum State {
        IDLE,
        INTAKE_SPEC,
        OUTTAKE_SUBMERSIBLE,
        OUTTAKE_SUBMERSIBLE_SCORE,
        OUTTAKE_SPEC,
        UNJAM
    }

    public static StateMachine<State> stateMachine;

    public static OpModeMeta.Flavor flavor;

    static PathBuilder plusThreeBlueSpec = new PathBuilder();

    public static void init() {

        StatePositions init = new StatePositions(
                Outtake.armHomePos,
                Outtake.pivotHomePos,
                true,
                OuttakeSlides.minPos
        );
        StatePositions idle = new StatePositions(
                Outtake.armHomePos,
                Outtake.pivotHomePos,
                true,
                OuttakeSlides.minPos
        );
        StatePositions intake = new StatePositions(
                Outtake.armSpecPos,
                Outtake.pivotSpecPos,
                true,
                OuttakeSlides.minPos
        );
        StatePositions outtakeSubmersible = new StatePositions(
                Outtake.armSubmersiblePos,
                Outtake.pivotSubmersiblePos,
                false,
                OuttakeSlides.submersiblePos
        );
        StatePositions outtakeSubmersibleScore = new StatePositions(
                Outtake.armSubmersiblePos,
                Outtake.pivotSubmersiblePos,
                false,
                OuttakeSlides.scoreSubmersiblePos
        );

        Map<State, StatePositions> states = Map.of(
                State.IDLE, idle,
                State.INTAKE_SPEC, intake,
                State.OUTTAKE_SUBMERSIBLE, outtakeSubmersible,
                State.OUTTAKE_SUBMERSIBLE_SCORE, outtakeSubmersibleScore,
        );

        flavor = FeatureRegistrar.getActiveOpModeWrapper().getOpModeType();

        stateMachine = new StateMachine<>(State.IDLE)
                .withState(State.HOME, (state, name) -> Lambda.from(
                        new Sequential(
//                                new IfElse(
//                                        () -> (Intake.raised && (
//                                                OuttakeSlides.controller.getSetPoint() == OuttakeSlides.submersiblePos ||
//                                                OuttakeSlides.controller.getSetPoint() == OuttakeSlides.scoreSubmersiblePos)),
//                                        Intake.dropIntake().then(new Wait(0.4)),
//                                        new Wait(0)
//                                ),
                                new IfElse(
                                        () -> Intake.raised,
                                        Intake.setIntake(Intake.hoverPos),
                                        new Wait(0)
                                ),
                                Intake.setIntake(Intake.hoverPos),
                                new Wait(0.2),
                                Outtake.openClaw(),
                                Outtake.setArm(home.armPos),
                                Outtake.setPivot(home.pivotPos),
                                OuttakeSlides.runToPosition(OuttakeSlides.minPos)
                        )
                ))
                .withState(State.INTAKE_SPEC, (state, name) -> Lambda.from(
                        new Sequential(
                                Outtake.openClaw(),
                                Outtake.setArm(intake.armPos),
                                Outtake.setPivot(intake.pivotPos),
                                new Wait(0.5),
                                OuttakeSlides.runToPosition(OuttakeSlides.minPos)

                        )
                ))
                .withState(State.OUTTAKE_SUBMERSIBLE, (state, name) -> Lambda.from(
                        new Sequential(
                                new IfElse(
                                        () -> Intake.raised,
                                        Intake.setIntake(Intake.hoverPos),
                                        new Wait(0)
                                ),
                                new Wait(0.2),
                                Outtake.closeClaw(),
                                OuttakeSlides.runToPosition(outtakeSubmersible.slidePos),
                                Outtake.setPivot(outtakeSubmersible.pivotPos),
                                Outtake.setArm(outtakeSubmersible.armPos)
                        )
                ))
                .withState(State.OUTTAKE_SUBMERSIBLE_SCORE, (state, name) -> Lambda.from(
                        new Sequential(
                                OuttakeSlides.runToPosition(outtakeSubmersibleScore.slidePos)
                        )
                ))
                .withState(State.OUTTAKE_SPEC, (state, name) -> Lambda.from(
                        new Sequential(
                                Outtake.closeClaw(),
                                OuttakeSlides.runToPosition(OuttakeSlides.safePos),
                                Outtake.setArm(Outtake.armOuttakeSpec),
                                Outtake.setPivot(Outtake.pivotOuttakeSpec)
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
