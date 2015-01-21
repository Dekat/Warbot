package edu.warbot.tools;

public class Vector2D {
	
	private CoordPolar _src, _dest;
	private Double _distance, _angle;
	
	public Vector2D(CoordPolar src, CoordPolar dest) {
		_src = src;
		_dest = dest;
		_distance = 0.0;
		_angle = 0.0;
	}
	
	public Double getX(Double dist, Double angl) {
		Double cos = Math.cos(Math.toRadians(_dest.getAngle()));
		cos = cos * _dest.getDistance();
		Double cos2 = Math.cos(Math.toRadians(angl));
		cos2 = cos2 * dist;
		return cos - cos2;
	}
	
	public Double getY(Double dist, Double angl) {
		Double sin = Math.sin(Math.toRadians(_dest.getAngle()));
		sin = sin * _dest.getDistance();
		Double sin2 = Math.sin(Math.toRadians(angl));
		sin2 = sin2 * dist;
		return sin - sin2;
	}
	
	public void calculateDistance() {
		_distance = Math.sqrt(Math.pow(getX(_src.getDistance(),_src.getAngle()),2) + Math.pow(getY(_src.getDistance(),_src.getAngle()),2));
	}
	
	public Double getDistance() {
		return _distance;
	}
	
	public void calculateAngle() {
		if(getX(_src.getDistance(),_src.getAngle()) < 0) {
			_angle = Math.toDegrees(Math.atan(getY(_src.getDistance(),_src.getAngle())/getX(_src.getDistance(),_src.getAngle()))) + 180;
		}
		else {
			_angle = Math.toDegrees(Math.atan(getY(_src.getDistance(),_src.getAngle())/getX(_src.getDistance(),_src.getAngle())));
		}
	}
	
	public Double getAngle() {
		return _angle;
	}
	
}
