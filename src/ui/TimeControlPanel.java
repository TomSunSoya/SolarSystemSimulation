package ui;

import core.SimulationController;
import utils.LocalizationManager;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.Consumer;

public class TimeControlPanel extends JPanel {
    private final SimulationController simulationController;
    private final LocalizationManager localizationManager;
    private final Consumer<Locale> localeChangeCallback;

    private final JButton startPauseButton;
    private final JButton resetViewButton;
    private final JLabel speedLabel;
    private final JLabel clockLabel;
    private final JLabel languageLabel;
    private final JLabel hintLabel;
    private final JSlider speedSlider;

    public TimeControlPanel(SimulationController simulationController,
                            LocalizationManager localizationManager,
                            Consumer<Locale> localeChangeCallback,
                            Runnable resetViewCallback) {
        this.simulationController = simulationController;
        this.localizationManager = localizationManager;
        this.localeChangeCallback = localeChangeCallback;

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder(localizationManager.getString("controls")));
        setBackground(new Color(248, 244, 237));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        startPauseButton = new JButton();
        startPauseButton.addActionListener(e -> toggleSimulation());

        resetViewButton = new JButton();
        resetViewButton.addActionListener(e -> resetViewCallback.run());

        speedLabel = new JLabel();
        speedSlider = new JSlider(1, 720, (int) Math.round(simulationController.getTimeSpeed()));
        speedSlider.addChangeListener(e -> {
            simulationController.setTimeSpeed(speedSlider.getValue());
            refreshDynamicText();
        });

        clockLabel = new JLabel();
        languageLabel = new JLabel();
        hintLabel = new JLabel();
        hintLabel.setFont(hintLabel.getFont().deriveFont(Font.PLAIN, 11f));
        hintLabel.setForeground(new Color(86, 86, 86));

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
        add(resetViewButton, gbc);

        gbc.gridy = 2;
        add(speedLabel, gbc);

        gbc.gridy = 3;
        add(speedSlider, gbc);

        gbc.gridy = 4;
        add(clockLabel, gbc);

        gbc.gridy = 5;
        add(languageLabel, gbc);

        gbc.gridy = 6;
        add(languageCombo, gbc);

        gbc.gridy = 7;
        add(hintLabel, gbc);

        refreshTexts();
    }

    public void toggleSimulation() {
        simulationController.toggleSimulation();
        refreshTexts();
    }

    public void changeSpeedByFactor(double factor) {
        simulationController.multiplyTimeSpeed(factor);
        speedSlider.setValue((int) Math.round(simulationController.getTimeSpeed()));
        refreshDynamicText();
    }

    public void refreshTexts() {
        setBorder(BorderFactory.createTitledBorder(localizationManager.getString("controls")));
        startPauseButton.setText(localizationManager.getString(
                simulationController.isRunning() ? "pause" : "start"
        ));
        resetViewButton.setText(localizationManager.getString("reset_view"));
        languageLabel.setText(localizationManager.getString("language"));
        hintLabel.setText(localizationManager.getString("interaction_hint"));
        refreshDynamicText();
    }

    public void refreshDynamicText() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(localizationManager.getLocale());
        numberFormat.setMaximumFractionDigits(1);
        numberFormat.setMinimumFractionDigits(1);

        speedLabel.setText(localizationManager.getString("speed") + ": " +
                numberFormat.format(simulationController.getTimeSpeed()) + " " +
                localizationManager.getString("unit_days_per_second"));

        clockLabel.setText(localizationManager.getString("simulated_time") + ": " +
                numberFormat.format(simulationController.getElapsedSimulationDays()) + " " +
                localizationManager.getString("unit_days") + " / " +
                numberFormat.format(simulationController.getElapsedSimulationYears()) + " " +
                localizationManager.getString("unit_years"));
    }
}
