package org.firstinspires.ftc.teamcode.teleop;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.commands.groups.Sequential;
import org.firstinspires.ftc.teamcode.teleop.bot.Bot;
import dev.frozenmilk.dairy.core.FeatureRegistrar;
import org.firstinspires.ftc.teamcode.teleop.bot.Drive;
import org.firstinspires.ftc.teamcode.teleop.bot.Slides;

@Mercurial.Attach
@Bot.Attach
@Drive.Attach
@Slides.Attach
@TeleOp(name = "MainTeleOp")
public class MainTeleOp extends OpMode {

    public MainTeleOp() {
        FeatureRegistrar.checkFeatures();
    }


    @Override
    public void init() {
        Mercurial.gamepad1().a().onTrue(Slides.runTo(3500));
    }
    @Override
    public void init_loop() {
        // the rest is as normal
    }

    @Override
    public void start() {
        // the rest is as normal
    }

    @Override
    public void loop() {

    }

    @Override
    public void stop() {
        // the rest is as normal
    }
}


