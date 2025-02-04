package org.firstinspires.ftc.teamcode.teleop.bot;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import java.lang.annotation.*;

@Config
public class Slides implements Subsystem {
    public static final Slides INSTANCE = new Slides();
    public static DcMotorEx slideR;
    public static DcMotorEx slideE;
    public static DcMotorEx encoder;

    public static Telemetry telemetry;
    public static int tolerance = 10;
    public static int safePos = 100;
    public static int wall = safePos;
    public static int scoreHighPos = 3000;
    public static int scoreLowPos = 2000;
    public static int lowPos = 2500;
    public static int highPos = 3500;
    public static double Kp = 0.00014;
    public static double Ki = 0.0000;
    public static double Kd = 0.0000;
    public static double Kf = 0.0000;
    public static double currentLimit = 4;
    public static volatile boolean enablePID = true;
    public static boolean climbOver = false;

    public static PIDFController controller = new PIDFController(Kp, Ki, Kd, Kf);

    private Slides() {}

    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE) @MustBeDocumented
    @Inherited
    public @interface Attach { }

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        HardwareMap hMap = opMode.getOpMode().hardwareMap;
        telemetry = opMode.getOpMode().telemetry;
        slideE = hMap.get(DcMotorEx.class, "vertSlideUp");
        slideR = hMap.get(DcMotorEx.class, "vertSlideDown");
        slideR.setCurrentAlert(currentLimit, CurrentUnit.AMPS);
        slideE.setCurrentAlert(currentLimit, CurrentUnit.AMPS);
        slideR.setDirection(DcMotorSimple.Direction.REVERSE);
        slideE.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        reset();

        controller.setTolerance(tolerance);

        setDefaultCommand(runPID());
    }

    @Override
    public void postUserInitHook(@NonNull Wrapper opMode) {
        Bot.init();
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {}

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

    public static void setPower(double power){
        if (power > 0){
            slideE.setPower(power);
            slideR.setPower(-power);
        } else if (power < 0){
            slideE.setPower(power);
            slideR.setPower(-power);
        } else {
            removeRegressorSlack();
            slideE.setPower(0);
        }
    }

    public static Lambda setPowerPersistantCommand(double power){
        return new Lambda("set-power")
                .setExecute(() -> {
                    if (power != 0) {
                        enablePID = false;
                        setPower(power);
                        controller.setSetPoint(getPos());
                        logTele();
                    } else {
                        enablePID = true;
                        controller.setSetPoint(getPos());
                        setPower(0);
                    }
                });
    }
    public static Lambda setPowerTransientCommand(double power){
        return new Lambda("set-power")
                .setExecute(() -> {
                    if (power != 0) {
                        enablePID = false;
                        setPower(power);
                        controller.setSetPoint(getPos());
                        logTele();
                    } else {
                        enablePID = true;
                        controller.setSetPoint(getPos());
                        setPower(0);
                    }
                })
                .setFinish(() -> true)
                .setEnd((interrupted) -> {
                    enablePID = true;
                    controller.setSetPoint(getPos());
                    setPower(0);
                });
    }

    public static boolean isOverCurrent() {
        return slideR.isOverCurrent() || slideE.isOverCurrent();
    }

    public static void reset() {
        slideE.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        controller.reset();
        controller.setSetPoint(0);
        logTele();
    }

    public static Lambda runToPosition(double pos){
        return new Lambda("set-target-pos")
                .setInterruptible(true)
                .setInit(() -> controller.setSetPoint(pos))
                .setFinish(() -> controller.atSetPoint());
    }

    public static void logCurrent(){
        telemetry.addData("isOver", isOverCurrent());
        telemetry.update();
    }

    public static double getPos(){
        return encoder.getCurrentPosition();
    }

    public static void logTele(){
        telemetry.addData("Slide Pos", getPos());
        telemetry.addData("Slide Power", slideE.getPower());
        telemetry.addData("Slide Setpoint", controller.getSetPoint());
        telemetry.addData("Slide Error", controller.getPositionError());
        telemetry.addData("At Setpoint?", controller.atSetPoint());
        telemetry.addData("Enable PID", enablePID);
    }

    public static Lambda runPID() {
        return new Lambda("outtake-pid")
                .addRequirements(INSTANCE)
                .setInterruptible(true)
                .setExecute(() -> {
                    if (enablePID) {
                        double power = controller.calculate(getPos());
                        setPower(power);
                        logTele();
                    }
                })
                .setFinish(() -> false);
    }

    public static Lambda home() {
        return new Lambda("home-outtake")
                .setInit(() -> {
                    enablePID = false;
                    setPower(-1);
                })
                .setFinish(Slides::isOverCurrent)
                .setEnd((interrupted) -> {
                    if (!interrupted) {
                        reset();
                    }
                    setPower(0);
                    enablePID = true;
                });
    }

    public static Lambda climb(){
        return new Lambda("climb-slides")
                //TODO: CLIMB LOGIC :sob:
                .setInit(() -> {
                    enablePID = false;
                    slideR.setPower(-1);
                })
                .setFinish(() -> climbOver)
                .setEnd((interrupted) -> {
                    slideR.setPower(0.4);
                });
    }
    public static Lambda removeRegressorSlack(){
        return new Lambda("remove-regressor-slack")
                .setInit(() -> {enablePID = false; slideR.setPower(-1);})
                .setFinish(() -> slideR.isOverCurrent());
    }

    public static Lambda waitForPos(int pos) {
        return new Lambda("wait-for-pos")
                .setFinish(() -> Math.abs(getPos() - pos) < tolerance);
    }

    public static Lambda setPowerSafe(int pow){
        return new Lambda("set-power-safe")
                .setInit(() -> {
                    enablePID = false;
                    setPower(pow);
                })
                .setFinish(Slides::isOverCurrent);
    }
}
