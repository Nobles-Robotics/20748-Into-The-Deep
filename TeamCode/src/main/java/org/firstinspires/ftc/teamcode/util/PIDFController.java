package org.firstinspires.ftc.teamcode.util;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import dev.frozenmilk.dairy.core.Feature;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.lazy.Yielding;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;

public class PIDFController implements Feature {
    // first, we need to set up the dependency
    // Yielding just says "this isn't too important, always attach me, but run me after more important things"
    // Yielding is reusable!
    private Dependency<?> dependency = Yielding.INSTANCE;
    @NonNull
    @Override
    public Dependency<?> getDependency() {
        return dependency;
    }

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) {
        this.dependency = dependency;
    }
    private double kP, kI, kD, kF;
    private double measuredValue;
    private double minIntegral, maxIntegral;

    private double errorVal_p;
    private double errorVal_v;

    private double totalError;
    private double prevErrorVal;

    private double errorTolerance_p = 0.05;
    private double errorTolerance_v = Double.POSITIVE_INFINITY;

    private double lastTimeStamp;
    private double period;
    private MotorEx Motor;
    private MotorGroup MotorGroup;


    public PIDFController(double kp, double ki, double kd, double kf, MotorEx motor, MotorGroup motorGroup) {
        kP = kp;
        kI = ki;
        kD = kd;
        kF = kf;
        Motor = motor;
        MotorGroup = motorGroup;
        reset();
    }

    {
        // regardless of constructor used, call register when the class is instantiated
        register();
    }
    public void reset() {
        totalError = 0;
        prevErrorVal = 0;
        lastTimeStamp = 0;
    }
    private void update() {
        prevErrorVal = errorVal_p;

        double currentTimeStamp = (double) System.nanoTime() / 1E9;
        double pv = Motor.getCurrentPosition();
        if (lastTimeStamp == 0) lastTimeStamp = currentTimeStamp;
        period = currentTimeStamp - lastTimeStamp;
        lastTimeStamp = currentTimeStamp;

        if (measuredValue == pv) {
            errorVal_p = getTarget() - measuredValue;
        } else {
            errorVal_p = getTarget() - pv;
            measuredValue = pv;
        }

        if (Math.abs(period) > 1E-6) {
            errorVal_v = (errorVal_p - prevErrorVal) / period;
        } else {
            errorVal_v = 0;
        }

        /*
        if total error is the integral from 0 to t of e(t')dt', and
        e(t) = sp - pv, then the total error, E(t), equals sp*t - pv*t.
         */
        totalError += period * (getTarget() - measuredValue);
        totalError = totalError < minIntegral ? minIntegral : Math.min(maxIntegral, totalError);

        // returns u(t)


        if (!enabled) return;

       Motor.set(kP * errorVal_p + kI * totalError + kD * errorVal_v + kF * getTarget());
    }

    // users should be able to change the target
    private int target = 0;

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    // users should be able to enable / disable the controller
    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    // after init loop and loop we will update the controller
    @Override
    public void postUserInitLoopHook(@NonNull Wrapper opMode) {
        update();
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {
        update();
    }

    // in cleanup we deregister, which prevents this from sticking around for another OpMode,
    // unless the user calls register again
    @Override
    public void cleanup(@NonNull Wrapper opMode) {
        deregister();
    }
}
