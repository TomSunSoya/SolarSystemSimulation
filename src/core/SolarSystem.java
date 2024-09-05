package core;

import java.util.ArrayList;
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
        for (Planet planet : planets)
            planet.updatePosition(elapsedTime);
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public Sun getSun() {
        return sun;
    }
}
