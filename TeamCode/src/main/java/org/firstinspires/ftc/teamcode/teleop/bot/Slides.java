package org.firstinspires.ftc.teamcode.teleop.bot;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.commands.groups.Race;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import dev.frozenmilk.mercurial.commands.util.Wait;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;

import java.lang.annotation.*;

@Config
public class Slides implements Subsystem {
    public static final Slides INSTANCE = new Slides();
    private static int liftTarget;
    private static double power;
    private static PIDFController pidf;
    private static double kP = .004, kI = 0, kD = 0, kF = 0;
    private static MotorEx slideER, slideEL, slideRR, slideRL;
    private static int tolerance = 500;

    private static double gamepadTollerance = 0.2;

    private static boolean isClimb = false;
    private static boolean isManual = false;

    double retractionPowerScale = (57.0 / 63.0) * (17.0 / 11.0);
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
        slideER = new MotorEx(hwmap, "vertSlideRightUp");
        slideEL = new MotorEx(hwmap, "vertSlideLeftUp");
        slideRR = new MotorEx(hwmap, "vertSlideRightDown");
        slideRL = new MotorEx(hwmap, "vertSlideLeftDown");
        slideER.setRunMode(Motor.RunMode.RawPower);
        slideEL.setRunMode(Motor.RunMode.RawPower);
        slideER.setInverted(false);
        slideEL.setInverted(false);
        slideEL.stopAndResetEncoder();
        setDefaultCommand(update());
        pidf = new PIDFController(kP, kI, kD, kF);
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {
    }


    public static void setTarget(int target){ liftTarget = target; }

    public static int getTarget(){ return liftTarget; }
    public static boolean getManual(){ return isManual; }

    public static void pidfUpdate() {
        if (isManual){
            power = parseGamepad();
            slideER.set(power);
            slideEL.set(power);
            setTarget(getLiftPosition());
        } else {
            pidf.setSetPoint(liftTarget);
            power = pidf.calculate(liftTarget, getLiftPosition());
            if (!isClimb){
                slideER.set(-power);
                slideEL.set(-power);
                //slideRR.set(power * retractionPowerScale);
                //slideRL.set(power * retractionPowerScale);
            } else {
                slideER.set(1);
                slideEL.set(1);
            }
        }
    }

    public static double parseGamepad(){
        double output = Mercurial.gamepad1().leftStickY().state();
        if ((output > 0.5 )){
            return 0.5;
        } else if (output < -0.5 ){
            return -0.5;
        }
        return 0;
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
        //Or else it will blow right past 0 and go to negative infinity! Downwards!
    }

    public static int getActualLiftPosition(){
        return slideER.getCurrentPosition();
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
        setTarget(getTarget());
    }
}
