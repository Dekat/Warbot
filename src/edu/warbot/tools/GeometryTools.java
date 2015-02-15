package edu.warbot.tools;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

public class GeometryTools {

    public static Path2D.Double moveToAndRotateShape(Shape shape, double newX, double newY, double angle) {
        Path2D.Double transformedShape = new Path2D.Double();
        transformedShape.append(shape, false);
        AffineTransform t = new AffineTransform();
        Rectangle2D shapeBounds = shape.getBounds2D();
        t.setToRotation(Math.toRadians(angle), shapeBounds.getWidth()/2., shapeBounds.getHeight()/2.);
        transformedShape.transform(t);
        t = new AffineTransform();
        t.setToTranslation(newX, newY);
        transformedShape.transform(t);
        return transformedShape;
    }

    public static Path2D.Double translateShape(Shape shape, double newX, double newY) {
        Path2D.Double transformedShape = new Path2D.Double();
        transformedShape.append(shape, false);
        AffineTransform t = new AffineTransform();
        t.setToTranslation(newX, newY);
        transformedShape.transform(t);
        return transformedShape;
    }

    public static Path2D.Double resize(Shape shape, double multiplier) {
        Path2D.Double transformedShape = new Path2D.Double();
        transformedShape.append(shape, false);
        AffineTransform t = new AffineTransform();
        t.scale(multiplier, multiplier);
        transformedShape.transform(t);
        return transformedShape;
    }

    public static List<Path2D.Double> dividePluralPathIntoSingularPaths(Path2D.Double path) {
        long start = System.nanoTime();
        List<Path2D.Double> singularPaths = new ArrayList<Path2D.Double>();

        PathIterator it = path.getPathIterator(null);
        double[] coords = new double[6];
        Path2D.Double currentPath = null;
        while (!it.isDone()) {
            int type = it.currentSegment(coords);
            switch (type) {
                case PathIterator.SEG_MOVETO:
                    if(currentPath != null)
                        singularPaths.add(new Path2D.Double(currentPath));
                    currentPath = new Path2D.Double();
                    currentPath.moveTo(coords[0], coords[1]);
                    break;
                case PathIterator.SEG_LINETO:
                    currentPath.lineTo(coords[0], coords[1]);
                    break;
                case PathIterator.SEG_CUBICTO:
                    currentPath.curveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
                    break;
                case PathIterator.SEG_QUADTO:
                    currentPath.quadTo(coords[0], coords[1], coords[2], coords[3]);
                    break;
                case PathIterator.SEG_CLOSE:
                    currentPath.closePath();
                    break;
                default:
                    throw new IllegalStateException("unknown PathIterator segment type: " + type);
            }
            it.next();
        }
        if(currentPath != null)
            singularPaths.add(new Path2D.Double(currentPath));

        long timePassed = System.nanoTime()-start;
        System.out.println("dividePluralPathIntoSingularPaths = " + timePassed);

        return singularPaths;
    }

    public static List<Point2D.Double> getPointsFromPath(Path2D.Double path) {
        List<Point2D.Double> points = new ArrayList<Point2D.Double>();

        PathIterator it = path.getPathIterator(null);
        double[] coords = new double[6];
        Path2D.Double currentPath = null;
        while (!it.isDone()) {
            int type = it.currentSegment(coords);
            switch (type) {
                case PathIterator.SEG_MOVETO:
                    points.add(new Point2D.Double(coords[0], coords[1]));
                    break;
                case PathIterator.SEG_LINETO:
                    points.add(new Point2D.Double(coords[0], coords[1]));
                    break;
                case PathIterator.SEG_CUBICTO:
                    points.add(new Point2D.Double(coords[0], coords[1]));
                    break;
                case PathIterator.SEG_QUADTO:
                    points.add(new Point2D.Double(coords[0], coords[1]));
                    break;
            }
            it.next();
        }

        return points;
    }
}
