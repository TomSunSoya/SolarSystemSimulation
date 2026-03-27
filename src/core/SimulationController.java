package core;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SimulationController {
    private static final int DEFAULT_DELAY = 16;

    private final SolarSystem solarSystem;
    private final TimeController timeController;
    private final Timer timer;
    private final List<Runnable> tickListeners;
    private boolean isRunning;

    public SimulationController(SolarSystem solarSystem, TimeController timeController) {
        this.solarSystem = solarSystem;
        this.timeController = timeController;
        this.isRunning = false;
        this.tickListeners = new ArrayList<>();

        timer = new Timer(DEFAULT_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRunning) {
                    update((double) DEFAULT_DELAY / 1000);
                }
            }
        });
    }

    public void startSimulation() {
        if (!isRunning) {
            isRunning = true;
            timer.start();
        }
    }

    public void pauseSimulation() {
        if (isRunning) {
            isRunning = false;
            timer.stop();
        }
    }

    public void update(double elapsedTime) {
        double simulatedDays = timeController.advance(elapsedTime);
        solarSystem.update(simulatedDays);
        for (Runnable tickListener : tickListeners) {
            tickListener.run();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setTimeSpeed(double speed) {
        timeController.setTimeSpeed(speed);
    }

    public double getTimeSpeed() {
        return timeController.getTimeSpeed();
    }

    public double getElapsedSimulationDays() {
        return timeController.getElapsedTime();
    }

    public double getElapsedSimulationYears() {
        return timeController.getElapsedYears();
    }

    public void toggleSimulation() {
        if (isRunning) {
            pauseSimulation();
        } else {
            startSimulation();
        }
    }

    public void multiplyTimeSpeed(double factor) {
        setTimeSpeed(getTimeSpeed() * factor);
    }

    public void addTickListener(Runnable tickListener) {
        tickListeners.add(tickListener);
    }
}
