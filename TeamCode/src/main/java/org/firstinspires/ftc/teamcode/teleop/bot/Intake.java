package org.firstinspires.ftc.teamcode.teleop.bot;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.Names;

import java.lang.annotation.*;

@Config
public class Intake implements Subsystem {
    public static final Intake INSTANCE = new Intake();
    public static double dropPos = 0;
    public static double raisePos = 355;
    public static double storePos = 0;
    public static SimpleServo wrist;
    public static Servo wrist2;
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

        wrist = new SimpleServo(opMode.getOpMode().hardwareMap, Names.wrist, 0, 355);
        wrist2 = hMap.get(Servo.class, Names.wrist);

        spinner = hMap.get(CRServo.class, Names.spinTake);
        colorSensor = hMap.get(ColorSensor.class, Names.color);
        telemetry = opMode.getOpMode().telemetry;

    }

    @Override
    public void postUserStartHook(@NonNull Wrapper opMode) {
    }


    public static double getPositionWrist(){
        return wrist.getAngle();
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
                .setInit(() -> {raised = true; stored = false; wrist2.getController().pwmEnable();})
                .setExecute(() -> wrist.turnToAngle(raisePos))
                .setFinish(() -> !raised || stored);
    }

    public static Lambda drop() {
        return new Lambda("drop-intake")
                .setInit(() -> {raised = false; stored = false; wrist2.getController().pwmEnable();})
                .setExecute(() -> wrist.turnToAngle(dropPos))
                .setFinish(() -> raised || stored);
    }

    public static Lambda store() {
        return new Lambda("store-intake")
                .setInit(() -> {
                    stored = true;
                    wrist.turnToAngle(raisePos);
                })
                .setFinish(() -> true)
                .setEnd((interrupted) -> {wrist2.getController().pwmDisable();});
    }

    public static Lambda runManual(double angle){
        return new Lambda("set-power-up")
                .setInit(() -> wrist.rotateByAngle(angle))
                .setFinish(() -> true);
    }

    public static Lambda raiseCommand() {
        return new Lambda("raise-command").setInit(Intake::raise).setFinish(() -> true);
    }
    public static Lambda dropCommand() {
        return new Lambda("raise-command").setInit(Intake::drop).setFinish(() -> true);
    }
    public static Lambda storeCommand() {
        return new Lambda("raise-command").setInit(Intake::store).setFinish(() -> true);
    }
    public static void logTele(){
        telemetry.addLine("Current Wrist Location" + getPositionWrist());
        telemetry.addLine("SpinTake Power" + spinner.getPower());
        telemetry.addLine("Raised: " + raised);
        telemetry.addLine("Stored: " + stored);
        telemetry.addData("red", colorSensor.red());
        telemetry.addData("blue", colorSensor.blue());
        telemetry.addData("green", colorSensor.green());
        double red = colorSensor.red();
        double green = colorSensor.green();
        double blue = colorSensor.blue();
        if (red>green*1.3 && red>blue*1.3){
            telemetry.addLine("Color: Red");
        } else if (blue>green*1.3 && blue>red*1.3){
            telemetry.addLine("Color: Blue");
        } else {
            telemetry.addLine("Nothing (or yellow :sob:)");
        }
    }
}
