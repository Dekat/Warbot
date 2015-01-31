package edu.warbot.maps;

import java.awt.Dimension;

import edu.warbot.tools.CoordCartesian;

public class DefaultWarMap extends AbstractWarMap {

	public DefaultWarMap() {
		super(new Dimension(1000, 600));
		
		// left side
		addTeamPositions(
				new CoordCartesian(TEAM_POSITION_RADIUS, TEAM_POSITION_RADIUS),
				new CoordCartesian(TEAM_POSITION_RADIUS, getHeight() / 2),
				new CoordCartesian(TEAM_POSITION_RADIUS, getHeight() - TEAM_POSITION_RADIUS)
				);
		addTeamPositions(
				new CoordCartesian(getWidth() - TEAM_POSITION_RADIUS, TEAM_POSITION_RADIUS),
				new CoordCartesian(getWidth() - TEAM_POSITION_RADIUS, getHeight() / 2),
				new CoordCartesian(getWidth() - TEAM_POSITION_RADIUS, getHeight() - TEAM_POSITION_RADIUS)
				);
		
		addFoodPosition(getWidth() - FOOD_POSITION_RADIUS, FOOD_POSITION_RADIUS);
		addFoodPosition(FOOD_POSITION_RADIUS, getHeight() - FOOD_POSITION_RADIUS);
		addFoodPosition(getWidth() / 2, getHeight() / 2);
		addFoodPosition(getWidth() / 2, getHeight() / 2);
	}

}
