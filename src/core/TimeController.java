package core;

public class TimeController {
    private double elapsedSimulationDays;
    private double timeSpeed;

    public TimeController() {
        this.elapsedSimulationDays = 0;
        this.timeSpeed = 30;
    }

    public double advance(double elapsedRealSeconds) {
        double simulatedDays = elapsedRealSeconds * timeSpeed;
        elapsedSimulationDays += simulatedDays;
        return simulatedDays;
    }

    public double getElapsedTime() {
        return elapsedSimulationDays;
    }

    public double getElapsedYears() {
        return elapsedSimulationDays / 365.25;
    }

    public double getTimeSpeed() {
        return timeSpeed;
    }

    public void setTimeSpeed(double timeSpeed) {
        this.timeSpeed = Math.max(0.5, Math.min(timeSpeed, 720));
    }
}
