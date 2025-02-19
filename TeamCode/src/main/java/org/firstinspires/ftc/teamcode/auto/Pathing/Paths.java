package org.firstinspires.ftc.teamcode.auto.Pathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Paths {

    public static ArrayList<Path> plusFourSpec = new ArrayList<>();
    public static Path parkWithSpecPush;

    public static double intakeOffset = -2;
    public static double intakeOffset1 = 2;
    public static double intakeOffset2 = 1;
    public static double intakeOffset3 = 1;
    public static double intakeOffset4 = 2;


    public static void init() {
        Collections.addAll(plusFourSpec,
                createPath( //preload
                        new BezierLine(
                                new Point(9.000, 65.000, Point.CARTESIAN),
                                new Point(40.00, 67.000, Point.CARTESIAN)
                        )
                ),//1
                createPath( // curve to first sample
                        new BezierCurve(
                                new Point(40.000, 67.000, Point.CARTESIAN),
                                new Point(31.500, 66.000, Point.CARTESIAN),
                                new Point(11.000, 12.000, Point.CARTESIAN),
                                new Point(68.000, 49.500, Point.CARTESIAN),
                                new Point(58.500, 23.000, Point.CARTESIAN)
                        )
                ),//2
                createPath( // push first sample
                        new BezierLine(
                                new Point(58.500, 23.000, Point.CARTESIAN),
                                new Point(20.000, 22.500, Point.CARTESIAN)
                        )
                ),//3
                createPath( // curve to second sample
                        new BezierCurve(
                                new Point(20.000, 22.500, Point.CARTESIAN),
                                new Point(62.500, 32.000, Point.CARTESIAN),
                                new Point(58.500, 12.500, Point.CARTESIAN)
                        )
                ),//4
                createPath( // push second sample
                        new BezierLine(
                                new Point(58.500, 12.500, Point.CARTESIAN),
                                new Point(20.000, 12.500, Point.CARTESIAN)
                        )
                ),//5
                createPath( // curve to third sample
                        new BezierCurve(
                                new Point(20.000, 12.500, Point.CARTESIAN),
                                new Point(62.000, 18.500, Point.CARTESIAN),
                                new Point(58.500, 9.000, Point.CARTESIAN)
                        )
                ),//6
                createPath( // push third sample
                        new BezierLine(
                                new Point(58.500, 9.000, Point.CARTESIAN),
                                new Point(20.000, 9.000, Point.CARTESIAN)
                        )
                ),//7
                createPath( // curve to plus 1 intake
                        new BezierCurve(
                                new Point(20.000, 9.000, Point.CARTESIAN),
                                new Point(27.000, 23.000, Point.CARTESIAN),
                                new Point( 25, 34.5, Point.CARTESIAN),
                                new Point(10.500 + intakeOffset + intakeOffset1, 33.000+3.5, Point.CARTESIAN)
                        )
                ),//8
                createPath( // plus 1 outtake
                        new BezierLine(
                                new Point(10.500, 33.000+2.5, Point.CARTESIAN),
                                new Point(40.000, 74.000, Point.CARTESIAN)
                        )
                ),//9
                createPath(
                        new BezierLine(
                                new Point(40.000, 74.000, Point.CARTESIAN),
                                new Point(40, 67, Point.CARTESIAN)
                        )
                ),//10
                createPath( // curve to plus 2 intake
                        new BezierCurve(
                                new Point(40.000, 67.000, Point.CARTESIAN),
                                new Point(24.655, 54.670, Point.CARTESIAN),
                                new Point(30.194, 26.084, Point.CARTESIAN),
                                new Point(10.500 + intakeOffset + intakeOffset2, 33.000+1.5, Point.CARTESIAN)
                        )
                ),//11
                createPath( // plus 2 outtake
                        new BezierLine(
                                new Point(10.500, 33.000, Point.CARTESIAN),
                                new Point(40.000, 74.000, Point.CARTESIAN)
                        )
                ),//12
                createPath(
                        new BezierLine(
                                new Point(40.000, 74.000, Point.CARTESIAN),
                                new Point(40, 67, Point.CARTESIAN)
                        )
                ),//13
                createPath( // curve to plus 3 intake
                        new BezierCurve(
                                new Point(40.000, 67.000, Point.CARTESIAN),
                                new Point(24.655, 54.849, Point.CARTESIAN),
                                new Point(30.372, 25.548, Point.CARTESIAN),
                                new Point(10.500 + intakeOffset + intakeOffset3, 33.000+2, Point.CARTESIAN)
                        )
                ),//14
                createPath( // plus 3 outtake
                        new BezierLine(
                                new Point(10.500, 33.000, Point.CARTESIAN),
                                new Point(40.000, 74.000, Point.CARTESIAN)
                        )
                ),//15
                createPath(
                        new BezierLine(
                                new Point(40.000, 74.000, Point.CARTESIAN),
                                new Point(40, 67, Point.CARTESIAN)
                        )
                ),//16
                createPath( // curve to plus 4 intake
                        new BezierCurve(
                                new Point(40.000, 67.000, Point.CARTESIAN),
                                new Point(24.655, 54.849, Point.CARTESIAN),
                                new Point(30.372, 25.548, Point.CARTESIAN),
                                new Point(10.500 + intakeOffset + intakeOffset4, 33.000+2, Point.CARTESIAN)
                        )
                ),//17
                createPath( // plus 4 outtake
                        new BezierLine(
                                new Point(10.500, 33.000-1, Point.CARTESIAN),
                                new Point(40.000, 74.000, Point.CARTESIAN)
                        )
                ),//18
//                createPath( // park
//                        new BezierLine(
//                                new Point(40.000, 74.000, Point.CARTESIAN),
//                                new Point(13.000, 20.000, Point.CARTESIAN)
//                        ), Math.toRadians(0), Math.toRadians(-45)
//                )//19
                createPath( // park
                        new BezierLine(
                                new Point(40.000, 74.000, Point.CARTESIAN),
                                new Point(32.000, 74.000, Point.CARTESIAN)
                        ), Math.toRadians(0), Math.toRadians(-45)
                )//19
        );
        parkWithSpecPush = createPath(
                new BezierLine(
                        new Point(40.000, 67.000, Point.CARTESIAN),
                        new Point(13.000, 20.000, Point.CARTESIAN)
                ), Math.toRadians(0), Math.toRadians(-45)
        );

    }

    public static Path pathTo(BezierLine line, Follower follower) {
        return createPath(
                new BezierLine(
                        new Point(follower.getPose().getX(), follower.getPose().getY()),
                        new Point(line.getLastControlPoint().getX(), line.getLastControlPoint().getY())
                )
        );
    }

    public static Path pathTo(BezierCurve curve, Follower follower) {
        ArrayList<Point> controlPoints = curve.getControlPoints();

        // Always include the first point (follower position)
        List<Point> points = new ArrayList<>();
        points.add(new Point(follower.getPose().getX(), follower.getPose().getY()));

        // Add as many control points as available
        points.addAll(controlPoints);

        // Add the last control point
        points.add(curve.getLastControlPoint());

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
