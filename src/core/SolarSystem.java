package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SolarSystem {
    private final Sun sun;
    private final List<Planet> planets;

    public SolarSystem() {
        this.sun = new Sun();
        this.planets = new ArrayList<>();
    }

    public void addPlanet(Planet planet) {
        planets.add(planet);
    }

    public void update(double elapsedTime) {
        for (Planet planet : planets) {
            planet.updatePosition(elapsedTime);
        }
    }

    public List<Planet> getPlanets() {
        return Collections.unmodifiableList(planets);
    }

    public Sun getSun() {
        return sun;
    }

    public double getMaxOrbitDistanceAu() {
        double maxDistance = 1;
        for (Planet planet : planets) {
            maxDistance = Math.max(maxDistance, planet.getOrbit().getAphelionDistanceAu());
        }
        return maxDistance;
    }
}
