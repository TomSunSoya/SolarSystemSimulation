import core.PlanetCatalog;
import core.SimulationController;
import core.SolarSystem;
import core.TimeController;
import ui.MainFrame;
import utils.LocalizationManager;

import javax.swing.*;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LocalizationManager localizationManager = new LocalizationManager(Locale.ENGLISH);
            SolarSystem solarSystem = PlanetCatalog.createSolarSystem();
            TimeController timeController = new TimeController();
            SimulationController simulationController = new SimulationController(solarSystem, timeController);

            MainFrame frame = new MainFrame(solarSystem, simulationController, localizationManager);
            frame.setVisible(true);
        });
    }
}
