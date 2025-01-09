package org.firstinspires.ftc.teamcode.teleop.teleop.bot;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.commands.stateful.StatefulLambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.mercurial.subsystems.SubsystemObjectCell;
import dev.frozenmilk.util.cell.RefCell;
import kotlin.annotation.MustBeDocumented;

public class JavaSubsystem implements Subsystem {
    public static final JavaSubsystem INSTANCE = new JavaSubsystem();

    private JavaSubsystem() { }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach{}
    private Dependency<?> dependency =
            Subsystem.DEFAULT_DEPENDENCY
                    .and(new SingleAnnotation<>(Attach.class));

    @NonNull
    @Override
    public Dependency<?> getDependency() {
        return dependency;
    }

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) {
        this.dependency = dependency;
    }

    private final SubsystemObjectCell<DcMotorEx> motor = subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(DcMotorEx.class, ""));
    public static DcMotorEx getMotor() {
        return INSTANCE.motor.get();
    }

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        // default command should be set up here, not in the constructor
        setDefaultCommand(simpleCommand());
    }
    // or here
    @Override
    public void postUserInitHook(@NonNull Wrapper opMode) {}

    // and you might put periodic code in these
    @Override
    public void preUserInitLoopHook(@NonNull Wrapper opMode) {}
    @Override
    public void preUserLoopHook(@NonNull Wrapper opMode) {}
    // or these
    @Override
    public void postUserInitLoopHook(@NonNull Wrapper opMode) {}
    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {}

    // and stopping code can go in here
    @Override
    public void preUserStopHook(@NonNull Wrapper opMode) {}
    // or here
    @Override
    public void postUserStopHook(@NonNull Wrapper opMode) {}

    // see the feature dev notes on when to use cleanup vs postStop
    @Override
    public void cleanup(@NonNull Wrapper opMode) {}

    // all depending on what you need!
    // remember, you only need to write implementations for the hooks you actually use
    // the rest don't need to be added to the class, nice and clean

    //
    // Commands
    //
    // commands are the same as older mercurial!
    // lambda commands are once again, powerful tools for developing simple units of operation
    @NonNull
    public static Lambda simpleCommand() {
        // we need to give commands names
        // names help to give helpful error messages when something goes wrong in your command
        // Mercurial will automatically rename your command to match the standard convention
        // learn more about names and error messages in the names and messages overview
        return new Lambda("simple")
                .addRequirements(INSTANCE)
                .setInit(() -> getMotor().setPower(0.4))
                .setEnd(interrupted -> {
                    if (!interrupted) getMotor().setPower(0.0);
                });
    }

    // lambda commands have a new powerful extension, designed to work well with the Cell patterns in Dairy Util
    // RefCell<Double> is an immutable reference with interior immutability
    // we could also use a LazyCell, or an OpModeLazyCell, or SubsystemObjectCell, depending on our needs
    // we need to manage the state ourselves, if we want to reset it at the start of each run of this command,
    // or if its persistent across runs, we are in control
    // note that, each copy of state is unique to each individual instance of this command
    // if we wanted shared state across all instances, we could capture state from this class instead
    // state can also be captured from the method itself, so StatefulLambdaCommand is not the only way to carry state
    @NonNull
    public static StatefulLambda<RefCell<Double>> statefulCommand() {
        // once again, we need to give it a name
        // learn more about names and error messages in the names and messages overview
        return new StatefulLambda<>("stateful", new RefCell<>(0.0))
                // note that stateful lambda commands have all the same methods that
                // the regular lambda command has
                // and variants that also take access to state where appropriate
                .addRequirements(INSTANCE)
                .setInit((state) -> getMotor().setPower(0.4 + state.get()))
                // every time this command ends, we increase the power next time we run it
                // this isn't a terribly practical example
                // but this is useful for PID controllers and similar, without
                // requiring the creation of a whole command class just to hold some state
                .setEnd((interrupted, state) -> {
                    if (!interrupted) getMotor().setPower(0);
                    state.accept(state.get() + 0.1);
                });
    }
}
