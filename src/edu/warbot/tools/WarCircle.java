package edu.warbot.tools;

import com.badlogic.gdx.math.Circle;

@SuppressWarnings("serial")
public class WarCircle extends Circle {

	public WarCircle(float x, float y, float radius) {
		super(x, y, radius);
	}
	
	public CoordCartesian getCenterPosition() {
		return new CoordCartesian(x, y);
	}
	
}
