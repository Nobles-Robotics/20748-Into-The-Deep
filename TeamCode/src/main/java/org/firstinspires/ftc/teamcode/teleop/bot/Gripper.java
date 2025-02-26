package org.firstinspires.ftc.teamcode.teleop.bot;

import androidx.annotation.NonNull;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.Names;

import java.lang.annotation.*;

public class Gripper implements Subsystem {
    public static final Gripper INSTANCE = new Gripper();

    private Gripper() { }
    private static Telemetry telemetry;
    private static SimpleServo gripperL;
    public static double gripperLocation = 0;
    public static double closePos = 0;
    public static double openPos = 30;

    public static boolean isGripperOpen = true;

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach{}
    private Dependency<?> dependency =
            Subsystem.DEFAULT_DEPENDENCY
                    .and(new SingleAnnotation<>(Attach.class));

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
        telemetry = opMode.getOpMode().telemetry;
        gripperL = new SimpleServo(opMode.getOpMode().hardwareMap, Names.gripper, 0, 355);
        //setDefaultCommand(runGripper());
    }
    @Override
    public void postUserInitHook(@NonNull Wrapper opMode) {
        //runGripper();
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {

        //runManual();
    }

    @Override
    public void postUserStopHook(@NonNull Wrapper opMode) {}

    @Override
    public void cleanup(@NonNull Wrapper opMode) {}


    public static Lambda runToPosition(double pos){
        return new Lambda("set-target-pos")
                .setInit(() -> {
                    gripperLocation = pos;
                })
                .setFinish(() -> true);
    }

    public static double getPositionGripper(){
        return gripperL.getAngle();
    }

    public static Lambda runManual(double angle){
        return new Lambda("set-power-up")
                .setInit(() -> gripperL.rotateByAngle(angle))
                .setFinish(() -> true);
    }

    public static Lambda close(){
        return new Lambda("set-power-up")
                .setInit(() -> gripperL.turnToAngle(0))
                .setFinish(() -> true);
    }

    public static Lambda open(){
        return new Lambda("set-power-up")
                .setInit(() -> gripperL.rotateByAngle(30))
                .setFinish(() -> true);
    }

    public static Lambda runGripper() {
        return new Lambda("outtake-pid")
                .setExecute(() -> gripperL.turnToAngle(gripperLocation))
                .setFinish(() -> false);
    }

    public static void logTele(Bot.Logging level){
        switch (level) {
            case NORMAL:
                telemetry.addData("Current Gripper Location", getPositionGripper());
                telemetry.addData("Gripper Open", isGripperOpen);
                break;
            case DISABLED:
                break;
            case VERBOSE:
                telemetry.addData("Current Gripper Location", getPositionGripper());
                telemetry.addData("Gripper Open", isGripperOpen);
        }
    }
}
