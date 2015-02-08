package edu.warbot.tools;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class GeometryTools {

    public static Path2D.Float moveToAndRotateShape(Shape shape, double newX, double newY, double angle) {
        Path2D.Float transformedShape = new Path2D.Float();
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

    public static Path2D.Float translateShape(Shape shape, double newX, double newY) {
        Path2D.Float transformedShape = new Path2D.Float();
        transformedShape.append(shape, false);
        AffineTransform t = new AffineTransform();
        t.setToTranslation(newX, newY);
        transformedShape.transform(t);
        return transformedShape;
    }

    public static Path2D.Float resize(Shape shape, double multiplier) {
        Path2D.Float transformedShape = new Path2D.Float();
        transformedShape.append(shape, false);
        AffineTransform t = new AffineTransform();
        t.scale(multiplier, multiplier);
        transformedShape.transform(t);
        return transformedShape;
    }
}
