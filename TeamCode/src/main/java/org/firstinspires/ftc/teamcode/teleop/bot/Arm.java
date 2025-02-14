package org.firstinspires.ftc.teamcode.teleop.bot;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.util.Names;

import java.lang.annotation.*;

@Config
public class Arm implements Subsystem {
    public static final Arm INSTANCE = new Arm();

    public static DcMotorEx arm;
    public static TouchSensor touch;
    public static Telemetry telemetry;

    public static double armHomePos = 0;
    public static double armExtendPos = 1000;
    public static boolean enablePID = true;
    public static double tolerance = 10;

    public static PIDFController controller = new PIDFController(0.00014, 0.0000, 0.0000, 0.0000);

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach {
    }

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotation<>(Attach.class));

    @NonNull
    @Override
    public Dependency<?> getDependency() {
        return dependency;
    }

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) {
        this.dependency = dependency;
    }

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        HardwareMap hMap = opMode.getOpMode().hardwareMap;
        telemetry = opMode.getOpMode().telemetry;

        arm = hMap.get(DcMotorEx.class, Names.arm);
        touch = hMap.get(TouchSensor.class, Names.touch);

        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        controller.setTolerance(tolerance);

    }

    @Override
    public void postUserInitHook(@NonNull Wrapper opMode) {
        runPID();
    }
    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {
    }

    public static void reset() {
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        controller.reset();
        controller.setSetPoint(0);
    }

    public static Lambda setPower(double pow) {
        return new Lambda("power-intake")
                .setInit(() -> arm.setPower(pow))
                .setFinish(() -> true)
                .setEnd((interrupted) -> arm.setPower(0));
    }

    public static Lambda setPowerPersistent(double pow) {
        return new Lambda("power-intake")
                .setInit(() -> arm.setPower(pow))
                .setFinish(() -> true);
    }

    public static Lambda home() {
        return new Lambda("home-intake")
                .setInit(() -> {
                    enablePID = false;
                    arm.setPower(-1);
                })
                .setFinish(() -> touch.isPressed())
                .setEnd((interrupted) -> {
                    reset();
                    enablePID = true;
                });
    }

    public static Lambda extend() {
        return new Lambda("extend-intake")
                .setInit(() -> {
                    arm.setPower(1);
                    enablePID = false;
                })
                .setFinish(() -> arm.getCurrentPosition() > armExtendPos || arm.getCurrent(CurrentUnit.MILLIAMPS) > 1500)
                .setEnd((interrupted) -> {
                    controller.setSetPoint(arm.getCurrentPosition());
                    enablePID = true;
                });
    }

    public static Lambda setPowerManual(double pow){
        return new Lambda("set-power-up")
                .setInit(() -> {
                    enablePID = false;
                    arm.setPower(pow);
                })
                .setFinish(() -> true)
                .setEnd((interrupted) -> {
                    arm.setPower(pow);
                    controller.setSetPoint(arm.getCurrentPosition());
                    enablePID = true;
                });
    }

    public static Lambda runPID() {
        return new Lambda("outtake-pid")
                .setExecute(() -> {
                    if (enablePID) {
                        double power = controller.calculate(arm.getCurrentPosition());
                        setPowerPersistent(power);
                    }
                })
                .setFinish(() -> false);
    }

    public static Lambda runToPosition(double pos){
        return new Lambda("set-target-pos")
                .setInterruptible(true)
                .setInit(() -> {
                    controller.setSetPoint(pos);
                    enablePID = true;
                })
                .setFinish(() -> controller.atSetPoint());
    }

    public static Lambda resetCommand(){
        return new Lambda("reset-slides")
                .setInit(Slides::reset)
                .setFinish(() -> true);
    }

    public static void logTele(Bot.Logging level){
        switch (level) {
            case NORMAL:
                telemetry.addLine("Arm: pid? "+ enablePID + " | SetPoint:" + controller.getSetPoint());
                telemetry.addData("Position: ", arm.getCurrentPosition());
                telemetry.addData("Touch Sensor", touch.isPressed());
                break;
            case DISABLED:
                break;
            case VERBOSE:
                telemetry.addLine("Arm: pid? " + enablePID);
                telemetry.addLine("Position: " + arm.getCurrentPosition() + " | Error: " + controller.getPositionError());
                telemetry.addData("Current Power", arm.getPower());
                telemetry.addLine("Current: " + arm.getCurrent(CurrentUnit.MILLIAMPS));
                telemetry.addData("Touch Sensor", touch.isPressed());
        }
    }
}
