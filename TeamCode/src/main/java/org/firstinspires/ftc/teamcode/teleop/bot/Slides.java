package org.firstinspires.ftc.teamcode.teleop.bot;
import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.arcrobotics.ftclib.util.Timing;
import com.qualcomm.robotcore.hardware.HardwareMap;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;
import org.firstinspires.ftc.teamcode.util.MotionProfiler;

public class Slides implements Subsystem {
    public static final Slides INSTANCE = new Slides();

    private Slides() { }
    private MotorEx slideEL, slideER, slideRL, slideRR;
    private static MotorGroup slideE;
    private MotorGroup slideR;
    private static PIDFController controller;
    private final double p = 0.01, i = 0, d = 0.001, f = 0;
    private double maxV = 10000, maxA = 5000;
    private static MotionProfiler profiler;
    private static int target;
    private static double startTime;
    private static double tolerance = 0.1;

    private static Timing.Timer Timer;


    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach {}

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
        slideE = new MotorGroup(slideEL, slideRL);
        slideR = new MotorGroup(slideER, slideRR);
        initializeSlides();
        controller = new PIDFController(p, i, d, f);
        controller.setTolerance(tolerance);
        profiler = new MotionProfiler(maxV, maxA);
        setDefaultCommand(update());
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {
        update();
    }

    @Override
    public void postUserStopHook(@NonNull Wrapper opMode) {}
    @Override
    public void cleanup(@NonNull Wrapper opMode) {}

    public void initializeSlides() {
        slideE.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        slideR.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        slideE.setInverted(false);
        slideR.setInverted(false);
        slideE.setRunMode(Motor.RunMode.RawPower);
        slideR.setRunMode(Motor.RunMode.RawPower);
    }

    @NonNull
    public static Lambda update() {
        return new Lambda("update")
                .addRequirements(INSTANCE)
                .setExecute(Slides::updatePIDF)
                .setFinish(() -> false);
    }

    @NonNull
    public static Lambda runTo(int to){
        return new Lambda("runTo")
                .setExecute(() -> setTarget(to))
                .setFinish(Slides::atTarget);
    }

    private static void updatePIDF() {
        double power = 0;
        double currentTime = Timer.elapsedTime();

        //If we want to implement the ElevatorFeedForward in ftclib (https://docs.ftclib.org/ftclib/features/controllers#elevatorfeedforward)
        double desiredPos = profiler.pos(currentTime);
        double desiredVelo = profiler.velocity(currentTime);
        double desiredAccel = profiler.acceleration(currentTime);

        if (profiler.isOver()){
            if (profiler.isDone()){
                INSTANCE.resetProfiler();
            } else {
                controller.setSetPoint(getTarget());
                power = controller.calculate(slideE.getCurrentPosition());
            }
        } else {
            controller.setSetPoint(profiler.pos(currentTime));
            power = controller.calculate(slideE.getCurrentPosition());
        }
        slideE.set(power);
    }

    public static boolean atTarget() {
        return (slideE.getCurrentPosition() >= (getTarget() - tolerance) || slideE.getCurrentPosition() <= (getTarget() + tolerance));
    }
    public void resetProfiler() {
        profiler = new MotionProfiler(maxV, maxA);
    }

    public static void setTarget(int runTo) {
        target = runTo;
        INSTANCE.resetProfiler();
        profiler.initialize(slideE.getCurrentPosition(), target);
        Timer = new Timing.Timer(profiler.getEntiredT());
    }

    public static int getTarget() {
        return target;
    }
    public static void resetEncoder(){
        slideE.resetEncoder();
    }
}
