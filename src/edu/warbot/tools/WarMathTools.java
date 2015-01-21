package edu.warbot.tools;

import java.util.ArrayList;

import com.badlogic.gdx.math.Circle;

public class WarMathTools {

	public static double getDistanceBetweenTwoPoints(double x1, double y1, double x2, double y2) {
		return Math.hypot(x2 - x1, y2 - y1);
	}
	
	public static CoordCartesian addTwoPoints(CoordCartesian p1, CoordCartesian p2) {
		CoordCartesian toReturn = new CoordCartesian(p1.getX(), p1.getY());
		toReturn.add(p2);
		return toReturn;
	}
	
	public static CoordCartesian addTwoPoints(CoordCartesian p1, CoordPolar p2) {
		return addTwoPoints(p1, p2.toCartesian());
	}
	
	public static CoordPolar addTwoPoints(CoordPolar p1, CoordPolar p2) {
		return addTwoPoints(p1.toCartesian(), p2.toCartesian()).toPolar();
	}
	
	public static CoordCartesian getCenterOfPoints(ArrayList<Circle> points) {
		double sumX = 0;
		double sumY = 0;
		for (Circle c : points) {
			sumX += c.x;
			sumY += c.y;
		}
		return new CoordCartesian(sumX / points.size(), sumY / points.size());
	}
}
