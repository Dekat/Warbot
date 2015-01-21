package edu.warbot.tools;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Random;

public class CoordCartesian extends Point2D {

	private double _x;
	private double _y;
	
	public CoordCartesian(double x, double y) {
		_x = x;
		_y = y;
	}
	
	public CoordCartesian(Point point) {
		_x = point.getX();
		_y = point.getY();
	}
	
	public CoordPolar toPolar() {
		return new CoordPolar(Math.sqrt(Math.pow(_x, 2) + Math.pow(_y, 2)),
				Math.toDegrees(Math.atan2(_y, _x)));
	}

	@Override
	public double getX() {
		return _x;
	}

	@Override
	public double getY() {
		return _y;
	}

	@Override
	public void setLocation(double x, double y) {
		_x = x;
		_y = y;
	}

	public void add(CoordCartesian cartesian) {
		_x += cartesian.getX();
		_y += cartesian.getY();
	}
	
	public void normalize(double minX, double maxX, double minY, double maxY) {
		if (_x < minX)
			_x = minX;
		else if (_x > maxX)
			_x = maxX;
		if (_y < minY)
			_y = minY;
		else if (_y > maxY)
			_y = maxY;
	}
	
	public Point toPoint() {
		return new Point((int) getX(), (int) getY());
	}
	
	public static CoordCartesian getRandomInBounds (int x, int y, int width, int height) {
		Random random = new Random();
		return new CoordCartesian((random.nextDouble() * (width - x)) + x, (random.nextDouble() * (height - y)) + y);
	}
	
	public double getAngleToPoint(CoordCartesian p) {
		CoordCartesian pFromOrigin = new CoordCartesian(p.getX() - _x, p.getY() - _y);
		return pFromOrigin.toPolar().getAngle();
	}
	
	@Override
	public String toString() {
		return "(" + _x + "; " + _y + ")";
	}
}
