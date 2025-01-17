package org.firstinspires.ftc.teamcode.teleop.bot;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import kotlin.annotation.MustBeDocumented;
public class Drive implements Subsystem {

    public static final Drive INSTANCE = new Drive();

    private Drive() { }
    private MotorEx fl, fr, br, bl;
    private IMU imu;

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach{}
    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotation<>(Attach.class));

    @NonNull
    @Override
    public Dependency<?> getDependency() { return dependency; }

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) { this.dependency = dependency; }


    private final boolean fieldCentric = false;
    @Override
    public void postUserInitHook(@NonNull Wrapper opMode) {
        HardwareMap hwmap = opMode.getOpMode().hardwareMap;
        fl = new MotorEx(hwmap, "motorFL");
        fr = new MotorEx(hwmap, "motorFR");
        br = new MotorEx(hwmap, "motorBR");
        bl = new MotorEx(hwmap, "motorBL");
        imu = hwmap.get(IMU.class, "imu");

        initializeDrive();
        stopDriveMotors();
        setDefaultCommand(drive(fieldCentric));
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {
        drive(fieldCentric);
    }

    @Override
    public void postUserStopHook(@NonNull Wrapper opMode) {}

    @Override
    public void cleanup(@NonNull Wrapper opMode) {}
    @NonNull
    public Lambda drive(boolean fieldCentric) {
        return new Lambda("drive")
                .addRequirements(INSTANCE)
                .setExecute(() -> {
                    double rightX = Mercurial.gamepad2().leftStickX().state();
                    double rightY = Mercurial.gamepad2().leftStickY().state();
                    double turn = Mercurial.gamepad2().rightStickX().state() * 1;
                    double heading = 0;
                    double rotX = (rightX * Math.cos(-heading) - rightY * Math.sin(-heading)) * 1.1;
                    double rotY = rightX * Math.sin(-heading) + rightY * Math.cos(-heading);
                    double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(turn), 1);
                    double lfPower = (rotY + rotX + turn) / denominator;
                    double blPower = (rotY - rotX + turn) / denominator;
                    double frPower = (rotY - rotX - turn) / denominator;
                    double brPower = (rotY + rotX - turn) / denominator;
                    fl.set(lfPower);
                    bl.set(blPower);
                    fr.set(frPower);
                    br.set(brPower);
                })
                .setFinish(() -> false);
    }

    private static double[] parseSpeeds(double[] speeds) {
        double maxSpeed = 0;

        for (double speed : speeds) {
            maxSpeed = Math.max(maxSpeed, speed);
        }
        if (maxSpeed > 1) {
            for (int i = 0; i < speeds.length; i++) {
                speeds[i] /= maxSpeed;
            }
        }
        return speeds;
    }
    public void initializeDrive() {

        fl.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        fl.setInverted(false);
        fr.setInverted(true);
        bl.setInverted(false);
        br.setInverted(true);

        fl.setRunMode(Motor.RunMode.RawPower);
        fr.setRunMode(Motor.RunMode.RawPower);
        bl.setRunMode(Motor.RunMode.RawPower);
        br.setRunMode(Motor.RunMode.RawPower);

        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD));

        imu.initialize(parameters);
    }
    public void stopDriveMotors() {
        fl.set(0.0);
        fr.set(0.0);
        bl.set(0.0);
        br.set(0.0);
    }
}
