package ui;

import core.Planet;
import core.SimulationController;
import core.SolarSystem;
import utils.LocalizationManager;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class MainFrame extends JFrame {
    private final LocalizationManager localizationManager;
    private final InfoPanel infoPanel;
    private final TimeControlPanel timeControlPanel;
    private final SolarSystemPanel solarSystemPanel;

    public MainFrame(SolarSystem solarSystem, SimulationController simulationController,
                     LocalizationManager localizationManager) {
        this.localizationManager = localizationManager;

        setTitle("Solar System Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        infoPanel = new InfoPanel(localizationManager);
        solarSystemPanel = new SolarSystemPanel(solarSystem, this::onPlanetSelected);
        timeControlPanel = new TimeControlPanel(simulationController, localizationManager, this::onLocaleChanged);

        add(solarSystemPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(infoPanel, BorderLayout.CENTER);
        rightPanel.add(timeControlPanel, BorderLayout.SOUTH);
        rightPanel.setPreferredSize(new Dimension(320, 600));
        add(rightPanel, BorderLayout.EAST);

        setSize(1100, 700);
        setLocationRelativeTo(null);
    }

    private void onPlanetSelected(Planet planet) {
        infoPanel.showPlanet(planet);
    }

    private void onLocaleChanged(Locale locale) {
        localizationManager.setLocale(locale);
        infoPanel.refreshTexts();
        timeControlPanel.refreshTexts();
    }
}
