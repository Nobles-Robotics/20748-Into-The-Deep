package org.firstinspires.ftc.teamcode.teleop.bot;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.CRServo;
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

public class Arm implements Subsystem {
    public static final Arm INSTANCE = new Arm();

    private Arm() { }
    private static CRServo servoIntake;
    private static ServoEx servoWrist;

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

    //Declares default command + Initializes the Subsystem
    @Override
    public void postUserInitHook(@NonNull Wrapper opMode) {
        servoIntake = new CRServo(opMode.getOpMode().hardwareMap, "servoIntake");
        servoWrist = new SimpleServo(opMode.getOpMode().hardwareMap, "servoWrist", 0, 300);
        servoIntake.setInverted(true);
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {}

    @Override
    public void postUserStopHook(@NonNull Wrapper opMode) {}

    @Override
    public void cleanup(@NonNull Wrapper opMode) {}

    @NonNull
    public static Lambda runIntake() {
        return new Lambda("simple")
                .addRequirements(INSTANCE)
                .setInit(() -> servoIntake.set(1))
                .setEnd(interrupted -> {
                    if (!interrupted) servoIntake.set(0.0);
                });
    }
    @NonNull
    public static Lambda releaseIntake() {
        return new Lambda("simple")
                .addRequirements(INSTANCE)
                .setInit(() -> servoIntake.set(-1))
                .setEnd(interrupted -> {
                    if (!interrupted) servoIntake.set(0.0);
                });
    }

    @NonNull
    public static Lambda runServoWrist() {
        return new Lambda("simple")
                .addRequirements(INSTANCE)
                .setInit(() -> servoWrist.turnToAngle(300))
                .setEnd(interrupted -> {
                    if (!interrupted) servoWrist.turnToAngle(0);
                });
    }
}
