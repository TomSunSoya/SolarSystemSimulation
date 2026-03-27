package core;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Orbit {
    private static final double FULL_ROTATION = Math.PI * 2;

    private final double semiMajorAxisAu;
    private final double eccentricity;
    private final double argumentOfPeriapsisRad;

    public Orbit(double semiMajorAxisAu, double eccentricity, double argumentOfPeriapsisDegrees) {
        this.semiMajorAxisAu = semiMajorAxisAu;
        this.eccentricity = eccentricity;
        this.argumentOfPeriapsisRad = Math.toRadians(argumentOfPeriapsisDegrees);
    }

    public Point2D.Double getPosition(double meanAnomaly) {
        double eccentricAnomaly = solveEccentricAnomaly(meanAnomaly);
        double x = semiMajorAxisAu * (Math.cos(eccentricAnomaly) - eccentricity);
        double y = semiMajorAxisAu * Math.sqrt(1 - eccentricity * eccentricity) * Math.sin(eccentricAnomaly);

        double cos = Math.cos(argumentOfPeriapsisRad);
        double sin = Math.sin(argumentOfPeriapsisRad);
        return new Point2D.Double(
                x * cos - y * sin,
                x * sin + y * cos
        );
    }

    public double getRadius(double meanAnomaly) {
        double eccentricAnomaly = solveEccentricAnomaly(meanAnomaly);
        return semiMajorAxisAu * (1 - eccentricity * Math.cos(eccentricAnomaly));
    }

    public double getTrueAnomaly(double meanAnomaly) {
        double eccentricAnomaly = solveEccentricAnomaly(meanAnomaly);
        double sin = Math.sqrt(1 - eccentricity * eccentricity) * Math.sin(eccentricAnomaly);
        double cos = Math.cos(eccentricAnomaly) - eccentricity;
        return normalizeAngle(Math.atan2(sin, cos) + argumentOfPeriapsisRad);
    }

    public List<Point2D.Double> samplePath(int samples) {
        List<Point2D.Double> points = new ArrayList<>(samples);
        for (int i = 0; i < samples; i++) {
            double meanAnomaly = FULL_ROTATION * i / samples;
            points.add(getPosition(meanAnomaly));
        }
        return points;
    }

    public double getSemiMajorAxisAu() {
        return semiMajorAxisAu;
    }

    public double getEccentricity() {
        return eccentricity;
    }

    public double getAphelionDistanceAu() {
        return semiMajorAxisAu * (1 + eccentricity);
    }

    private double solveEccentricAnomaly(double meanAnomaly) {
        double normalizedMeanAnomaly = normalizeAngle(meanAnomaly);
        double eccentricAnomaly = eccentricity < 0.8 ? normalizedMeanAnomaly : Math.PI;

        for (int i = 0; i < 12; i++) {
            double f = eccentricAnomaly - eccentricity * Math.sin(eccentricAnomaly) - normalizedMeanAnomaly;
            double fPrime = 1 - eccentricity * Math.cos(eccentricAnomaly);
            eccentricAnomaly -= f / fPrime;
        }
        return eccentricAnomaly;
    }

    private double normalizeAngle(double angle) {
        double normalized = angle % FULL_ROTATION;
        return normalized < 0 ? normalized + FULL_ROTATION : normalized;
    }
}
