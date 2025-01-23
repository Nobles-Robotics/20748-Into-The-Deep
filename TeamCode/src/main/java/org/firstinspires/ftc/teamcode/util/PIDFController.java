package org.firstinspires.ftc.teamcode.util;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;

import dev.frozenmilk.dairy.core.Feature;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.lazy.Yielding;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;

public class PIDFController implements Feature {

    private Dependency<?> dependency = Yielding.INSTANCE;

    private double kP, kI, kD, kF;
    private double totalError, prevErrorVal;
    private double minIntegral = -1.0, maxIntegral = 1.0;
    private double errorTolerance_p = 0.05, errorTolerance_v = Double.POSITIVE_INFINITY;
    private double lastTimeStamp, period;
    private boolean enabled = true;

    private final Motor motor;
    private final MotorGroup motorGroup;
    private final boolean isMotorGroup;

    public PIDFController(Motor motor, double kP, double kI, double kD, double kF) {
        this.motor = motor;
        this.motorGroup = null;
        this.isMotorGroup = false;
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
        register();
    }

    public PIDFController(MotorGroup motorGroup, double kP, double kI, double kD, double kF) {
        this.motor = null;
        this.motorGroup = motorGroup;
        this.isMotorGroup = true;
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
        register();
    }

    @NonNull
    @Override
    public Dependency<?> getDependency() {
        return dependency;
    }

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) {
        this.dependency = dependency;
    }

    private int target = 0;

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public boolean atTarget() {
        double currentPosition = isMotorGroup ? motorGroup.getCurrentPosition() : motor.getCurrentPosition();
        return Math.abs(target - currentPosition) < errorTolerance_p;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void reset() {
        totalError = 0;
        prevErrorVal = 0;
        lastTimeStamp = 0;
    }

    private void update() {
        if (!enabled) return;
        double currentPosition = isMotorGroup ? motorGroup.getCurrentPosition() : motor.getCurrentPosition();
        double error = target - currentPosition;

        double currentTimeStamp = System.nanoTime() / 1E9;
        if (lastTimeStamp == 0) lastTimeStamp = currentTimeStamp;
        period = currentTimeStamp - lastTimeStamp;
        lastTimeStamp = currentTimeStamp;

        double derivative = period > 1E-6 ? (error - prevErrorVal) / period : 0;
        totalError += error * period;
        totalError = Math.max(minIntegral, Math.min(maxIntegral, totalError));

        double output = kP * error + kI * totalError + kD * derivative + kF * target;
        prevErrorVal = error;

        if (isMotorGroup) {
            motorGroup.set(output);
        } else {
            motor.set(output);
        }
    }

    @Override
    public void postUserInitLoopHook(@NonNull Wrapper opMode) {
        update();
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {
        update();
    }

    @Override
    public void cleanup(@NonNull Wrapper opMode) {
        deregister();
    }
}
