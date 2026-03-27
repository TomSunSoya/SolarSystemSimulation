package ui;

import core.Planet;
import core.SolarSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class SolarSystemPanel extends JPanel {
    private final SolarSystem solarSystem;
    private final Consumer<Planet> onPlanetSelected;

    public SolarSystemPanel(SolarSystem solarSystem, Consumer<Planet> onPlanetSelected) {
        this.solarSystem = solarSystem;
        this.onPlanetSelected = onPlanetSelected;

        setBackground(Color.BLACK);

        Timer repaintTimer = new Timer(16, e -> repaint());
        repaintTimer.start();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Planet clickedPlanet = findPlanetAt(e.getX(), e.getY());
                if (clickedPlanet != null) {
                    onPlanetSelected.accept(clickedPlanet);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        g2d.setColor(Color.DARK_GRAY);
        for (Planet planet : solarSystem.getPlanets()) {
            int orbit = (int) planet.getOrbitRadius();
            g2d.drawOval(centerX - orbit, centerY - orbit, orbit * 2, orbit * 2);
        }

        g2d.setColor(Color.YELLOW);
        int sunRadius = 24;
        g2d.fillOval(centerX - sunRadius, centerY - sunRadius, sunRadius * 2, sunRadius * 2);

        for (Planet planet : solarSystem.getPlanets()) {
            planet.render(g2d, centerX, centerY);
        }
    }

    private Planet findPlanetAt(int x, int y) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        for (Planet planet : solarSystem.getPlanets()) {
            int px = planet.getCurrentX(centerX);
            int py = planet.getCurrentY(centerY);
            double distance = Math.hypot(x - px, y - py);
            if (distance <= planet.getRadius() + 4) {
                return planet;
            }
        }
        return null;
    }
}
