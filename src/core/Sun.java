package core;

import java.awt.*;

public class Sun {
    public static final double SUN_RADIUS = 696300;

    private double radius;
    private Color color;

    public Sun() {
        this.radius = SUN_RADIUS;
        this.color = Color.YELLOW;
    }

    public void render(Graphics2D g2d, int x, int y) {
        g2d.setColor(color);
        g2d.fillOval(x - (int) radius, y - (int) radius, (int) radius * 2, (int) radius * 2);
    }
}
