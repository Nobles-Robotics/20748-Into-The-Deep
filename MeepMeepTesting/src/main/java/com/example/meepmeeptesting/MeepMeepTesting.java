package com.example.meepmeeptesting;


import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        double specimen1 = 46;
        double specimen2 = 56;
        double specimen3 = 63;
        double specimenMax = -10;
        double specimenClose = -65;
        double specimenReturn = -20;
        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(0, -63, Math.toRadians(90)))
                .strafeTo(new Vector2d(0, -43))
                .strafeTo(new Vector2d(0, -34))
                .splineToConstantHeading(new Vector2d(24, -36), Math.toRadians(0))

                .splineToConstantHeading(new Vector2d(specimen1, specimenMax), Math.toRadians(0))
                .strafeTo(new Vector2d(specimen1, specimenClose))
                .strafeTo(new Vector2d(specimen1, specimenReturn))

                .splineToConstantHeading(new Vector2d(specimen2, specimenMax), Math.toRadians(0))
                .strafeTo(new Vector2d(specimen2, specimenClose))
                .strafeTo(new Vector2d(specimen2, specimenReturn))

                .splineToConstantHeading(new Vector2d(specimen3, specimenMax), Math.toRadians(0))
                .strafeTo(new Vector2d(specimen3, specimenClose))

                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
