package core;

import java.awt.*;

public final class PlanetCatalog {
    private PlanetCatalog() {
    }

    public static SolarSystem createSolarSystem() {
        SolarSystem solarSystem = new SolarSystem();

        solarSystem.addPlanet(Planet.builder("mercury")
                .orbit(new Orbit(0.387, 0.2056, 29))
                .colors(new Color(181, 178, 174), new Color(109, 102, 95))
                .physicalRadius(2439.7)
                .mass(3.301e23)
                .orbitalPeriod(87.97)
                .surfaceTemperature(167)
                .initialMeanAnomalyDegrees(210)
                .build());

        solarSystem.addPlanet(Planet.builder("venus")
                .orbit(new Orbit(0.723, 0.0068, 54))
                .colors(new Color(222, 179, 94), new Color(166, 108, 54))
                .physicalRadius(6051.8)
                .mass(4.867e24)
                .orbitalPeriod(224.7)
                .surfaceTemperature(464)
                .initialMeanAnomalyDegrees(35)
                .build());

        solarSystem.addPlanet(Planet.builder("earth")
                .orbit(new Orbit(1.0, 0.0167, 110))
                .colors(new Color(69, 157, 230), new Color(44, 110, 66))
                .physicalRadius(6371)
                .mass(5.972e24)
                .orbitalPeriod(365.25)
                .surfaceTemperature(15)
                .initialMeanAnomalyDegrees(260)
                .build());

        solarSystem.addPlanet(Planet.builder("mars")
                .orbit(new Orbit(1.524, 0.0934, 150))
                .colors(new Color(224, 109, 73), new Color(131, 54, 38))
                .physicalRadius(3389.5)
                .mass(6.417e23)
                .orbitalPeriod(686.98)
                .surfaceTemperature(-65)
                .initialMeanAnomalyDegrees(120)
                .build());

        solarSystem.addPlanet(Planet.builder("jupiter")
                .orbit(new Orbit(5.203, 0.0489, 195))
                .colors(new Color(224, 191, 150), new Color(150, 104, 67))
                .physicalRadius(69911)
                .mass(1.898e27)
                .orbitalPeriod(4332.59)
                .surfaceTemperature(-110)
                .initialMeanAnomalyDegrees(300)
                .build());

        solarSystem.addPlanet(Planet.builder("saturn")
                .orbit(new Orbit(9.537, 0.0565, 235))
                .colors(new Color(223, 206, 134), new Color(151, 121, 76))
                .physicalRadius(58232)
                .mass(5.683e26)
                .orbitalPeriod(10759.22)
                .surfaceTemperature(-140)
                .initialMeanAnomalyDegrees(25)
                .build());

        solarSystem.addPlanet(Planet.builder("uranus")
                .orbit(new Orbit(19.191, 0.0463, 280))
                .colors(new Color(157, 222, 238), new Color(95, 149, 185))
                .physicalRadius(25362)
                .mass(8.681e25)
                .orbitalPeriod(30688.5)
                .surfaceTemperature(-195)
                .initialMeanAnomalyDegrees(150)
                .build());

        solarSystem.addPlanet(Planet.builder("neptune")
                .orbit(new Orbit(30.069, 0.0095, 328))
                .colors(new Color(85, 128, 242), new Color(48, 73, 166))
                .physicalRadius(24622)
                .mass(1.024e26)
                .orbitalPeriod(60182)
                .surfaceTemperature(-200)
                .initialMeanAnomalyDegrees(75)
                .build());

        return solarSystem;
    }
}
