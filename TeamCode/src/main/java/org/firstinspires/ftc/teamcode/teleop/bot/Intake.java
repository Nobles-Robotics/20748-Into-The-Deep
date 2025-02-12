package org.firstinspires.ftc.teamcode.teleop.bot;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
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
    public static double raisePos = 100;
    public static double storePos = 200;
    public static SimpleServo wrist;
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

        wrist = new SimpleServo(opMode.getOpMode().hardwareMap, Names.wrist, 0, 300);

        spinner = hMap.get(CRServo.class, Names.spinTake);
        //colorSensor = hMap.get(ColorSensor.class, Names.color);
        telemetry = opMode.getOpMode().telemetry;

    }

    @Override
    public void postUserStartHook(@NonNull Wrapper opMode) {
        getColor();
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
                .setExecute(() -> wrist.turnToAngle(raisePos))
                .setFinish(() -> !raised);
    }
    public static Lambda drop() {
        return new Lambda("drop-intake")
                .setInit(() -> {raised = false; stored = false;})
                .setExecute(() -> wrist.turnToAngle(dropPos))
                .setFinish(() -> raised);
    }
    public static Lambda store() {
        return new Lambda("store-intake")
                .setInit(() -> stored = true)
                .setExecute(() -> wrist.turnToAngle(storePos))
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

    public static Lambda getColor(){
        return new Lambda("get-color")
                .setExecute(() -> {
                    double red = colorSensor.red();
                    double green = colorSensor.green();
                    double blue = colorSensor.blue();
                    if (Math.abs(1- (red/green)) < 0.1 && red>blue*1.3){
                        telemetry.addLine("Color: Yellow");
                    }
                    if (red>green*1.3 && red>blue*1.3){
                        telemetry.addLine("Color: Red");
                    }
                    if (blue>green*1.3 && blue>red*1.3){
                        telemetry.addLine("Color: Blue");
                    }
                });
    }

    public static Lambda runManual(double angle){
        return new Lambda("set-power-up")
                .setInit(() -> wrist.rotateByAngle(angle))
                .setFinish(() -> true);
    }

    public static double getPositionWrist(){
        return wrist.getAngle();
    }

    public static void logTele(){
        telemetry.addLine("Current Wrist Location" + getPositionWrist());
        telemetry.addLine("SpinTake Power" + spinner.getPower());
        telemetry.addLine("Raised: " + raised);
        telemetry.addLine("Stored: " + stored);
    }
}
