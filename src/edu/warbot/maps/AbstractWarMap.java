package edu.warbot.maps;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import edu.warbot.launcher.WarConfig;
import edu.warbot.tools.CoordCartesian;
import edu.warbot.tools.GeometryTools;
import edu.warbot.tools.WarCircle;

public abstract class AbstractWarMap {

    public static final int MAP_MARGIN = 10;
	protected static final float TEAM_POSITION_RADIUS = 100;
	protected static final float FOOD_POSITION_RADIUS = 200;
	
	protected Shape mapLimits;
	private ArrayList<ArrayList<WarCircle>> _teamsPositions;
	private ArrayList<WarCircle> _foodPositions;
    private float mapWidth;
    private float mapHeight;

	public AbstractWarMap(Shape mapLimits) {
		this(mapLimits, ((Double) mapLimits.getBounds2D().getWidth()).floatValue(), ((Double) mapLimits.getBounds2D().getHeight()).floatValue());
	}

    public AbstractWarMap(Shape mapLimits, float mapWidth, float mapHeight) {
        _teamsPositions = new ArrayList<>();
        _foodPositions = new ArrayList<>();
        this.mapLimits = mapLimits;
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
	
	public void addFoodPosition(float x, float y) {
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

    public Shape getMapLimits() {
        return GeometryTools.translateShape(mapLimits, MAP_MARGIN / 2, MAP_MARGIN / 2);
    }

	public float getWidth() {
		return mapWidth;
	}
	
	public float getHeight() {
		return mapHeight;
	}

    public double getCenterX() {
        return mapWidth / 2.;
    }

    public double getCenterY() {
        return mapHeight / 2.;
    }
}
