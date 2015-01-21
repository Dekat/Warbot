package edu.warbot.maps;

import java.awt.Dimension;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import edu.warbot.tools.WarCircle;

@SuppressWarnings("serial")
public class WarMap implements Serializable {
	
	public static final String MAP_DIRECTORY = "maps" + File.separatorChar; 
	public static final String MAP_EXTENSION = ".warmap"; 
	
	private Dimension _size;
	private ArrayList<WarCircle> _teamsPositions;
	private ArrayList<WarCircle> _foodPositions;
	
	public WarMap(Dimension size) {
		_teamsPositions = new ArrayList<>();
		_foodPositions = new ArrayList<>();
		_size = size;
	}
	
	public ArrayList<WarCircle> getTeamsPositions() {
		return new ArrayList<>(_teamsPositions);
	}
	
	public void addTeamPosition(WarCircle pos) {
		_teamsPositions.add(pos);
	}
	
	public ArrayList<WarCircle> getFoodPositions() {
		return new ArrayList<>(_foodPositions);
	}
	
	public void addFoodPosition(WarCircle pos) {
		_foodPositions.add(pos);
	}
	
	public int getMaxPlayers() {
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
