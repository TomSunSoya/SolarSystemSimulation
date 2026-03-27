package ui;

import core.Planet;
import core.SolarSystem;
import utils.LocalizationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SolarSystemPanel extends JPanel {
    private static final int TRAIL_LENGTH = 150;
    private static final double BASE_ZOOM = 1.0;
    private static final double MIN_ZOOM = 0.6;
    private static final double MAX_ZOOM = 12.0;

    private final SolarSystem solarSystem;
    private final LocalizationManager localizationManager;
    private final Consumer<Planet> onPlanetSelected;
    private final SolarSystemRenderer renderer;
    private final Map<Planet, Deque<Point2D.Double>> trails;

    private Planet selectedPlanet;
    private Point dragAnchor;
    private double zoom;
    private double panX;
    private double panY;
    private double baselinePanX;
    private double baselinePanY;
    private boolean baselineDirty;

    public SolarSystemPanel(SolarSystem solarSystem,
                            LocalizationManager localizationManager,
                            Consumer<Planet> onPlanetSelected) {
        this.solarSystem = solarSystem;
        this.localizationManager = localizationManager;
        this.onPlanetSelected = onPlanetSelected;
        this.renderer = new SolarSystemRenderer();
        this.trails = new LinkedHashMap<>();
        this.zoom = BASE_ZOOM;
        this.baselineDirty = true;

        setBackground(new Color(8, 12, 28));
        setFocusable(true);
        initializeTrails();
        registerInteractions();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                baselineDirty = true;
                applyBaselineView();
                repaint();
            }
        });
    }

    public void onSimulationTick() {
        for (Planet planet : solarSystem.getPlanets()) {
            Deque<Point2D.Double> trail = trails.computeIfAbsent(planet, ignored -> new ArrayDeque<>());
            trail.addLast(planet.getCurrentPosition());
            while (trail.size() > TRAIL_LENGTH) {
                trail.removeFirst();
            }
        }
        repaint();
    }

    public void setSelectedPlanet(Planet selectedPlanet) {
        this.selectedPlanet = selectedPlanet;
        repaint();
    }

    public void panBy(double deltaX, double deltaY) {
        panX += deltaX;
        panY += deltaY;
        repaint();
    }

    public void resetView() {
        baselineDirty = true;
        applyBaselineView();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        applyBaselineView();

        Graphics2D g2d = (Graphics2D) graphics.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        ViewTransform transform = new ViewTransform(
                getWidth() / 2.0,
                getHeight() / 2.0,
                panX,
                panY,
                getPixelsPerCompressedUnit()
        );

        renderer.render(g2d, solarSystem, selectedPlanet, localizationManager, transform, trails, getSize());
        g2d.dispose();
    }

    private void initializeTrails() {
        trails.clear();
        for (Planet planet : solarSystem.getPlanets()) {
            Deque<Point2D.Double> trail = new ArrayDeque<>();
            trail.add(planet.getCurrentPosition());
            trails.put(planet, trail);
        }
    }

    private void registerInteractions() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragAnchor = e.getPoint();
                requestFocusInWindow();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragAnchor = null;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragAnchor == null) {
                    return;
                }

                panBy(e.getX() - dragAnchor.x, e.getY() - dragAnchor.y);
                dragAnchor = e.getPoint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                Planet clickedPlanet = findPlanetAt(e.getPoint());
                if (clickedPlanet != null) {
                    selectedPlanet = clickedPlanet;
                    onPlanetSelected.accept(clickedPlanet);
                }
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double zoomFactor = e.getPreciseWheelRotation() < 0 ? 1.15 : 1 / 1.15;
                zoomAt(e.getPoint(), zoomFactor);
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        addMouseWheelListener(mouseAdapter);
    }

    private void zoomAt(Point pivot, double zoomFactor) {
        double newZoom = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, zoom * zoomFactor));
        double oldScale = getPixelsPerCompressedUnit();
        Point2D.Double compressedCoordinate = new Point2D.Double(
                (pivot.x - getWidth() / 2.0 - panX) / oldScale,
                (pivot.y - getHeight() / 2.0 - panY) / oldScale
        );

        zoom = newZoom;

        double newScale = getPixelsPerCompressedUnit();
        panX = pivot.x - getWidth() / 2.0 - compressedCoordinate.x * newScale;
        panY = pivot.y - getHeight() / 2.0 - compressedCoordinate.y * newScale;
        repaint();
    }

    private Planet findPlanetAt(Point point) {
        ViewTransform transform = new ViewTransform(
                getWidth() / 2.0,
                getHeight() / 2.0,
                panX,
                panY,
                getPixelsPerCompressedUnit()
        );

        for (Planet planet : solarSystem.getPlanets()) {
            Point planetPoint = transform.toScreen(planet.getCurrentPosition());
            int radius = Math.max(8, (int) Math.round(2.4 + Math.log10(planet.getPhysicalRadius())));
            if (planetPoint.distance(point) <= radius + 6) {
                return planet;
            }
        }
        return null;
    }

    private double getPixelsPerCompressedUnit() {
        Rectangle.Double bounds = getCompressedOrbitBounds();
        double baseScale = computeBaseScale(bounds);
        return baseScale * zoom;
    }

    private void applyBaselineView() {
        if (!baselineDirty || getWidth() <= 0 || getHeight() <= 0) {
            return;
        }

        Rectangle.Double bounds = getCompressedOrbitBounds();
        double baseScale = computeBaseScale(bounds);
        baselinePanX = -((bounds.x + bounds.width / 2.0) * baseScale);
        baselinePanY = -((bounds.y + bounds.height / 2.0) * baseScale);

        zoom = BASE_ZOOM;
        panX = baselinePanX;
        panY = baselinePanY;
        baselineDirty = false;
    }

    private Rectangle.Double getCompressedOrbitBounds() {
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        for (Planet planet : solarSystem.getPlanets()) {
            for (Point2D.Double point : planet.getOrbit().samplePath(240)) {
                Point2D.Double compressed = ViewTransform.compress(point);
                minX = Math.min(minX, compressed.x);
                minY = Math.min(minY, compressed.y);
                maxX = Math.max(maxX, compressed.x);
                maxY = Math.max(maxY, compressed.y);
            }
        }

        if (!Double.isFinite(minX) || !Double.isFinite(minY) || !Double.isFinite(maxX) || !Double.isFinite(maxY)) {
            return new Rectangle.Double(-1, -1, 2, 2);
        }

        return new Rectangle.Double(minX, minY, maxX - minX, maxY - minY);
    }

    private double computeBaseScale(Rectangle.Double bounds) {
        double availableWidth = Math.max(240, getWidth() * 0.82);
        double availableHeight = Math.max(240, getHeight() * 0.82);
        return Math.min(
                availableWidth / Math.max(1.0, bounds.width),
                availableHeight / Math.max(1.0, bounds.height)
        );
    }
}
