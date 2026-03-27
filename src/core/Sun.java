package core;

import java.awt.*;

public class Sun {
    public static final double RADIUS_KILOMETERS = 696340;

    private final double radius;
    private final Color coreColor;
    private final Color glowColor;

    public Sun() {
        this.radius = RADIUS_KILOMETERS;
        this.coreColor = new Color(255, 214, 102);
        this.glowColor = new Color(255, 168, 53);
    }

    public double getRadius() {
        return radius;
    }

    public Color getCoreColor() {
        return coreColor;
    }

    public Color getGlowColor() {
        return glowColor;
    }
}
