package org.firstinspires.ftc.teamcode.teleop.teleop;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import dev.frozenmilk.mercurial.Mercurial;
import org.firstinspires.ftc.teamcode.teleop.teleop.bot.Bot;
import org.firstinspires.ftc.teamcode.teleop.teleop.bot.Slides;
import dev.frozenmilk.dairy.core.FeatureRegistrar;

@Mercurial.Attach
@TeleOp(name = "MainTeleOp")
public class MainTeleOp extends OpMode {

    private Bot bot;
    private Slides slides;
    private double driveSpeed = 1, driveMultiplier = 1;
    private GamepadEx gp1, gp2;
    private boolean fieldCentric;

    public MainTeleOp() {
        FeatureRegistrar.checkFeatures(/* pass desired features as varargs here */);
    }


    @Override
    public void init() {
        bot = Bot.getInstance(this);

        gp1 = new GamepadEx(gamepad1);
        gp2 = new GamepadEx(gamepad2);

        bot.initializeBot();
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
        gp1.readButtons();
        gp2.readButtons();

        if (gp1.getButton(GamepadKeys.Button.A)) {
            slides.runToMM(1000);
        }
        if (gp1.getButton(GamepadKeys.Button.B)) {
            slides.runToMM(0);
        }
        telemetry.addData("Slides Position (mm)", slides.getMMPosition());
        telemetry.addData("Slides IK Position (mm)", slides.getIKMMPosition());
        telemetry.update();
        drive();
    }

    @Override
    public void stop() {
        // the rest is as normal
    }

    private void drive() {
        driveSpeed = driveMultiplier - 0.5 * gp1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER);
        driveSpeed = Math.max(0, driveSpeed);

        if (fieldCentric) {
            Vector2d driveVector = new Vector2d(-gp1.getLeftX(), -gp1.getLeftY()),
                    turnVector = new Vector2d(-gp1.getRightX(), 0);
            bot.driveFieldCentric(driveVector.getX() * driveSpeed,
                    driveVector.getY() * driveSpeed,
                    turnVector.getX() * driveSpeed
            );
        } else {
            Vector2d driveVector = new Vector2d(gp1.getLeftX(), -gp1.getLeftY()),
                    turnVector = new Vector2d(gp1.getRightX(), 0);
            bot.driveRobotCentric(driveVector.getX() * driveSpeed,
                    driveVector.getY() * driveSpeed,
                    turnVector.getX() * driveSpeed
            );
        }
    }
}


