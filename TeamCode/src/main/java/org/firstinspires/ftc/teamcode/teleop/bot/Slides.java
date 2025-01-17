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
import dev.frozenmilk.mercurial.commands.groups.CommandGroup;
import dev.frozenmilk.mercurial.commands.groups.Race;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.Wait;
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
    private static int tolerance = 500;

    private static boolean isClimb = false;
    private static boolean isManual = false;
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

    public static void pidfUpdate() {
        if (isManual){
            power = Mercurial.gamepad1().rightStickY().state();
            slideER.set(power);
            slideEL.set(power);
        } else {
            pidf.setSetPoint(liftTarget);
            power = pidf.calculate(liftTarget, getLiftPosition());
            if (!isClimb){
                slideER.set(power);
                slideEL.set(power);
            } else {
                slideER.set(1);
                slideEL.set(1);
            }
        }
    }

    public static void hold() {
        slideER.set(power);
        slideEL.set(power);
    }
    public static double getPower(){
        return power;
    }

    public static int getLiftPosition(){
        return Math.max(slideER.getCurrentPosition(), 0);
        //Or else it will blow right past 0 and go to negative infinity! Onwards!
    }

    public static boolean atTarget() { return (getLiftPosition() >= (getTarget() - tolerance) || getLiftPosition() <= (getTarget() + tolerance)); }

    @NonNull
    public static Lambda update() {
        return new Lambda("update the pid")
                .addRequirements(INSTANCE)
                .setExecute(Slides::pidfUpdate)
                .setFinish(() -> false);
    }

    @NonNull
    public static Lambda goTo(int to){
        return new Lambda("set pidf target")
                .setExecute(() -> setTarget(to))
                .setFinish(Slides::atTarget);
    }
    @NonNull
    public static Lambda resetEncoder(){
        return new Lambda("reset encoders")
                .setExecute(Slides::resetEncoders);
    }
    @NonNull
    public static Sequential climb(){
        return new Sequential(setClimb(true), new Race(new Wait(5.0), goTo(0)), setClimb(false));
    }

    @NonNull
    public static Lambda setClimb(boolean climb){
        return new Lambda("set climb variable")
                .setExecute(() -> isClimb = climb);
    }

    @NonNull
    public static Lambda setManual(boolean manual){
        return new Lambda("set climb variable")
                .setExecute(() -> isManual = manual);
    }




    public static void resetEncoders(){
        slideER.stopAndResetEncoder();
        slideEL.stopAndResetEncoder();
    }
}
