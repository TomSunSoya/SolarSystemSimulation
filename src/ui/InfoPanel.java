package ui;

import core.Planet;
import core.PlanetInfo;
import utils.LocalizationManager;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    private final LocalizationManager localizationManager;
    private final JTextArea infoArea;
    private Planet currentPlanet;

    public InfoPanel(LocalizationManager localizationManager) {
        this.localizationManager = localizationManager;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(localizationManager.getString("planet_info")));

        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        infoArea.setText(localizationManager.getString("select_planet_prompt"));

        add(new JScrollPane(infoArea), BorderLayout.CENTER);
    }

    public void showPlanet(Planet planet) {
        currentPlanet = planet;
        PlanetInfo planetInfo = new PlanetInfo(
                planet.getDistanceFromSun(),
                planet.getName(),
                planet.getPhysicalRadius(),
                planet.getMass(),
                planet.getOrbitalPeriod(),
                planet.getSurfaceTemperature(),
                localizationManager
        );
        infoArea.setText(planetInfo.getFormattedInfo());
    }

    public void refreshTexts() {
        setBorder(BorderFactory.createTitledBorder(localizationManager.getString("planet_info")));
        if (currentPlanet == null) {
            infoArea.setText(localizationManager.getString("select_planet_prompt"));
        } else {
            showPlanet(currentPlanet);
        }
    }
}
