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
    private double maxV = 1000, maxA = 500;
    private static MotionProfiler profiler;
    private static int target;
    private static double startTime;
    private static double tolerance = 0.1;


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
        controller.setTolerance(0.1);
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
                .setExecute(Slides::updatePIDF);
    }

    @NonNull
    public static Lambda goTo(int to){
        return new Lambda("set pid target")
                .setExecute(() -> setTarget(to))
                .setFinish(Slides::atTarget);
    }

    public void resetProfiler(double currentPos) {
        profiler.initialize(currentPos, target);
        startTime = System.currentTimeMillis() / 1000.0;
    }

    public static void setTarget(int runTo) {
        target = runTo;
        INSTANCE.resetProfiler(slideE.getCurrentPosition());
    }

    public static int getTarget() {
        return target;
    }

    private static void updatePIDF() {
        double currentTime = (System.currentTimeMillis() / 1000.0) - startTime;
        double desiredPos = profiler.pos(currentTime);
        double currentPos = slideE.getCurrentPosition();

        controller.setSetPoint(desiredPos);
        double power = controller.calculate(currentPos);

        slideE.set(power);
    }

    public static boolean atTarget() {
        return (slideE.getCurrentPosition() >= (getTarget() - tolerance) || slideE.getCurrentPosition() <= (getTarget() + tolerance));
    }
}
