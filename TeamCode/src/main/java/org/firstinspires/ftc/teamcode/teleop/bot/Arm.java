package org.firstinspires.ftc.teamcode.teleop.bot;

import androidx.annotation.NonNull;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.CRServo;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;

import java.lang.annotation.*;

public class Arm implements Subsystem {
    public static final Arm INSTANCE = new Arm();

    private Arm() { }
    private static CRServo servoIntake, servoSlideE, servoSlideR;
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
        servoIntake = new CRServo(opMode.getOpMode().hardwareMap, "servo5");
        servoSlideE = new CRServo(opMode.getOpMode().hardwareMap, "servoExp5");
        servoSlideR = new CRServo(opMode.getOpMode().hardwareMap, "servoExp3");
        servoWrist = new SimpleServo(opMode.getOpMode().hardwareMap, "servo3", 0, 300);
        servoIntake.setInverted(true);
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {
        runSlides();
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
                .setInit(() -> servoWrist.turnToAngle(258))
                .setEnd(interrupted -> {
                    if (!interrupted) servoWrist.turnToAngle(61);
                });
    }

    @NonNull
    public static Lambda runSlides() {
        return new Lambda("simple")
                .addRequirements(INSTANCE)
                .setInit(() -> {
                    double power = parseGamepad();
                    if (power > 0){
                        servoSlideE.set(power);
                        servoSlideR.set(power * 1.2);
                    } else {
                        servoSlideR.set(power);
                        servoSlideE.set(power * 0.8);
                    }
                })
                .setEnd(interrupted -> {
                    if (!interrupted){
                        servoSlideE.set(0);
                        servoSlideR.set(0);
                    }
                });

        /*
                if (Math.abs(power) < 0.01) {
            extender.set(0);
            retracter.set(0);
        } else if (power > 0) {
            extender.set(power);
            retracter.set(power * CombinedActionTeleOpMode.horizontalExtender_extendingPowerFactor);
        } else {
            retracter.set(power);
            extender.set(power * CombinedActionTeleOpMode.horizontalExtender_retractingPowerFactor);
        }
    }
        */

    }

    public static double getWrist(){
        return servoWrist.getAngle();
    }

    public static double parseGamepad(){
        double outputR = Mercurial.gamepad1().rightTrigger().state();
        double outputL = Mercurial.gamepad1().leftTrigger().state();
        if (outputR > 0.1){
            if (outputR > 0.85){
                return 0.6;
            } else if (outputR > 0.75){
                return 0.4;
            } else if (outputR > 0.5){
                return 0.3;
            } else if (outputR > 0.25){
                return 0.2;
            }
            return 0.1;
        } else if (outputL > 0.1){
            if (outputL > 0.85){
                return -0.6;
            } else if (outputL > 0.75){
                return -0.4;
            } else if (outputL > 0.5){
                return -0.3;
            } else if (outputL > 0.25){
                return -0.2;
            }
            return -0.1;
        }
        return 0;
    }
}
