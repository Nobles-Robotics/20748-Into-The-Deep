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
    public void postUserInitHook(@NonNull Wrapper opMode) {
        telemetry = opMode.getOpMode().telemetry;
        gripperL = new SimpleServo(opMode.getOpMode().hardwareMap, Names.gripper, 0, 300);
        open();
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {

        //runManual();
    }

    @Override
    public void postUserStopHook(@NonNull Wrapper opMode) {}

    @Override
    public void cleanup(@NonNull Wrapper opMode) {}

    @NonNull
    public static Lambda open() {
        return new Lambda("open")
                .addRequirements(INSTANCE)
                .setInit(() -> gripperL.turnToAngle(230))
                .setFinish(() -> true);
    }
    @NonNull
    public static Lambda close() {
        return new Lambda("close")
                .addRequirements(INSTANCE)
                .setInit(() -> gripperL.turnToAngle(205))
                .setFinish(() -> true);
    }
    public static double getPositionGripperL(){
        return gripperL.getAngle();
    }

    public static void logTele(){
        telemetry.addLine("Current Gripper Location" + getPositionGripperL());
    }

    public static Lambda runManual(double angle){
        return new Lambda("set-power-up")
                .setInit(() -> gripperL.rotateByAngle(angle))
                .setFinish(() -> true);
    }
}
