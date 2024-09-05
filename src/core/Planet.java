package core;

import java.awt.*;

public class Planet {
    private String name;
    private double orbitRadius;
    private double orbitSpeed;
    private double angle;
    private double radius;
    private Color color;

    public Planet(String name, double orbitRadius, double orbitSpeed, double radius, Color color) {
        this.name = name;
        this.orbitRadius = orbitRadius;
        this.orbitSpeed = orbitSpeed;
        this.radius = radius;
        this.color = color;
        this.angle = 0;
    }

    public void updatePosition(double elapsedTime) {
        // Update the position of the planet
        angle += orbitSpeed * elapsedTime;
    }

    public void render(Graphics2D g2d, int x, int y) {
        // Render the planet
        int planetX = (int) (x + orbitRadius * Math.cos(angle));
        int planetY = (int) (y + orbitRadius * Math.sin(angle));
        g2d.setColor(color);
        g2d.fillOval(planetX - (int) radius, planetY - (int) radius, (int) radius * 2, (int) radius * 2);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
