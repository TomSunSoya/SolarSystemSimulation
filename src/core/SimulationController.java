package core;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationController {
    private SolarSystem solarSystem;
    private TimeController timeController;
    private boolean isRunning;
    private Timer timer;
    private static final int DEFAULT_DELAY = 16;

    public SimulationController(SolarSystem solarSystem, TimeController timeController) {
        this.solarSystem = solarSystem;
        this.timeController = timeController;
        this.isRunning = false;

        timer = new Timer(DEFAULT_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRunning)
                    update((double) DEFAULT_DELAY / 1000);
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
        double timeSpeed = timeController.getTimeSpeed();
        solarSystem.update(elapsedTime * timeSpeed);
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
}
