package ui;

import core.Planet;
import core.SimulationController;
import core.SolarSystem;
import utils.LocalizationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
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
        setLayout(new BorderLayout(12, 12));

        infoPanel = new InfoPanel(localizationManager);
        solarSystemPanel = new SolarSystemPanel(solarSystem, localizationManager, this::onPlanetSelected);
        timeControlPanel = new TimeControlPanel(
                simulationController,
                localizationManager,
                this::onLocaleChanged,
                solarSystemPanel::resetView
        );

        simulationController.addTickListener(solarSystemPanel::onSimulationTick);
        simulationController.addTickListener(infoPanel::refreshCurrentPlanet);
        simulationController.addTickListener(timeControlPanel::refreshDynamicText);

        add(solarSystemPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout(0, 12));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 12));
        rightPanel.add(infoPanel, BorderLayout.CENTER);
        rightPanel.add(timeControlPanel, BorderLayout.SOUTH);
        rightPanel.setPreferredSize(new Dimension(340, 760));
        add(rightPanel, BorderLayout.EAST);

        getRootPane().setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 0));
        setPreferredSize(new Dimension(1400, 860));
        setMinimumSize(new Dimension(1080, 720));
        pack();
        setLocationRelativeTo(null);

        installShortcuts();
        selectDefaultPlanet(solarSystem);
    }

    private void selectDefaultPlanet(SolarSystem solarSystem) {
        for (Planet planet : solarSystem.getPlanets()) {
            if ("earth".equals(planet.getId())) {
                onPlanetSelected(planet);
                return;
            }
        }
    }

    private void installShortcuts() {
        JRootPane rootPane = getRootPane();
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = rootPane.getActionMap();

        bindShortcut(inputMap, actionMap, "toggleSimulation",
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), timeControlPanel::toggleSimulation);
        bindShortcut(inputMap, actionMap, "speedUp",
                KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, InputEvent.SHIFT_DOWN_MASK),
                () -> timeControlPanel.changeSpeedByFactor(1.25));
        bindShortcut(inputMap, actionMap, "speedDown",
                KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0),
                () -> timeControlPanel.changeSpeedByFactor(0.8));
        bindShortcut(inputMap, actionMap, "panLeft",
                KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), () -> solarSystemPanel.panBy(-28, 0));
        bindShortcut(inputMap, actionMap, "panRight",
                KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), () -> solarSystemPanel.panBy(28, 0));
        bindShortcut(inputMap, actionMap, "panUp",
                KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), () -> solarSystemPanel.panBy(0, -28));
        bindShortcut(inputMap, actionMap, "panDown",
                KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), () -> solarSystemPanel.panBy(0, 28));
        bindShortcut(inputMap, actionMap, "resetView",
                KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), solarSystemPanel::resetView);
    }

    private void bindShortcut(InputMap inputMap,
                              ActionMap actionMap,
                              String actionKey,
                              KeyStroke keyStroke,
                              Runnable action) {
        inputMap.put(keyStroke, actionKey);
        actionMap.put(actionKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    private void onPlanetSelected(Planet planet) {
        solarSystemPanel.setSelectedPlanet(planet);
        infoPanel.showPlanet(planet);
    }

    private void onLocaleChanged(Locale locale) {
        localizationManager.setLocale(locale);
        infoPanel.refreshTexts();
        timeControlPanel.refreshTexts();
        solarSystemPanel.repaint();
    }
}
