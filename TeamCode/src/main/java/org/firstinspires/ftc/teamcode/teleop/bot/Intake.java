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
    public static double dropPos = 10;
    public static double raisePos = 355;
    public static double storePos = 0;
    public static double intakeLocation = 0;
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
        wrist2.getController().pwmDisable();
        wrist2.getController().pwmEnable();
        wrist2.setDirection(Servo.Direction.REVERSE);


        spinner = hMap.get(CRServo.class, Names.spinTake);
        colorSensor = hMap.get(ColorSensor.class, Names.color);
        telemetry = opMode.getOpMode().telemetry;
        setDefaultCommand(runGripper());
    }

    @Override
    public void postUserStartHook(@NonNull Wrapper opMode) {
        runGripper();
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

    public static Lambda runToPosition(double pos){
        return new Lambda("set-target-pos")
                .setInit(() -> {
                    intakeLocation = pos;
                })
                .setFinish(() -> true);
    }

    public static Lambda runGripper() {
        return new Lambda("outtake-pid")
                .setExecute(() -> wrist.turnToAngle(intakeLocation))
                .setFinish(() -> false);
    }
    public static Lambda runManual(double angle){
        return new Lambda("set-power-up")
                .setInit(() -> wrist.rotateByAngle(angle))
                .setFinish(() -> true);
    }

    public static Lambda raiseCommand() {
        return new Lambda("raise-command").setInit(() -> intakeLocation = 355).setFinish(() -> true);
    }
    public static Lambda dropCommand() {
        return new Lambda("raise-command").setInit(() -> intakeLocation = 0).setFinish(() -> true);
    }

    public static void logTele(Bot.Logging level){
        switch (level) {
            case NORMAL:
                telemetry.addData("Current Wrist Location", getPositionWrist());
                telemetry.addData("SpinTake Power", spinner.getPower());
                //TODO: Using a color sensor is known to affect performance namely loop times ~ this should be changed to only be active when the bot is actively intaking a sample
                computeColor();
                break;
            case DISABLED:
                break;
            case VERBOSE:
                telemetry.addData("Current Wrist Location", getPositionWrist());
                telemetry.addData("SpinTake Power", spinner.getPower());
                telemetry.addData("Raised: " , raised);
                telemetry.addData("Stored: ", stored);
                computeColor();

        }
    }

    public static void computeColor(){
        double red = colorSensor.red();
        double green = colorSensor.green();
        double blue = colorSensor.blue();
        telemetry.addLine("Red" + red + " | Blue" + blue + " | Green" + green);

        if (red>green*1.3 && red>blue*1.3){
            telemetry.addLine("Color: Red");
        } else if (blue>green*1.3 && blue>red*1.3){
            telemetry.addLine("Color: Blue");
        } else {
            telemetry.addLine("Nothing (or yellow :sob:)");
        }
    }
}
