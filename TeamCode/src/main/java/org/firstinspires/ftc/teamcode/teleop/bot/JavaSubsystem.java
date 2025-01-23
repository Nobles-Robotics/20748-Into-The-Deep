/*
THIS FILE AIMS AS A BASIS FOR ALL SUBSYSTEMS
 */
/*
package org.firstinspires.ftc.teamcode.teleop.bot;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.hardware.motors.MotorEx;

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

    private final SubsystemObjectCell<MotorEx> motor = subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(MotorEx.class, ""));
    public static MotorEx getMotor() {
        return INSTANCE.motor.get();
    }

    //Declares default command + Initializes the Subsystem
    @Override
    public void postUserInitHook(@NonNull Wrapper opMode) {
        setDefaultCommand(simpleCommand());
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {}

    @Override
    public void postUserStopHook(@NonNull Wrapper opMode) {}

    @Override
    public void cleanup(@NonNull Wrapper opMode) {}

    @NonNull
    public static Lambda simpleCommand() {
        return new Lambda("simple")
                .addRequirements(INSTANCE)
                .setInit(() -> getMotor().set(0.4))
                .setEnd(interrupted -> {
                    if (!interrupted) getMotor().set(0.0);
                });
    }
    @NonNull
    public static StatefulLambda<RefCell<Double>> statefulCommand() {
        return new StatefulLambda<>("stateful", new RefCell<>(0.0))
                .addRequirements(INSTANCE)
                .setInit((state) -> getMotor().set(0.4 + state.get()))
                .setEnd((interrupted, state) -> {
                    if (!interrupted) getMotor().set(0);
                    state.accept(state.get() + 0.1);
                });
    }
}
*/

