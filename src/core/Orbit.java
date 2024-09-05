package core;

import utils.Pair;

public class Orbit {
    private double semiMajorAxis;
    private double semiMinorAxis;

    public Orbit(double semiMajorAxis, double semiMinorAxis) {
        this.semiMajorAxis = semiMajorAxis;
        this.semiMinorAxis = semiMinorAxis;
    }

    public Pair<Integer, Integer> getPosition(double angle, int centerX, int centerY) {
        double x = centerX + semiMajorAxis * Math.cos(angle);
        double y = centerY + semiMinorAxis * Math.sin(angle);
        return new Pair<>((int) x, (int) y);
    }
}
