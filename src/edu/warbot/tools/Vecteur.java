package edu.warbot.tools;

public class Vecteur {

	private CoordCartesian _src, _dest;
	
	public Vecteur(CoordCartesian src, CoordCartesian dest) {
		_src = src;
		_dest = dest;
	}
	
	public Vecteur(double x1, double y1, double x2, double y2) {
		_src = new CoordCartesian(x1, y1);
		_dest = new CoordCartesian(x2, y2);
	}
	
	public CoordCartesian getSrcPoint() {
		return _src;
	}
	
	public CoordCartesian getDestPoint() {
		return _dest;
	}
}
