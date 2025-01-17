package org.firstinspires.ftc.teamcode.teleop.bot;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.CRServo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;

public class Arm implements Subsystem {
    public static final Arm INSTANCE = new Arm();

    private Arm() { }
    private static CRServo servoIntake, servoSlideL, servoSlideR;
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
        servoSlideL = new CRServo(opMode.getOpMode().hardwareMap, "servoSlideL");
        servoSlideR = new CRServo(opMode.getOpMode().hardwareMap, "servoSlideR");
        servoWrist = new SimpleServo(opMode.getOpMode().hardwareMap, "servoWrist", 0, 300);
        servoIntake.setInverted(true);
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {
        runServoWrist();
    }

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
                .setInit(() -> servoWrist.rotateByAngle(Mercurial.gamepad1().rightStickY().state()))
                .setEnd(interrupted -> {
                    //if (!interrupted) servoWrist.turnToAngle(0);
                });
    }

    @NonNull
    public static Lambda runSlides() {
        return new Lambda("simple")
                .addRequirements(INSTANCE)
                .setInit(() -> {
                    servoSlideL.set(1);
                    servoSlideR.set(1);
                })
                .setEnd(interrupted -> {
                    if (!interrupted){
                        servoSlideL.set(0);
                        servoSlideR.set(0);
                    };
                });
    }

    public static double getWrist(){
        return servoWrist.getAngle();
    }
}
