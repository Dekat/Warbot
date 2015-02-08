package edu.warbot.maps;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

import edu.warbot.tools.CoordCartesian;

public class DefaultWarMap extends AbstractWarMap {

	public DefaultWarMap() {
		super(new Rectangle2D.Float(0, 0, 1000, 600));
		
		addTeamPositions(
				new CoordCartesian(TEAM_POSITION_RADIUS, TEAM_POSITION_RADIUS),
				new CoordCartesian(TEAM_POSITION_RADIUS, getBoundsHeight() / 2),
				new CoordCartesian(TEAM_POSITION_RADIUS, getBoundsHeight() - TEAM_POSITION_RADIUS)
				);
		addTeamPositions(
				new CoordCartesian(getBoundsWidth() - TEAM_POSITION_RADIUS, TEAM_POSITION_RADIUS),
				new CoordCartesian(getBoundsWidth() - TEAM_POSITION_RADIUS, getBoundsHeight() / 2),
				new CoordCartesian(getBoundsWidth() - TEAM_POSITION_RADIUS, getBoundsHeight() - TEAM_POSITION_RADIUS)
				);
		
		addFoodPosition(getBoundsWidth() - FOOD_POSITION_RADIUS, FOOD_POSITION_RADIUS);
		addFoodPosition(FOOD_POSITION_RADIUS, getBoundsHeight() - FOOD_POSITION_RADIUS);
		addFoodPosition(getBoundsWidth() / 2, getBoundsHeight() / 2);
		addFoodPosition(getBoundsWidth() / 2, getBoundsHeight() / 2);
	}

}
