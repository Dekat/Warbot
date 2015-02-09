package edu.warbot.tools;

import com.badlogic.gdx.math.Circle;

import java.awt.geom.Ellipse2D;

@SuppressWarnings("serial")
public class WarCircle extends Ellipse2D.Double {

	public WarCircle(double x, double y, double radius) {
		super(x, y, radius, radius);
	}
	
	public CoordCartesian getCenterPosition() {
		return new CoordCartesian(x, y);
	}

    public double getRadius() {
        return getWidth();
    }

}
