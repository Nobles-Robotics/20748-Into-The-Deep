package org.firstinspires.ftc.teamcode.teleop.bot;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;

@Config
public class Slides implements Subsystem {
    public static final Slides INSTANCE = new Slides();
    private static int liftTarget;
    private static double power;
    private static PIDFController pidf;
    private static double kP = .004, kI = 0, kD = 0, kF = 0;
    private static MotorEx slideER, slideEL, slideRR, slideRL;
    private static int tollerence = 500;
    private Slides() { }

    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE) @MustBeDocumented
    @Inherited
    public @interface Attach { }

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotation<>(Attach.class));

    @NonNull
    @Override
    public Dependency<?> getDependency() { return dependency; }

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) { this.dependency = dependency; }

    @Override
    public void postUserInitHook(@NonNull Wrapper opMode) {
        HardwareMap hwmap = opMode.getOpMode().hardwareMap;
        slideER = new MotorEx(hwmap, "motorSlideER");
        slideEL = new MotorEx(hwmap, "motorSlideEL");
        slideRR = new MotorEx(hwmap, "motorSlideRR");
        slideRL = new MotorEx(hwmap, "motorSlideRL");
        slideER.setRunMode(Motor.RunMode.RawPower);
        slideEL.setRunMode(Motor.RunMode.RawPower);
        slideER.setInverted(true);
        slideEL.setInverted(true);
        slideEL.stopAndResetEncoder();
        setDefaultCommand(update());
        pidf = new PIDFController(kP, kI, kD, kF);
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {
    }


    public static void setTarget(int target){ liftTarget = target; }

    public static int getTarget(){ return liftTarget; }

    public static void pidUpdate() {
        pidf.setSetPoint(liftTarget);
        power = pidf.calculate(liftTarget, getLiftPosition());
        slideER.set(power);
        slideEL.set(power);
    }

    public static void hold() {
        slideER.set(power);
        slideEL.set(power);
    }
    public static double getPower(){
        return power;
    }

    public static int getLiftPosition(){
        if (slideER.getCurrentPosition() < 0){
            return 0;
        } else {
            return slideER.getCurrentPosition();
        }
    }

    public static boolean atTarget() { return (getLiftPosition() >= (getTarget() - tollerence) || getLiftPosition() <= (getTarget() + tollerence)); }

    @NonNull
    public static Lambda update() {
        return new Lambda("update the pid")
                .addRequirements(INSTANCE)
                .setExecute(Slides::pidUpdate)
                .setFinish(() -> false);
    }

    @NonNull
    public static Lambda goTo(int to){
        return new Lambda("set pid target")
                .setExecute(() -> setTarget(to))
                .setFinish(Slides::atTarget);
    }
    @NonNull
    public static Lambda resetEncoder(){
        return new Lambda("set pid target")
                .setExecute(Slides::resetEncoders);
    }


    public static void resetEncoders(){
        slideER.stopAndResetEncoder();
        slideEL.stopAndResetEncoder();
    }
}
