/*
THIS FILE AIMS AS A BASIS FOR ALL SUBSYSTEMS
 */

package org.firstinspires.ftc.teamcode.teleop.bot;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.geometry.Vector2d;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;
import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.commands.stateful.StatefulLambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.mercurial.subsystems.SubsystemObjectCell;
import dev.frozenmilk.util.cell.RefCell;
import kotlin.annotation.MustBeDocumented;
import org.firstinspires.ftc.teamcode.auto.MecanumDrive;
public class Drive implements Subsystem {

    public static final Drive INSTANCE = new Drive();


    private Drive() { }

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

    private final SubsystemObjectCell<MotorEx> fl = subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(MotorEx.class, "motorFL"));
    private final SubsystemObjectCell<MotorEx> fr = subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(MotorEx.class, "motorFR"));
    private final SubsystemObjectCell<MotorEx> bl = subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(MotorEx.class, "motorBL"));
    private final SubsystemObjectCell<MotorEx> br = subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(MotorEx.class, "motorBR"));
    private final SubsystemObjectCell<IMU> imu = subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(IMU.class, "imu"));


    public static MotorEx getFL() {
        return INSTANCE.fl.get();
    }
    public static MotorEx getFR() {
        return INSTANCE.fr.get();
    }
    public static MotorEx getBL() {
        return INSTANCE.bl.get();
    }
    public static MotorEx getBR() {
        return INSTANCE.br.get();
    }
    public static IMU getIMU(){
        return INSTANCE.imu.get();
    }

    private final boolean fieldCentric = false;
    @Override
    public void postUserInitHook(@NonNull Wrapper opMode) {
        setDefaultCommand(drive(fieldCentric));
        initializeDrive();
        stopDriveMotors();
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
    public static Lambda drive(boolean fieldCentric) {
        return new Lambda("drive")
                .addRequirements(INSTANCE)
                .setExecute(() -> {
                    if (fieldCentric){
                        double driveSpeed = 1;
                        double rightX = Mercurial.gamepad1().leftStickX().state();
                        double rightY = Mercurial.gamepad1().leftStickY().state();
                        Vector2d driveVector = new Vector2d(-rightX, -rightY),
                                turnVector = new Vector2d(-rightX, 0);
                        driveFieldCentric(driveVector.getX() * driveSpeed, driveVector.getY() * driveSpeed, turnVector.getX() * driveSpeed);
                    } else {
                        double driveSpeed = 1;
                        double rightX = Mercurial.gamepad1().leftStickX().state();
                        double rightY = Mercurial.gamepad1().leftStickY().state();
                        Vector2d driveVector = new Vector2d(rightX, -rightY),
                                turnVector = new Vector2d(rightX, 0);
                        driveRobotCentric(driveVector.getX() * driveSpeed, driveVector.getY() * driveSpeed, turnVector.getX() * driveSpeed);
                    }
                });
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

    public static void driveRobotCentric(double strafeSpeed, double forwardBackSpeed, double turnSpeed) {

        double[] speeds = {
                (forwardBackSpeed - strafeSpeed - turnSpeed),
                (forwardBackSpeed + strafeSpeed + turnSpeed),
                (forwardBackSpeed + strafeSpeed - turnSpeed),
                (forwardBackSpeed - strafeSpeed + turnSpeed),
        };
        parseSpeeds(speeds);

        getFL().set(speeds[0]);
        getFR().set(speeds[1]);
        getBL().set(speeds[2]);
        getBR().set(speeds[3]);
    }
    public static void driveFieldCentric(double strafeSpeed, double forwardBackSpeed, double turnSpeed) {
        double magnitude = Math.sqrt(strafeSpeed * strafeSpeed + forwardBackSpeed * forwardBackSpeed);
        double theta = (Math.atan2(forwardBackSpeed, strafeSpeed)) % (2 * Math.PI); //        double theta = (Math.atan2(forwardBackSpeed, strafeSpeed) - heading) % (2 * Math.PI);

        double speed = magnitude * Math.sin(theta + Math.PI / 4);
        double[] speeds = {
                speed + turnSpeed,
                speed - turnSpeed,
                speed + turnSpeed,
                speed - turnSpeed
        };
        parseSpeeds(speeds);

        getFL().set(speeds[0]);
        getFR().set(speeds[1]);
        getBL().set(speeds[2]);
        getBR().set(speeds[3]);
    }
    public void initializeDrive() {
        getFL().setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        getFR().setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        getBL().setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        getBR().setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        getFL().setInverted(false);
        getFR().setInverted(true);
        getBL().setInverted(false);
        getBR().setInverted(true);

        getFL().setRunMode(Motor.RunMode.RawPower);
        getFR().setRunMode(Motor.RunMode.RawPower);
        getBL().setRunMode(Motor.RunMode.RawPower);
        getBR().setRunMode(Motor.RunMode.RawPower);

        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD));

        getIMU().initialize(parameters);
    }
    public void stopDriveMotors() {
        getFL().set(0.0);
        getFR().set(0.0);
        getBL().set(0.0);
        getBR().set(0.0);
    }
}
