package org.firstinspires.ftc.teamcode.teleop.teleop;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.teleop.teleop.bot.Bot;
import org.firstinspires.ftc.teamcode.teleop.teleop.bot.Slides;

@TeleOp(name = "MainTeleOp")
public class MainTeleOp extends LinearOpMode {

    private Bot bot;
    private Slides slides;
    private double driveSpeed = 1, driveMultiplier = 1;
    private GamepadEx gp1;
    private boolean fieldCentric;

    @Override
    public void runOpMode() {

        bot = Bot.getInstance(this);
        slides = Slides.getInstance(this);

        gp1 = new GamepadEx(gamepad1);

        bot.initializeDrive();
        bot.stopDriveMotors();

        slides.initializeSlides();
        slides.stopSlideMotors();


        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {

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
