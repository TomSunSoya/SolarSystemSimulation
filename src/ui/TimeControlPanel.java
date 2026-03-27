package ui;

import core.SimulationController;
import utils.LocalizationManager;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.function.Consumer;

public class TimeControlPanel extends JPanel {
    private final SimulationController simulationController;
    private final LocalizationManager localizationManager;
    private final Consumer<Locale> localeChangeCallback;

    private final JButton startPauseButton;
    private final JLabel speedLabel;
    private final JSlider speedSlider;
    private final JLabel languageLabel;

    public TimeControlPanel(SimulationController simulationController,
                            LocalizationManager localizationManager,
                            Consumer<Locale> localeChangeCallback) {
        this.simulationController = simulationController;
        this.localizationManager = localizationManager;
        this.localeChangeCallback = localeChangeCallback;

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder(localizationManager.getString("controls")));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        startPauseButton = new JButton(localizationManager.getString("start"));
        startPauseButton.addActionListener(e -> toggleSimulation());

        speedLabel = new JLabel(localizationManager.getString("speed") + ": 1.0x");
        speedSlider = new JSlider(1, 50, 10);
        speedSlider.addChangeListener(e -> {
            double speed = speedSlider.getValue() / 10.0;
            simulationController.setTimeSpeed(speed);
            speedLabel.setText(localizationManager.getString("speed") + ": " + speed + "x");
        });

        languageLabel = new JLabel(localizationManager.getString("language"));
        JComboBox<String> languageCombo = new JComboBox<>(new String[]{"English", "\u4E2D\u6587"});
        languageCombo.addActionListener(e -> {
            if (languageCombo.getSelectedIndex() == 1) {
                localeChangeCallback.accept(Locale.SIMPLIFIED_CHINESE);
            } else {
                localeChangeCallback.accept(Locale.ENGLISH);
            }
        });

        gbc.gridy = 0;
        add(startPauseButton, gbc);
        gbc.gridy = 1;
        add(speedLabel, gbc);
        gbc.gridy = 2;
        add(speedSlider, gbc);
        gbc.gridy = 3;
        add(languageLabel, gbc);
        gbc.gridy = 4;
        add(languageCombo, gbc);
    }

    private void toggleSimulation() {
        if (simulationController.isRunning()) {
            simulationController.pauseSimulation();
            startPauseButton.setText(localizationManager.getString("start"));
        } else {
            simulationController.startSimulation();
            startPauseButton.setText(localizationManager.getString("pause"));
        }
    }

    public void refreshTexts() {
        setBorder(BorderFactory.createTitledBorder(localizationManager.getString("controls")));
        if (simulationController.isRunning()) {
            startPauseButton.setText(localizationManager.getString("pause"));
        } else {
            startPauseButton.setText(localizationManager.getString("start"));
        }
        double speed = simulationController.getTimeSpeed();
        speedLabel.setText(localizationManager.getString("speed") + ": " + speed + "x");
        languageLabel.setText(localizationManager.getString("language"));
    }
}
