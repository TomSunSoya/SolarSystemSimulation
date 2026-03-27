package ui;

import core.Orbit;
import core.Planet;
import core.SolarSystem;
import core.Sun;
import utils.LocalizationManager;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Deque;
import java.util.Map;

public class SolarSystemRenderer {
    public void render(Graphics2D g2d,
                       SolarSystem solarSystem,
                       Planet selectedPlanet,
                       LocalizationManager localizationManager,
                       ViewTransform transform,
                       Map<Planet, Deque<Point2D.Double>> trails,
                       Dimension viewportSize) {
        renderBackground(g2d, viewportSize);
        renderOrbits(g2d, solarSystem, transform);
        renderTrails(g2d, trails, transform);
        renderSun(g2d, solarSystem.getSun(), transform);
        renderPlanets(g2d, solarSystem, selectedPlanet, localizationManager, transform);
    }

    private void renderBackground(Graphics2D g2d, Dimension viewportSize) {
        GradientPaint gradientPaint = new GradientPaint(
                0, 0, new Color(7, 10, 26),
                0, viewportSize.height, new Color(16, 21, 49)
        );
        g2d.setPaint(gradientPaint);
        g2d.fillRect(0, 0, viewportSize.width, viewportSize.height);
    }

    private void renderOrbits(Graphics2D g2d, SolarSystem solarSystem, ViewTransform transform) {
        Stroke originalStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{5f, 5f}, 0f));
        g2d.setColor(new Color(255, 255, 255, 45));

        for (Planet planet : solarSystem.getPlanets()) {
            drawPath(g2d, planet.getOrbit(), transform);
        }

        g2d.setStroke(originalStroke);
    }

    private void renderTrails(Graphics2D g2d, Map<Planet, Deque<Point2D.Double>> trails, ViewTransform transform) {
        for (Map.Entry<Planet, Deque<Point2D.Double>> entry : trails.entrySet()) {
            Planet planet = entry.getKey();
            Deque<Point2D.Double> trail = entry.getValue();
            if (trail.size() < 2) {
                continue;
            }

            Point previous = null;
            int index = 0;
            for (Point2D.Double position : trail) {
                Point current = transform.toScreen(position);
                if (previous != null) {
                    float alpha = (float) index / trail.size();
                    Color trailColor = new Color(
                            planet.getPrimaryColor().getRed(),
                            planet.getPrimaryColor().getGreen(),
                            planet.getPrimaryColor().getBlue(),
                            Math.max(18, Math.min(150, (int) (alpha * 170)))
                    );
                    g2d.setColor(trailColor);
                    g2d.drawLine(previous.x, previous.y, current.x, current.y);
                }
                previous = current;
                index++;
            }
        }
    }

    private void renderSun(Graphics2D g2d, Sun sun, ViewTransform transform) {
        Point sunPosition = transform.toScreen(new Point2D.Double(0, 0));
        float glowRadius = (float) Math.max(46, transform.scaleLength(0.42));
        RadialGradientPaint glow = new RadialGradientPaint(
                sunPosition.x,
                sunPosition.y,
                glowRadius,
                new float[]{0f, 0.4f, 1f},
                new Color[]{
                        new Color(255, 250, 214, 220),
                        sun.getGlowColor(),
                        new Color(sun.getGlowColor().getRed(), sun.getGlowColor().getGreen(), sun.getGlowColor().getBlue(), 0)
                }
        );

        Paint originalPaint = g2d.getPaint();
        g2d.setPaint(glow);
        g2d.fillOval(
                (int) (sunPosition.x - glowRadius),
                (int) (sunPosition.y - glowRadius),
                (int) (glowRadius * 2),
                (int) (glowRadius * 2)
        );

        int coreRadius = 18;
        g2d.setPaint(new GradientPaint(
                sunPosition.x - coreRadius,
                sunPosition.y - coreRadius,
                new Color(255, 251, 184),
                sunPosition.x + coreRadius,
                sunPosition.y + coreRadius,
                sun.getCoreColor()
        ));
        g2d.fillOval(sunPosition.x - coreRadius, sunPosition.y - coreRadius, coreRadius * 2, coreRadius * 2);
        g2d.setPaint(originalPaint);
    }

    private void renderPlanets(Graphics2D g2d,
                               SolarSystem solarSystem,
                               Planet selectedPlanet,
                               LocalizationManager localizationManager,
                               ViewTransform transform) {
        Font originalFont = g2d.getFont();
        g2d.setFont(originalFont.deriveFont(Font.BOLD, 12f));

        for (Planet planet : solarSystem.getPlanets()) {
            Point screenPosition = transform.toScreen(planet.getCurrentPosition());
            int radius = getPlanetRadius(planet);

            if (planet == selectedPlanet) {
                g2d.setColor(new Color(255, 255, 255, 140));
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawOval(screenPosition.x - radius - 6, screenPosition.y - radius - 6,
                        (radius + 6) * 2, (radius + 6) * 2);
            }

            Paint originalPaint = g2d.getPaint();
            g2d.setPaint(new GradientPaint(
                    screenPosition.x - radius,
                    screenPosition.y - radius,
                    planet.getSecondaryColor(),
                    screenPosition.x + radius,
                    screenPosition.y + radius,
                    planet.getPrimaryColor()
            ));
            g2d.fillOval(screenPosition.x - radius, screenPosition.y - radius, radius * 2, radius * 2);
            g2d.setPaint(originalPaint);

            g2d.setColor(new Color(255, 255, 255, 210));
            g2d.drawString(localizationManager.getString("planet." + planet.getId()),
                    screenPosition.x + radius + 6, screenPosition.y - radius - 2);
        }

        g2d.setFont(originalFont);
    }

    private void drawPath(Graphics2D g2d, Orbit orbit, ViewTransform transform) {
        Path2D.Double path = new Path2D.Double();
        boolean firstPoint = true;

        for (Point2D.Double point : orbit.samplePath(240)) {
            Point screenPoint = transform.toScreen(point);
            if (firstPoint) {
                path.moveTo(screenPoint.x, screenPoint.y);
                firstPoint = false;
            } else {
                path.lineTo(screenPoint.x, screenPoint.y);
            }
        }
        path.closePath();
        g2d.draw(path);
    }

    private int getPlanetRadius(Planet planet) {
        double scaled = 2.4 + Math.log10(planet.getPhysicalRadius());
        return Math.max(4, Math.min(18, (int) Math.round(scaled)));
    }
}
