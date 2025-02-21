package org.firstinspires.ftc.teamcode.auto.Pathing;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Config
public class Paths {

    public static ArrayList<Path> fiveSpecs = new ArrayList<>();
    public static Path parkWithSpecPush;

    public static Pose pickup1 = new Pose(15,130.5, Math.toRadians(0));
    public static Pose pickup2 = new Pose(24,131.5, Math.toRadians(0));
    public static Pose pickup3 = new Pose(29,125, Math.toRadians(45));
    public static Pose samplePark = new Pose(60,100, Math.toRadians(270));
    public static Point parkControl = new Point(60, 110);

    public static Pose specStart = new Pose(9, 65, Math.toRadians(0));
    public static PathChain robotPush;

    public static double intakeOffset = -2;
    public static double intakeOffset1 = 2;
    public static double intakeOffset2 = 1;
    public static double intakeOffset3 = 1;
    public static double intakeOffset4 = 2;

    public static void init() {
        Collections.addAll(fiveSpecs,
                createPath( //preload 0
                        new BezierLine(
                                new Point(9.000, 65.000, Point.CARTESIAN),
                                new Point(42.00, 67.000, Point.CARTESIAN)
                        )
                ),
                createPath( // curve to first sample 1
                        new BezierCurve(
                                new Point(42.000, 67.000, Point.CARTESIAN),
                                new Point(31.500, 66.000, Point.CARTESIAN),
                                new Point(11.000, 12.000, Point.CARTESIAN),
                                new Point(68.000, 49.500, Point.CARTESIAN),
                                new Point(58.500, 23.000, Point.CARTESIAN)
                        )
                ),
                createPath( // push first sample 2
                        new BezierLine(
                                new Point(58.500, 23.000, Point.CARTESIAN),
                                new Point(30.000, 22.500, Point.CARTESIAN)
                        )
                ),
                createPath( // curve to second sample 3
                        new BezierCurve(
                                new Point(30.000, 22.500, Point.CARTESIAN),
                                new Point(62.500, 32.000, Point.CARTESIAN),
                                new Point(58.500, 12.500, Point.CARTESIAN)
                        )
                ),
                createPath( // push second sample 4
                        new BezierLine(
                                new Point(58.500, 12.500, Point.CARTESIAN),
                                new Point(30.000, 12.500, Point.CARTESIAN)
                        )
                ),
                createPath( // curve to third sample 5
                        new BezierCurve(
                                new Point(30.000, 12.500, Point.CARTESIAN),
                                new Point(62.000, 18.500, Point.CARTESIAN),
                                new Point(58.500, 9.000, Point.CARTESIAN)
                        )
                ),
                createPath( // push third sample 6
                        new BezierLine(
                                new Point(58.500, 9.000, Point.CARTESIAN),
                                new Point(30.000, 9.000, Point.CARTESIAN)
                        )
                ),
                createPath( // curve to plus 1 intake 7
                        new BezierCurve(
                                new Point(30.000, 9.000, Point.CARTESIAN),
                                new Point(27.000, 23.000, Point.CARTESIAN),
                                new Point( 25, 34.5, Point.CARTESIAN),
                                new Point(10.500, 33.000+3.5, Point.CARTESIAN)
                        )
                ),
                createPath( // plus 1 outtake 8
                        new BezierLine(
                                new Point(10.500, 33.000+2.5, Point.CARTESIAN),
                                new Point(42.000, 74.000, Point.CARTESIAN)
                        )
                ),
                createPath( // plus 1 outtake push 9
                        new BezierLine(
                                new Point(42.000, 74.000, Point.CARTESIAN),
                                new Point(42, 67, Point.CARTESIAN)
                        )
                ),
                createPath( // curve to plus 2 intake 10
                        new BezierCurve(
                                new Point(42.000, 74, Point.CARTESIAN),
                                new Point(24.655, 54.670, Point.CARTESIAN),
                                new Point(30.194, 26.084, Point.CARTESIAN),
                                new Point(10.500, 33.000+1.5, Point.CARTESIAN)
                        )
                ),
                createPath( // plus 2 outtake 11
                        new BezierLine(
                                new Point(10.500, 33.000, Point.CARTESIAN),
                                new Point(42.000, 72.500, Point.CARTESIAN)
                        )
                ),
                createPath( // plus 2 outtake push 12
                        new BezierLine(
                                new Point(42.000, 72.500, Point.CARTESIAN),
                                new Point(42, 67, Point.CARTESIAN)
                        )
                ),
                createPath( // curve to plus 3 intake 13
                        new BezierCurve(
                                new Point(40.000, 72.5, Point.CARTESIAN),
                                new Point(24.655, 54.849, Point.CARTESIAN),
                                new Point(30.372, 25.548, Point.CARTESIAN),
                                new Point(10.500, 33.000+2, Point.CARTESIAN)
                        )
                ),
                createPath( // plus 3 outtake 14
                        new BezierLine(
                                new Point(10.500, 33.000, Point.CARTESIAN),
                                new Point(40.000, 71.00, Point.CARTESIAN)
                        )
                ),
                createPath( // plus 3 outtake push 15
                        new BezierLine(
                                new Point(40.000, 71.000, Point.CARTESIAN),
                                new Point(40, 67, Point.CARTESIAN)
                        )
                ),
                createPath( // curve to plus 4 intake 16
                        new BezierCurve(
                                new Point(40.000, 71, Point.CARTESIAN),
                                new Point(24.655, 54.849, Point.CARTESIAN),
                                new Point(30.372, 25.548, Point.CARTESIAN),
                                new Point(10.500, 33.000+2, Point.CARTESIAN)
                        )
                ),
                createPath( // plus 4 outtake 17
                        new BezierLine(
                                new Point(10.500, 33.000-1, Point.CARTESIAN),
                                new Point(40.000, 69.500, Point.CARTESIAN)
                        )
                ),
//                createPath( // park
//                        new BezierLine(
//                                new Point(40.000, 74.000, Point.CARTESIAN),
//                                new Point(13.000, 20.000, Point.CARTESIAN)
//                        ), Math.toRadians(0), Math.toRadians(-45)
//                )
                createPath( // park 18
                        new BezierLine(
                                new Point(40.000, 74.000, Point.CARTESIAN),
                                new Point(32.000, 74.000, Point.CARTESIAN)
                        ), Math.toRadians(0), Math.toRadians(-45)
                )
        );
        parkWithSpecPush = createPath(
                new BezierLine(
                        new Point(40.000, 67.000, Point.CARTESIAN),
                        new Point(13.000, 20.000, Point.CARTESIAN)
                ), Math.toRadians(0), Math.toRadians(-45)
        );

        robotPush = new PathChain(fiveSpecs.get(1), fiveSpecs.get(2), fiveSpecs.get(3), fiveSpecs.get(4), fiveSpecs.get(5), fiveSpecs.get(6));

        fiveSpecs.get(1).setPathEndTimeoutConstraint(0);
        fiveSpecs.get(2).setPathEndTimeoutConstraint(0);
        fiveSpecs.get(3).setPathEndTimeoutConstraint(0);
        fiveSpecs.get(4).setPathEndTimeoutConstraint(0);
        fiveSpecs.get(5).setPathEndTimeoutConstraint(0);
        fiveSpecs.get(6).setPathEndTimeoutConstraint(0);

        fiveSpecs.get(1).setZeroPowerAccelerationMultiplier(7.5);
        fiveSpecs.get(2).setZeroPowerAccelerationMultiplier(7.5);
        fiveSpecs.get(3).setZeroPowerAccelerationMultiplier(7.5);
        fiveSpecs.get(4).setZeroPowerAccelerationMultiplier(7.5);
        fiveSpecs.get(5).setZeroPowerAccelerationMultiplier(7.5);
        fiveSpecs.get(6).setZeroPowerAccelerationMultiplier(7.5);
    }

