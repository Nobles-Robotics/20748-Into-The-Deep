package org.firstinspires.ftc.teamcode.teleop.bot;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.RevColorSensor;

import java.lang.annotation.*;

@Config
public class ColorSensor implements Subsystem {
    public static final ColorSensor INSTANCE = new ColorSensor();


    public static Telemetry telemetry;


    public static RevColorSensor colorSensor;

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach {
    }

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotation<>(org.firstinspires.ftc.teamcode.teleop.bot.Arm.Attach.class));

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
        colorSensor = new RevColorSensor(hMap, "sensor");
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {
        telemetry.addData("isBlue?", colorSensor.isBlue());
        telemetry.addData("isYellow?", colorSensor.isYellow());
        telemetry.addData("isRed?", colorSensor.isRed());
        telemetry.addLine("Color Sensor?");
        telemetry.update();

    }
}

