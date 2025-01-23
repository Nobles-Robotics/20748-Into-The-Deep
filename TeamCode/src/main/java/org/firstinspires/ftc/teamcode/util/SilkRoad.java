package org.firstinspires.ftc.teamcode.util;

import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import dev.frozenmilk.dairy.core.Feature;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.Iterator;

public class SilkRoad implements Feature {
    private Dependency<?> dependency = new SingleAnnotation<>(Attach.class);
    private static FtcDashboard dash;
    private static Canvas canvas;
    private static final ArrayList<Action> actions = new ArrayList<Action>();
    @NonNull
    @Override
    public Dependency<?> getDependency() { return dependency; }

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) {
        this.dependency = dependency;
    }

    private SilkRoad() {}

    public static final SilkRoad INSTANCE = new SilkRoad();


    @Override
    public void postUserInitHook(@NotNull Wrapper opMode) {
        dash = FtcDashboard.getInstance();
        canvas = new Canvas();
    }

    @Override
    public void postUserLoopHook(@NotNull Wrapper opMode) {
        TelemetryPacket packet = new TelemetryPacket();
        packet.fieldOverlay().getOperations().addAll(canvas.getOperations());
        Iterator<Action> iter = actions.iterator();
        while (iter.hasNext() && !Thread.currentThread().isInterrupted()) {
            Action action = iter.next();
            if (!action.run(packet)) iter.remove();
        }
        dash.sendTelemetryPacket(packet);
    }

    @Override
    public void cleanup(@NotNull Wrapper opMode) {
        dash = null;
        canvas = null;
        actions.clear();
    }

    public static void runAsync(Action action){
        actions.add(action);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    public @interface Attach {}
}