    public static void build(Pose currentPose) {

    }

    public static Path pathTo(BezierLine line, Follower follower) {



        return createPath(
                new BezierLine(
                        new Point(follower.getPose().getX(), follower.getPose().getY()),
                        new Point(line.getLastControlPoint().getX(), line.getLastControlPoint().getY())
                )
        );
    }

    public static Path curveTo(BezierCurve curve, Follower follower) {
        ArrayList<Point> controlPoints = curve.getControlPoints();
        controlPoints.remove(0);

        // Always include the first point (follower position)
        List<Point> points = new ArrayList<>();
        points.add(new Point(follower.getPose().getX(), follower.getPose().getY()));

        // Add as many control points as available
        points.addAll(controlPoints);

        return createPath(new BezierCurve(points.toArray(new Point[0])));
    }


    private static Path createPath(BezierLine line, double heading) {
        Path path = new Path(line);
        path.setConstantHeadingInterpolation(heading); // Constant heading
        return path;
    }

    private static Path createPath(BezierLine line, double startHeading, double endHeading) {
        Path path = new Path(line);
        path.setLinearHeadingInterpolation(startHeading, endHeading); // Linear heading
        return path;
    }

    private static Path createPath(BezierLine line, boolean tangent) {
        Path path = new Path(line);
        if (tangent) path.setTangentHeadingInterpolation();
        return path;
    }

    private static Path createPath(BezierLine line) {
        Path path = new Path(line);
        path.setConstantHeadingInterpolation(0);
        return path;
    }

    private static Path createPath(BezierCurve curve) {
        Path path = new Path(curve);
        path.setConstantHeadingInterpolation(0);
        return path;
    }

    // Constant Heading
    private static Path createPath(BezierCurve curve, double heading) {
        Path path = new Path(curve);
        path.setConstantHeadingInterpolation(heading); // Constant heading
        return path;
    }

    // Linear Heading
    private static Path createPath(BezierCurve curve, double startHeading, double endHeading) {
        Path path = new Path(curve);
        path.setLinearHeadingInterpolation(startHeading, endHeading); // Linear heading
        return path;
    }

    // Tangent or Default Constant Heading
    private static Path createPath(BezierCurve curve, boolean tangent) {
        Path path = new Path(curve);
        if (tangent) path.setTangentHeadingInterpolation(); // Tangent heading
        else path.setConstantHeadingInterpolation(0);      // Default constant heading
        return path;
    }
}
