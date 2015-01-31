package edu.warbot.maps;

import java.awt.Dimension;
import java.util.ArrayList;

import edu.warbot.tools.CoordCartesian;
import edu.warbot.tools.WarCircle;

public abstract class AbstractWarMap {
	
	protected static final float TEAM_POSITION_RADIUS = 100;
	protected static final float FOOD_POSITION_RADIUS = 200;
	
//	public static final String MAP_DIRECTORY = "maps" + File.separatorChar; 
//	public static final String MAP_EXTENSION = ".warmap"; 
	
	private Dimension _size;
	private ArrayList<ArrayList<WarCircle>> _teamsPositions;
	private ArrayList<WarCircle> _foodPositions;
	
	public AbstractWarMap(Dimension size) {
		_teamsPositions = new ArrayList<>();
		_foodPositions = new ArrayList<>();
		_size = size;
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
	
	public Dimension getSize() {
		return _size;
	}
	
	public int getWidth() {
		return _size.width;
	}
	
	public int getHeight() {
		return _size.height;
	}
}
