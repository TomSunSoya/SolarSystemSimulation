package ui;

import java.awt.*;
import java.awt.geom.Point2D;

public class ViewTransform {
    private static final double COMPRESSION_FACTOR = 1.35;

    private final double centerX;
    private final double centerY;
    private final double panX;
    private final double panY;
    private final double scale;

    public ViewTransform(double centerX, double centerY, double panX, double panY, double scale) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.panX = panX;
        this.panY = panY;
        this.scale = scale;
    }

    public Point toScreen(Point2D.Double worldPositionAu) {
        Point2D.Double compressed = compress(worldPositionAu);
        return toScreenFromCompressed(compressed);
    }

    public Point toScreenFromCompressed(Point2D.Double compressedPoint) {
        return new Point(
                (int) Math.round(centerX + panX + compressedPoint.x * scale),
                (int) Math.round(centerY + panY + compressedPoint.y * scale)
        );
    }

    public Point2D.Double toCompressedWorld(Point screenPoint) {
        return new Point2D.Double(
                (screenPoint.x - centerX - panX) / scale,
                (screenPoint.y - centerY - panY) / scale
        );
    }

    public double scaleLength(double worldLengthAu) {
        return compressedRadius(worldLengthAu) * scale;
    }

    public static Point2D.Double compress(Point2D.Double point) {
        double radius = Math.hypot(point.x, point.y);
        if (radius == 0) {
            return new Point2D.Double();
        }

        double compressedRadius = compressedRadius(radius);
        double compression = compressedRadius / radius;
        return new Point2D.Double(point.x * compression, point.y * compression);
    }

    public static double compressedRadius(double radiusAu) {
        return Math.log1p(radiusAu * COMPRESSION_FACTOR);
    }
}
