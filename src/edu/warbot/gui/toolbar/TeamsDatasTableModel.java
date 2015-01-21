package edu.warbot.gui.toolbar;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import edu.warbot.agents.enums.WarAgentCategory;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.game.Game;
import edu.warbot.game.Team;

@SuppressWarnings("serial")
public class TeamsDatasTableModel extends AbstractTableModel implements Observer {

	private static final String[] COLUMN_NAMES_FOR_TEAM_IDENTIFICATION = {"Couleur", "Logo", "Nom"};
	private static final WarAgentType[] AGENT_TYPES_TO_FOLLOW = WarAgentType.getAgentsOfCategories(WarAgentCategory.Building, WarAgentCategory.Soldier, WarAgentCategory.Worker);
	private final int _nbCols;
	
	public TeamsDatasTableModel() {
		super();
		_nbCols = COLUMN_NAMES_FOR_TEAM_IDENTIFICATION.length + AGENT_TYPES_TO_FOLLOW.length;
		
		Game.getInstance().addObserver(this);
	}
	
	public int getNbColumnsForTeamIdentification() {
		return COLUMN_NAMES_FOR_TEAM_IDENTIFICATION.length; // + 1 pour la l�gende
	}
	
	@Override
	public int getRowCount() { // Return nb columns because inverted table
		return _nbCols;
	}

	@Override
	public int getColumnCount() { // Return nb rows because inverted table
		return Game.getInstance().getPlayerTeams().size() + 1;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0)
			return getRowName(rowIndex);
		
		// TODO Trouver une fa�on plus jolie d'emp�cher l'erreur du OutOfBound
		ArrayList<Team> teams = Game.getInstance().getPlayerTeams();
		int teamPosInListTeams = columnIndex - 1;
		boolean isIndexOutOfBounds = teamPosInListTeams >= teams.size();
		if (isIndexOutOfBounds)
			return new Object();
		Team team = null;
			if (! isIndexOutOfBounds)
				team = teams.get(teamPosInListTeams);
		Object toReturn;
		switch(rowIndex) {
		case 0:
			if (isIndexOutOfBounds)
				toReturn = Color.WHITE;
			else
				toReturn = team.getColor();
			break;
		case 1:
			if (isIndexOutOfBounds)
				toReturn = new ImageIcon();
			else
				toReturn = new ImageIcon(team.getImage().getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
			break;
		case 2:
			if (isIndexOutOfBounds)
				toReturn = "";
			else
				toReturn = team.getName();
			break;
		default:
			if (isIndexOutOfBounds)
				toReturn = 0;
			else
				toReturn = team.getNbUnitsLeftOfType(AGENT_TYPES_TO_FOLLOW[rowIndex - COLUMN_NAMES_FOR_TEAM_IDENTIFICATION.length]);
			break;
		}
		
		return toReturn;
	}
	
	private String getRowName(int rowIndex) {
		if (rowIndex < COLUMN_NAMES_FOR_TEAM_IDENTIFICATION.length) {
			return COLUMN_NAMES_FOR_TEAM_IDENTIFICATION[rowIndex];
		} else {
			return AGENT_TYPES_TO_FOLLOW[rowIndex - COLUMN_NAMES_FOR_TEAM_IDENTIFICATION.length].toString();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		fireTableDataChanged();
	}

}
