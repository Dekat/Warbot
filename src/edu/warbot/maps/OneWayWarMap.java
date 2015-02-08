package edu.warbot.maps;

import edu.warbot.tools.CoordCartesian;

import java.awt.geom.Path2D;

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
