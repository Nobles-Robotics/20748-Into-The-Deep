package org.firstinspires.ftc.teamcode.teleop.bot;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.lang.annotation.*;

@Config
public class Intake implements Subsystem {
    public static final Intake INSTANCE = new Intake();
    public static double dropPos = 0.00;
    public static double raisePos = 0.5;
    public static double storePos = 1.0;
    public static Servo wrist;
    public static CRServo spinner;

    public static ColorSensor colorSensor;

    public static Telemetry telemetry;
    public static boolean raised;
    public static boolean stored;
    public static String color;
    private Intake() {}

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

        //wrist = hMap.get(Servo.class, "dropdownL");
        //spinner = hMap.get(CRServo.class, "spintake");
        colorSensor = hMap.get(ColorSensor.class, "color");
        telemetry = opMode.getOpMode().telemetry;

    }

    @Override
    public void postUserStartHook(@NonNull Wrapper opMode) {
        if (colorSensor.red() > 2000 && colorSensor.green() > 2000) {
            telemetry.addLine("Yellow");
        } else if (colorSensor.red() > 1500 && colorSensor.green() < 1150) {
            telemetry.addLine("Red");
        } else if (colorSensor.red() < 900 && colorSensor.green() > 1000) {
            telemetry.addLine("Blue");
        }
    }



    private static void setPos(double pos) {
        wrist.setPosition(pos);
        if (Math.abs(pos - dropPos) < 0.1) {
            raised = false;
        } else if (Math.abs(pos - raisePos) < 0.1){
            raised = true;
        }
    }

    private static void spin(double power){
        spinner.setPower(power);
    }

    private static void stop(){
        spinner.setPower(0);
    }

    public static Lambda spintake(double power){
        return new Lambda("spintake")
                .addRequirements(spinner)
                .setExecute(() -> spin(power))
                .setFinish(() -> true);
    }

    public static Lambda raise() {
        return new Lambda("raise-intake")
                .setInit(() -> {raised = true; stored = false;})
                .setExecute(() -> {wrist.setPosition(raisePos);})
                .setFinish(() -> !raised);
    }
    public static Lambda drop() {
        return new Lambda("drop-intake")
                .setInit(() -> {raised = false; stored = false;})
                .setExecute(() -> {wrist.setPosition(dropPos);})
                .setFinish(() -> raised);
    }
    public static Lambda store() {
        return new Lambda("store-intake")
                .setInit(() -> {stored = true;})
                .setExecute(() -> {wrist.setPosition(storePos);})
                .setFinish(() -> !stored);
    }

    public static Lambda raiseIntake() {
        return new Lambda("raise-intake")
                .setInit(Intake::raise)
                .setFinish(() -> true);
    }
    public static Lambda dropIntake() {
        return new Lambda("drop-intake")
                .setInit(Intake::drop)
                .setFinish(() -> true);
    }
    public static Lambda storeIntake() {
        return new Lambda("store-intake")
                .setInit(Intake::store)
                .setFinish(() -> true);

    }

    public static Lambda setIntake(double pos) {
        return new  Lambda("set-intake")
                .setInit(() -> Intake.setPos(pos));
    }
}
