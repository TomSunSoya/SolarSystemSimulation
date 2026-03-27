package core;

import utils.LocalizationManager;

import java.text.NumberFormat;

public class PlanetInfo {
    private final Planet planet;
    private final LocalizationManager localizationManager;

    public PlanetInfo(Planet planet, LocalizationManager localizationManager) {
        this.planet = planet;
        this.localizationManager = localizationManager;
    }

    public String getFormattedInfo() {
        return localizationManager.getString("planet_name") + ": " + planet.getLocalizedName(localizationManager) + "\n" +
                formatMeasurement("radius", planet.getPhysicalRadius(), "unit_kilometers", 1) +
                formatScientific("mass", planet.getMass()) +
                formatMeasurement("orbital_period", planet.getOrbitalPeriod(), "unit_days", 1) +
                formatMeasurement("surface_temperature", planet.getSurfaceTemperature(), "unit_celsius", 0) +
                formatMeasurement("semi_major_axis", planet.getSemiMajorAxis(), "unit_million_kilometers", 1) +
                formatMeasurement("eccentricity", planet.getEccentricity(), null, 3) +
                formatMeasurement("current_distance", planet.getCurrentDistanceFromSun(), "unit_million_kilometers", 2) +
                formatMeasurement("current_speed", planet.getCurrentSpeedKilometersPerSecond(), "unit_km_per_second", 2) +
                formatMeasurement("current_angle", planet.getCurrentOrbitalAngleDegrees(), "unit_degrees", 1);
    }

    private String formatScientific(String labelKey, double value) {
        if (!Double.isFinite(value)) {
            return localizationManager.getString(labelKey) + ": " +
                    localizationManager.getString("unknown") + "\n";
        }

        return localizationManager.getString(labelKey) + ": " + String.format(localizationManager.getLocale(), "%.3e", value) + " " +
                localizationManager.getString("unit_kilograms") + "\n";
    }

    private String formatMeasurement(String labelKey, double value, String unitKey, int decimals) {
        if (!Double.isFinite(value)) {
            return localizationManager.getString(labelKey) + ": " +
                    localizationManager.getString("unknown") + "\n";
        }

        NumberFormat numberFormat = NumberFormat.getNumberInstance(localizationManager.getLocale());
        numberFormat.setMaximumFractionDigits(decimals);
        numberFormat.setMinimumFractionDigits(decimals == 0 ? 0 : Math.min(1, decimals));

        String line = localizationManager.getString(labelKey) + ": " + numberFormat.format(value);
        if (unitKey != null) {
            line += " " + localizationManager.getString(unitKey);
        }
        return line + "\n";
    }
}
