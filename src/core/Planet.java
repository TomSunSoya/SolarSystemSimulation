package core;

import utils.LocalizationManager;

import java.awt.*;
import java.awt.geom.Point2D;

public class Planet {
    public static final double AU_IN_MILLION_KILOMETERS = 149.5978707;

    private static final double FULL_ROTATION = Math.PI * 2;
    private static final double SOLAR_GRAVITATIONAL_PARAMETER = 1.32712440018e11;

    private final String id;
    private final Orbit orbit;
    private final Color primaryColor;
    private final Color secondaryColor;
    private final double physicalRadius;
    private final double mass;
    private final double orbitalPeriod;
    private final double surfaceTemperature;

    private double meanAnomaly;

    private Planet(Builder builder) {
        this.id = builder.id;
        this.orbit = builder.orbit;
        this.primaryColor = builder.primaryColor;
        this.secondaryColor = builder.secondaryColor;
        this.physicalRadius = builder.physicalRadius;
        this.mass = builder.mass;
        this.orbitalPeriod = builder.orbitalPeriod;
        this.surfaceTemperature = builder.surfaceTemperature;
        this.meanAnomaly = Math.toRadians(builder.initialMeanAnomalyDegrees);
    }

    public void updatePosition(double elapsedSimulationDays) {
        meanAnomaly = normalizeAngle(meanAnomaly + FULL_ROTATION * elapsedSimulationDays / orbitalPeriod);
    }

    public Point2D.Double getCurrentPosition() {
        return orbit.getPosition(meanAnomaly);
    }

    public double getCurrentDistanceFromSun() {
        return orbit.getRadius(meanAnomaly) * AU_IN_MILLION_KILOMETERS;
    }

    public double getCurrentDistanceFromSunAu() {
        return orbit.getRadius(meanAnomaly);
    }

    public double getCurrentOrbitalAngleDegrees() {
        return Math.toDegrees(orbit.getTrueAnomaly(meanAnomaly));
    }

    public double getCurrentSpeedKilometersPerSecond() {
        double semiMajorAxisKilometers = orbit.getSemiMajorAxisAu() * AU_IN_MILLION_KILOMETERS * 1_000_000;
        double currentDistanceKilometers = getCurrentDistanceFromSun() * 1_000_000;
        return Math.sqrt(SOLAR_GRAVITATIONAL_PARAMETER *
                (2.0 / currentDistanceKilometers - 1.0 / semiMajorAxisKilometers));
    }

    public String getLocalizedName(LocalizationManager localizationManager) {
        return localizationManager.getString("planet." + id);
    }

    public String getId() {
        return id;
    }

    public Orbit getOrbit() {
        return orbit;
    }

    public Color getPrimaryColor() {
        return primaryColor;
    }

    public Color getSecondaryColor() {
        return secondaryColor;
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

    public double getSemiMajorAxis() {
        return orbit.getSemiMajorAxisAu() * AU_IN_MILLION_KILOMETERS;
    }

    public double getEccentricity() {
        return orbit.getEccentricity();
    }

    private double normalizeAngle(double angle) {
        double normalized = angle % FULL_ROTATION;
        return normalized < 0 ? normalized + FULL_ROTATION : normalized;
    }

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public static class Builder {
        private final String id;
        private Orbit orbit;
        private Color primaryColor = Color.WHITE;
        private Color secondaryColor = Color.LIGHT_GRAY;
        private double physicalRadius = Double.NaN;
        private double mass = Double.NaN;
        private double orbitalPeriod = Double.NaN;
        private double surfaceTemperature = Double.NaN;
        private double initialMeanAnomalyDegrees;

        private Builder(String id) {
            this.id = id;
        }

        public Builder orbit(Orbit orbit) {
            this.orbit = orbit;
            return this;
        }

        public Builder colors(Color primaryColor, Color secondaryColor) {
            this.primaryColor = primaryColor;
            this.secondaryColor = secondaryColor;
            return this;
        }

        public Builder physicalRadius(double physicalRadius) {
            this.physicalRadius = physicalRadius;
            return this;
        }

        public Builder mass(double mass) {
            this.mass = mass;
            return this;
        }

        public Builder orbitalPeriod(double orbitalPeriod) {
            this.orbitalPeriod = orbitalPeriod;
            return this;
        }

        public Builder surfaceTemperature(double surfaceTemperature) {
            this.surfaceTemperature = surfaceTemperature;
            return this;
        }

        public Builder initialMeanAnomalyDegrees(double initialMeanAnomalyDegrees) {
            this.initialMeanAnomalyDegrees = initialMeanAnomalyDegrees;
            return this;
        }

        public Planet build() {
            if (orbit == null) {
                throw new IllegalStateException("Planet orbit is required");
            }
            return new Planet(this);
        }
    }
}
