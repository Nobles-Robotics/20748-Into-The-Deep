package org.firstinspires.ftc.teamcode.teleop.bot;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.FtcDashboard;
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
import org.firstinspires.ftc.teamcode.util.Names;

import java.lang.annotation.*;

@Config
public class Slides implements Subsystem {
    public static final Slides INSTANCE = new Slides();
    public static DcMotorEx slideR;
    public static DcMotorEx slideE;

    public static Telemetry telemetry;
    public static int tolerance = 10;
    public static int safePos = 400;
    public static int wall = safePos;
    public static int scoreHighPos = 4700;
    public static int scoreLowPos = 2000;
    public static int lowPos = 2500;
    public static int highPos = 7000;
    public static double Kp = 0.0025;
    public static double Ki = 0;
    public static double Kd = 0;
    public static double Kf = 0.0000;
    public static double maxCurrentLimit = 3500;
    public static double regressionCurrentLimit = 800;
    public static volatile boolean enablePID = true;
    public static boolean climbOver = false;
    private static double currentE = 0, currentR = 0;
    FtcDashboard dashboard;
    static Telemetry dashboardTelemetry;

    public static PIDFController controller = new PIDFController(Kp, Ki, Kd, Kf);

    private Slides() {}

    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE) @MustBeDocumented
    @Inherited
    public @interface Attach { }

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        HardwareMap hMap = opMode.getOpMode().hardwareMap;
        telemetry = opMode.getOpMode().telemetry;

        dashboardTelemetry = FtcDashboard.getInstance().getTelemetry();

        slideE = hMap.get(DcMotorEx.class, Names.vertSlideUp);
        slideR = hMap.get(DcMotorEx.class, Names.vertSlideDown);
        slideR.setCurrentAlert(maxCurrentLimit, CurrentUnit.MILLIAMPS);
        slideE.setCurrentAlert(maxCurrentLimit, CurrentUnit.MILLIAMPS);
        slideR.setDirection(DcMotorSimple.Direction.REVERSE);
        slideE.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        reset();

        controller.setTolerance(tolerance);

        setDefaultCommand(runPID());
    }

    @Override
    public void postUserInitHook(@NonNull Wrapper opMode) {
        runPID();
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {}

    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotation<>(Attach.class));

    @NonNull
    @Override
    public Dependency<?> getDependency() {return dependency;}

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) {this.dependency = dependency;}

    public static void setPower(double power){
        if (power > 0) {
//            if (power > 0.5){
//                power = 0.5;
//            }
            setPowerE(power);
            //setPowerR(-power * 1.75, true);
        } else if (power < 0) {
//            if (power < -0.5){
//                power = -0.5;
//            }
            setPowerE(power);
            //setPowerR(-power * 2, false);
        } else {
            setPowerE(0);
            //setPowerR(0, true);
        }
    }

    public static void setPowerE(double power){
        if (!(slideE.getCurrent(CurrentUnit.MILLIAMPS) > maxCurrentLimit)) {
            slideE.setPower(power);
        } else {
            slideE.setPower(0);
        }
    }

    public static void setPowerR(double power, boolean current){
        if (!(slideR.getCurrent(CurrentUnit.MILLIAMPS) > maxCurrentLimit)) {
            slideR.setPower(power);
        } else {
            slideR.setPower(0);
        }
    }

    public static boolean isOverCurrent(double limit) {
        return slideR.getCurrent(CurrentUnit.MILLIAMPS) > limit || slideE.getCurrent(CurrentUnit.MILLIAMPS) > limit;
    }

    public static void reset() {
        slideE.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideE.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        controller.reset();
        controller.setSetPoint(0);
    }

    public static Lambda resetCommand(){
        return new Lambda("reset-slides")
                .setInit(Slides::reset)
                .setFinish(() -> true);
    }

    public static Lambda runToPosition(double pos){
        return new Lambda("set-target-pos")
                .setInterruptible(true)
                .setInit(() -> {
                    controller.setSetPoint(pos);
                    enablePID = true;
                })
                .setFinish(() -> controller.atSetPoint())
                .setEnd((interrupted) -> {
                    if (!interrupted) {
                        removeSlack();
                    }
                });
    }

    public static double getPos(){
        return slideE.getCurrentPosition();
    }


    public static Lambda runPID() {
        return new Lambda("outtake-pid")
                .setExecute(() -> {
                    if (enablePID) {
                        double power = controller.calculate(getPos());
                        setPower(power);
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
                .setFinish(() -> isOverCurrent(maxCurrentLimit))
                .setEnd((interrupted) -> {
                    if (!interrupted) {
                        removeSlack();
                        reset();
                    }
                    setPower(0);
                    enablePID = true;
                });
    }

    public static Lambda climb(){
        return new Lambda("climb-slides")
                .setInit(() -> {
                    enablePID = false;
                    slideR.setPower(-1);
                })
                .setFinish(() -> climbOver)
                .setEnd((interrupted) -> slideR.setPower(0.4));
    }

    public static Lambda setClimbOver(boolean over){
        return new Lambda("remove-regressor-slack")
                .setInit(() -> climbOver = over)
                .setFinish(() -> true);
    }

    public static Lambda removeSlack(){
        return new Lambda("remove-regressor-slack")
                .setInit(() -> slideR.setPower(-1))
                .setFinish(() -> isOverCurrent(regressionCurrentLimit))
                .setEnd((interrupted) -> slideR.setPower(0));
    }

    public static Lambda waitForPos(int pos) {
        return new Lambda("wait-for-pos")
                .setFinish(() -> Math.abs(getPos() - pos) < tolerance);
    }

    public static Lambda setPowerUp(double pow){
        return new Lambda("set-power-up")
                .setInit(() -> {
                    enablePID = false;
                    setPowerE(pow);
                    //slideE.setPower(pow);
                })
                .setFinish(() -> true)
                .setEnd((interrupted) -> {
                    slideE.setPower(0);
                    controller.setSetPoint(slideE.getCurrentPosition());
                    enablePID = true;
                });
    }
    public static Lambda setPowerDown(double pow){
        return new Lambda("set-power-up")
                .setInit(() -> {
                    enablePID = false;
                    setPowerR(pow, false);
                    //slideR.setPower(pow);
                })
                .setFinish(() -> true)
                .setEnd((interrupted) -> {
                    slideR.setPower(0);
                    controller.setSetPoint(slideE.getCurrentPosition());
                    enablePID = true;
                });
    }


    public static void logTele(Bot.Logging level){
        switch (level) {
            case NORMAL:
                telemetry.addLine("Slides: pid? "+ enablePID + " | SetPoint:" + controller.getSetPoint());
                telemetry.addData("Position: ", getPos());
                break;
            case DISABLED:
                break;
            case VERBOSE:
                telemetry.addLine("Slides: pid? " + enablePID);
                telemetry.addLine("Position: " + getPos() + " | Error: " + controller.getPositionError());
                telemetry.addLine("Up:" + slideE.getPower() + " | Down:" + slideR.getPower());
                telemetry.addLine("Current: " + isOverCurrent(maxCurrentLimit) + " | Max: " + maxCurrentLimit);
                telemetry.addLine("Slack: " + isOverCurrent(regressionCurrentLimit) + " | Max: " + regressionCurrentLimit);
                telemetry.addLine("Up: " + slideE.getCurrent(CurrentUnit.MILLIAMPS) + " | Down: " + slideR.getCurrent(CurrentUnit.MILLIAMPS));
                telemetry.addLine("Setpoint:" + controller.atSetPoint() + " | " + controller.getSetPoint());
                if (slideE.getCurrent(CurrentUnit.MILLIAMPS) > currentE){
                    currentE = slideE.getCurrent(CurrentUnit.MILLIAMPS);
                }
                if (slideR.getCurrent(CurrentUnit.MILLIAMPS) > currentR){
                    currentR = slideR.getCurrent(CurrentUnit.MILLIAMPS);
                }
                telemetry.addLine("Max SlideE: " + currentE + " | Max SlideR: " + currentR);

                dashboardTelemetry.addData("current position", slideE.getCurrentPosition());
                dashboardTelemetry.addData("desired position", controller.getSetPoint());
        }
    }
}
