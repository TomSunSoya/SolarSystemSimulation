package core;

import java.awt.*;

public class Planet {
    private static final double FULL_ROTATION = Math.PI * 2;

    private String name;
    private double orbitRadius;
    private double orbitSpeed;
    private double angle;
    private double radius;
    private Color color;

    private final double physicalRadius;
    private final double mass;
    private final double orbitalPeriod;
    private final double surfaceTemperature;
    private final double distanceFromSun;

    public Planet(String name, double orbitRadius, double orbitSpeed, double radius, Color color) {
        this(name, orbitRadius, orbitSpeed, radius, color,
                Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN);
    }

    public Planet(String name, double orbitRadius, double orbitSpeed, double radius, Color color,
                  double physicalRadius, double mass, double orbitalPeriod,
                  double surfaceTemperature, double distanceFromSun) {
        this.name = name;
        this.orbitRadius = orbitRadius;
        this.orbitSpeed = orbitSpeed;
        this.radius = radius;
        this.color = color;
        this.angle = 0;
        this.physicalRadius = physicalRadius;
        this.mass = mass;
        this.orbitalPeriod = orbitalPeriod;
        this.surfaceTemperature = surfaceTemperature;
        this.distanceFromSun = distanceFromSun;
    }

    public void updatePosition(double elapsedTime) {
        angle += orbitSpeed * elapsedTime;
        angle %= FULL_ROTATION;
        if (angle < 0) {
            angle += FULL_ROTATION;
        }
    }

    public void render(Graphics2D g2d, int x, int y) {
        int planetX = (int) (x + orbitRadius * Math.cos(angle));
        int planetY = (int) (y + orbitRadius * Math.sin(angle));
        g2d.setColor(color);
        g2d.fillOval(planetX - (int) radius, planetY - (int) radius, (int) radius * 2, (int) radius * 2);
    }

    public int getCurrentX(int centerX) {
        return (int) (centerX + orbitRadius * Math.cos(angle));
    }

    public int getCurrentY(int centerY) {
        return (int) (centerY + orbitRadius * Math.sin(angle));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getOrbitRadius() {
        return orbitRadius;
    }

    public double getRadius() {
        return radius;
    }

    public double getPhysicalRadius() {
        return physicalRadius;
    }

    public double getMass() {
        return mass;
    }

    public double getOrbitalPeriod() {
        return orbitalPeriod;
    }

    public double getSurfaceTemperature() {
        return surfaceTemperature;
    }

    public double getDistanceFromSun() {
        return distanceFromSun;
    }
}
