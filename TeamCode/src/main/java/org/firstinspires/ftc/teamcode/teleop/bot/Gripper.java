package org.firstinspires.ftc.teamcode.teleop.bot;

import androidx.annotation.NonNull;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;

import java.lang.annotation.*;

public class Gripper implements Subsystem {
    public static final Gripper INSTANCE = new Gripper();

    private Gripper() { }
    private static SimpleServo gripperL, gripperR;

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

    //Declares default command + Initializes the Subsystem
    @Override
    public void postUserInitHook(@NonNull Wrapper opMode) {
        gripperL = new SimpleServo(opMode.getOpMode().hardwareMap, "servo1", 0, 300);
        gripperR = new SimpleServo(opMode.getOpMode().hardwareMap, "servo3", 0, 300);
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
                .setInit(() -> {
                    gripperL.turnToAngle(93);
                    gripperR.turnToAngle(131);
                });
    }
    @NonNull
    public static Lambda close() {
        return new Lambda("close")
                .addRequirements(INSTANCE)
                .setInit(() -> {
                    gripperL.turnToAngle(85);
                    gripperR.turnToAngle(141);
                });
    }

    @NonNull
    public static Lambda runManual() {
        return new Lambda("runManual")
                .addRequirements(INSTANCE)
                .setInit(() -> {
                    double angle = parseGamepad();
                    gripperL.rotateByAngle(angle);
                    gripperR.rotateByAngle(angle);
                })
                .setEnd((interrupted) -> open().execute());
    }


    public static double parseGamepad(){
        double output = Mercurial.gamepad1().rightStickY().state();
        if ((output > 0.5 )){
            return 1;
        } else if (output < -0.5 ){
            return -1;
        }
        return 0;
    }

    public static double getPositionGripperL(){
        return gripperL.getPosition();
    }
    public static double getPositionGripperR(){
        return gripperR.getPosition();
    }
}
