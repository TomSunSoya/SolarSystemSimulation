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
        infoArea.setBackground(new Color(247, 244, 238));
        infoArea.setText(localizationManager.getString("select_planet_prompt"));

        add(new JScrollPane(infoArea), BorderLayout.CENTER);
    }

    public void showPlanet(Planet planet) {
        currentPlanet = planet;
        refreshCurrentPlanet();
    }

    public void refreshCurrentPlanet() {
        if (currentPlanet == null) {
            infoArea.setText(localizationManager.getString("select_planet_prompt"));
            return;
        }

        PlanetInfo planetInfo = new PlanetInfo(currentPlanet, localizationManager);
        infoArea.setText(planetInfo.getFormattedInfo());
        infoArea.setCaretPosition(0);
    }

    public void refreshTexts() {
        setBorder(BorderFactory.createTitledBorder(localizationManager.getString("planet_info")));
        refreshCurrentPlanet();
    }
}
