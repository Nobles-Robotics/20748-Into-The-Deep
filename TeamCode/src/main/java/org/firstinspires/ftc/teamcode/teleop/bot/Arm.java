package org.firstinspires.ftc.teamcode.teleop.bot;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;

@Config
public class Arm implements Subsystem {
    public static final Arm INSTANCE = new Arm();

    public static DcMotorEx extendo;
    public static TouchSensor touch;
    public static Telemetry telemetry;

    public static double constantPower = 0.1;
    public static double armHomePos = 0;
    public static double armExtendPos = 1000;
    public static boolean enablePID = true;

    public static PIDFController controller = new PIDFController(0.00014, 0.0000, 0.0000, 0.0000);

    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE) @MustBeDocumented
    @Inherited
    public @interface Attach { }

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

        extendo = hMap.get(DcMotorEx.class, "extendo");
        touch = hMap.get(TouchSensor.class, "touchSlide");

        reset();
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {}

    public static void reset() {
        extendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extendo.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public static Lambda setPower(double pow) {
        return new Lambda("power-intake")
                .setInit(() -> {
                    extendo.setPower(pow);
                })
                .setFinish(() -> true);
    }

    public static Lambda home() {
        return new Lambda("home-intake")
                .setInit(() -> extendo.setPower(-1))
                .setFinish(() -> touch.isPressed())
                .setEnd((interrupted) -> extendo.setPower(-constantPower));
    }

    public static Lambda runToPosition(double pos){
        return new Lambda("set-target-pos")
                .setInterruptible(true)
                .setInit(() -> {
                    controller.setSetPoint(pos);
                })
                .setFinish(() -> controller.atSetPoint());
    }
}
