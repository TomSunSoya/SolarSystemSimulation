import core.Planet;
import core.SolarSystem;
import core.SimulationController;
import core.TimeController;
import ui.MainFrame;
import utils.LocalizationManager;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LocalizationManager localizationManager = new LocalizationManager(Locale.ENGLISH);
            SolarSystem solarSystem = buildSolarSystem();
            TimeController timeController = new TimeController();
            SimulationController simulationController = new SimulationController(solarSystem, timeController);

            MainFrame frame = new MainFrame(solarSystem, simulationController, localizationManager);
            frame.setVisible(true);
        });
    }

    private static SolarSystem buildSolarSystem() {
        SolarSystem solarSystem = new SolarSystem();

        solarSystem.addPlanet(new Planet("Mercury", 55, 1.6, 5, new Color(169, 169, 169),
                2439.7, 3.301e23, 88, 167, 57.9));
        solarSystem.addPlanet(new Planet("Venus", 85, 1.2, 7, new Color(218, 165, 32),
                6051.8, 4.867e24, 224.7, 464, 108.2));
        solarSystem.addPlanet(new Planet("Earth", 120, 1.0, 8, new Color(70, 130, 180),
                6371, 5.972e24, 365.2, 15, 149.6));
        solarSystem.addPlanet(new Planet("Mars", 155, 0.8, 6, new Color(188, 39, 50),
                3389.5, 6.417e23, 687, -65, 227.9));
        solarSystem.addPlanet(new Planet("Jupiter", 210, 0.45, 14, new Color(205, 133, 63),
                69911, 1.898e27, 4331, -110, 778.5));

        return solarSystem;
    }
}
