package core;

public class TimeController {
    private double elapsedTime;
    private double timeScale;
    private double timeSpeed;

    public TimeController() {
        this.elapsedTime = 0;
        this.timeScale = 1;
        this.timeSpeed = 1;
    }

    public void update(double elapsedTime) {
        this.elapsedTime += elapsedTime * timeScale;
    }

    public double getElapsedTime() {
        return elapsedTime;
    }

    public void setTimeScale(double timeScale) {
        this.timeScale = timeScale;
    }

    public double getTimeScale() {
        return timeScale;
    }

    public double getTimeSpeed() {
        return timeSpeed;
    }

    public void setTimeSpeed(double timeSpeed) {
        this.timeSpeed = timeSpeed;
    }
}
