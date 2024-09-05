package core;

import utils.LocalizationManager;

import java.util.List;

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
                localizationManager.getString("radius") + ": " + radius + " " +
                localizationManager.getString("unit_kilometers") + "\n" +
                localizationManager.getString("mass") + ": " + mass + " " +
                localizationManager.getString("unit_kilograms") + "\n" +
                localizationManager.getString("orbital_period") + ": " + orbitalPeriod + " " +
                localizationManager.getString("unit_days") + "\n" +
                localizationManager.getString("surface_temperature") + ": " + surfaceTemperature + " " +
                localizationManager.getString("unit_celsius") + "\n" +
                localizationManager.getString("distance_from_sun") + ": " + distanceFromSun + " " +
                localizationManager.getString("unit_million_kilometers") + "\n";
    }
}
