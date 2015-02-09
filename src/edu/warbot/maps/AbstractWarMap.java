package edu.warbot.maps;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import edu.warbot.launcher.WarConfig;
import edu.warbot.tools.CoordCartesian;
import edu.warbot.tools.GeometryTools;
import edu.warbot.tools.WarCircle;
import org.w3c.dom.css.Rect;

public abstract class AbstractWarMap {

    private static final double MAP_ACCESSIBLE_AREA_PADDING = 5.;
    private static final double BORDER_THICKNESS = 5.;

	protected static final float TEAM_POSITION_RADIUS = 100;
	protected static final float FOOD_POSITION_RADIUS = 200;

    protected Area mapAccessibleArea;
	private ArrayList<ArrayList<WarCircle>> _teamsPositions;
	private ArrayList<WarCircle> _foodPositions;
    private double mapWidth;
    private double mapHeight;

    public AbstractWarMap(double mapWidth, double mapHeight) {
        _teamsPositions = new ArrayList<>();
        _foodPositions = new ArrayList<>();
        mapAccessibleArea = new Area(new Rectangle2D.Double(-MAP_ACCESSIBLE_AREA_PADDING, -MAP_ACCESSIBLE_AREA_PADDING, mapWidth + (MAP_ACCESSIBLE_AREA_PADDING*2.), mapHeight + (MAP_ACCESSIBLE_AREA_PADDING*2.)));
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    public ArrayList<ArrayList<WarCircle>> getTeamsPositions() {
		return new ArrayList<>(_teamsPositions);
	}
	
	/**
	 * Add all possible positions for one same team.
	 * @param coords Coordinates of possible positions for this team
	 */
	public void addTeamPositions(CoordCartesian ... coords) {
		ArrayList<WarCircle> positions = new ArrayList<WarCircle>();
		for(CoordCartesian coord : coords)
			positions.add(new WarCircle(((Double)coord.getX()).floatValue(), ((Double)coord.getY()).floatValue(), TEAM_POSITION_RADIUS));
		_teamsPositions.add(positions);
	}
	
	public ArrayList<WarCircle> getFoodPositions() {
		return new ArrayList<>(_foodPositions);
	}
	
	public void addFoodPosition(double x, double y) {
		_foodPositions.add(new WarCircle(x, y, FOOD_POSITION_RADIUS));
	}
	
	public int getMaxPlayers() {
		if (_teamsPositions.size() > 0)
			return _teamsPositions.get(0).size() * _teamsPositions.size();
		else
			return 0;
	}
	
	public int getMaxTeams() {
		return _teamsPositions.size();
	}

    public Shape getMapAccessibleArea() {
        return mapAccessibleArea;
    }

    public Shape getMapForbidArea() {
        Area forbidArea = new Area(new Rectangle2D.Double(0, 0, mapWidth, mapHeight));
        forbidArea.subtract(mapAccessibleArea);
        return forbidArea;
    }

	public double getWidth() {
		return mapWidth;
	}
	
	public double getHeight() {
		return mapHeight;
	}

    public double getCenterX() {
        return mapWidth / 2.;
    }

    public double getCenterY() {
        return mapHeight / 2.;
    }

    protected void forbidArea(Shape forbidArea) {
        mapAccessibleArea.subtract(new Area(forbidArea));
    }

    protected void forbidAllBorders() {
        forbidArea(new Rectangle2D.Double(0, 0, getWidth(), BORDER_THICKNESS));
        forbidArea(new Rectangle2D.Double(getWidth() - BORDER_THICKNESS, 0, BORDER_THICKNESS, getHeight()));
        forbidArea(new Rectangle2D.Double(0, getHeight() - BORDER_THICKNESS, getWidth(), BORDER_THICKNESS));
        forbidArea(new Rectangle2D.Double(0, 0, BORDER_THICKNESS, getHeight()));
    }
}
