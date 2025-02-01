package org.firstinspires.ftc.teamcode.util.Features;

import androidx.annotation.NonNull;
import com.qualcomm.hardware.lynx.LynxModule;
import dev.frozenmilk.dairy.core.Feature;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;
import java.util.List;

public class BulkReads implements Feature {
    private Dependency<?> dependency = new SingleAnnotation<>(Attach.class);
    @NonNull
    @Override
    public Dependency<?> getDependency() { return dependency; }

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) {
        this.dependency = dependency;
    }

    private BulkReads() {}
    public static final BulkReads INSTANCE = new BulkReads();
    private static List<LynxModule> allHubs;
    private void clearCache(){ for (LynxModule hub : allHubs) { hub.clearBulkCache(); }}
    @Override
    public void preUserInitHook(@NotNull Wrapper opMode) {}

    @Override
    public void postUserInitHook(@NotNull Wrapper opMode) {
        allHubs = opMode.getOpMode().hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) { hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL); }
    }

    @Override
    public void preUserInitLoopHook(@NotNull Wrapper opMode) { clearCache(); }

    @Override
    public void preUserStartHook(@NotNull Wrapper opMode) { clearCache(); }

    @Override
    public void preUserLoopHook(@NotNull Wrapper opMode) { clearCache(); }

    @Override
    public void preUserStopHook(@NotNull Wrapper opMode) { clearCache(); }

    @Override
    public void cleanup(@NonNull Wrapper opMode) { allHubs.clear(); }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    public @interface Attach {}
}
