package edu.warbot.maps;

import edu.warbot.tools.CoordCartesian;

import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class OneWayWarMap extends AbstractWarMap {

	public OneWayWarMap() {
        super(new Path2D.Double());

        int width = 1000;
        int height = 600;
        int middleWallWidth = 20;
        int middleWallDoorHeight = 60;
        ((Path2D.Double) mapLimits).moveTo(0, 0);
        ((Path2D.Double) mapLimits).lineTo((width - middleWallWidth) / 2, 0);
        ((Path2D.Double) mapLimits).lineTo((width - middleWallWidth) / 2, (height - middleWallDoorHeight) / 2);
        ((Path2D.Double) mapLimits).lineTo((width + middleWallWidth) / 2, (height - middleWallDoorHeight) / 2);
        ((Path2D.Double) mapLimits).lineTo((width + middleWallWidth) / 2, 0);
        ((Path2D.Double) mapLimits).lineTo(width, 0);
        ((Path2D.Double) mapLimits).lineTo(width, height);
        ((Path2D.Double) mapLimits).lineTo((width + middleWallWidth) / 2, height);
        ((Path2D.Double) mapLimits).lineTo((width + middleWallWidth) / 2, (height + middleWallDoorHeight) / 2);
        ((Path2D.Double) mapLimits).lineTo((width - middleWallWidth) / 2, (height + middleWallDoorHeight) / 2);
        ((Path2D.Double) mapLimits).lineTo((width - middleWallWidth) / 2, height);
        ((Path2D.Double) mapLimits).lineTo(0, height);
        ((Path2D.Double) mapLimits).lineTo(0, 0);

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
