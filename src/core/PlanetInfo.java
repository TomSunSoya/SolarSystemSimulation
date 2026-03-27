package core;

import utils.LocalizationManager;

public class PlanetInfo {
    private final String name;
    private final double radius;
    private final double mass;
    private final double orbitalPeriod;
    private final double surfaceTemperature;
    private final double distanceFromSun;

    private final LocalizationManager localizationManager;

    public PlanetInfo(double distanceFromSun, String name, double radius, double mass, double orbitalPeriod,
                      double surfaceTemperature, LocalizationManager localizationManager) {
        this.distanceFromSun = distanceFromSun;
        this.name = name;
        this.radius = radius;
        this.mass = mass;
        this.orbitalPeriod = orbitalPeriod;
        this.surfaceTemperature = surfaceTemperature;
        this.localizationManager = localizationManager;
    }

    // 返回格式化的行星信息，使用资源文件中的本地化文本
    public String getFormattedInfo() {
        return localizationManager.getString("planet_name") + ": " + name + "\n" +
                formatMeasurement("radius", radius, "unit_kilometers") +
                formatMeasurement("mass", mass, "unit_kilograms") +
                formatMeasurement("orbital_period", orbitalPeriod, "unit_days") +
                formatMeasurement("surface_temperature", surfaceTemperature, "unit_celsius") +
                formatMeasurement("distance_from_sun", distanceFromSun, "unit_million_kilometers");
    }

    private String formatMeasurement(String labelKey, double value, String unitKey) {
        if (!Double.isFinite(value)) {
            return localizationManager.getString(labelKey) + ": " +
                    localizationManager.getString("unknown") + "\n";
        }

        return localizationManager.getString(labelKey) + ": " + value + " " +
                localizationManager.getString(unitKey) + "\n";
    }
}
