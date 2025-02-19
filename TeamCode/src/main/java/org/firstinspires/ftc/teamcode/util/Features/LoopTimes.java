package org.firstinspires.ftc.teamcode.util.Features;

import androidx.annotation.NonNull;
import dev.frozenmilk.dairy.core.Feature;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

public class LoopTimes implements Feature {
    private Dependency<?> dependency = new SingleAnnotation<>(Attach.class);

    @NonNull
    @Override
    public Dependency<?> getDependency() { return dependency; }

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) {
        this.dependency = dependency;
    }

    private LoopTimes() {}

    public static LoopTimes INSTANCE = new LoopTimes();
    //TODO: MAKE THIS NOT FINAL
    private final long startTime = System.nanoTime();
    private long lastTime = startTime;
    private int loops = 0;


    private void time(@NonNull Telemetry telemetry){
        long currentTime = System.nanoTime();
        double instantLoopTime = .000001 * (currentTime - lastTime);
        double instantHz = 1 / (instantLoopTime / 1000);
        double averageLoopTime = (.000001 * (currentTime - startTime)) / loops;
        double averageHz = loops / (averageLoopTime / 1000);

        telemetry.addData("Instantaneous Loop Time", instantLoopTime);
        telemetry.addData("Instantaneous Loop Hz", instantHz);
        telemetry.addData("Average Loop Time", averageLoopTime);
        telemetry.addData("Average Loop Hz", averageHz);

        lastTime = currentTime;
        loops += 1;
    }

    @Override
    public void preUserInitHook(@NotNull Wrapper opMode) {
    }

    @Override
    public void postUserInitHook(@NotNull Wrapper opMode) {
        time(opMode.getOpMode().telemetry);
    }

    @Override
    public void postUserInitLoopHook(@NotNull Wrapper opMode) {
        time(opMode.getOpMode().telemetry);
    }

    @Override
    public void postUserStartHook(@NotNull Wrapper opMode) {
        time(opMode.getOpMode().telemetry);
    }

    @Override
    public void postUserLoopHook(@NotNull Wrapper opMode) {
        time(opMode.getOpMode().telemetry);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    public @interface Attach {}
}
