package edu.warbot.maps;

import edu.warbot.tools.CoordCartesian;

import java.awt.geom.Path2D;

public class OneWayWarMap extends AbstractWarMap {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;
    private static final int MIDDLE_WALL_WIDTH = 20;
    private static final int MIDDLE_WALL_DOOR_HEIGHT = 60;

    public OneWayWarMap() {
        super(new Path2D.Double(), WIDTH, HEIGHT);

        ((Path2D.Double) mapLimits).moveTo(0, 0);
        ((Path2D.Double) mapLimits).lineTo((WIDTH - MIDDLE_WALL_WIDTH) / 2, 0);
        ((Path2D.Double) mapLimits).lineTo((WIDTH - MIDDLE_WALL_WIDTH) / 2, (HEIGHT - MIDDLE_WALL_DOOR_HEIGHT) / 2);
        ((Path2D.Double) mapLimits).lineTo((WIDTH + MIDDLE_WALL_WIDTH) / 2, (HEIGHT - MIDDLE_WALL_DOOR_HEIGHT) / 2);
        ((Path2D.Double) mapLimits).lineTo((WIDTH + MIDDLE_WALL_WIDTH) / 2, 0);
        ((Path2D.Double) mapLimits).lineTo(WIDTH, 0);
        ((Path2D.Double) mapLimits).lineTo(WIDTH, HEIGHT);
        ((Path2D.Double) mapLimits).lineTo((WIDTH + MIDDLE_WALL_WIDTH) / 2, HEIGHT);
        ((Path2D.Double) mapLimits).lineTo((WIDTH + MIDDLE_WALL_WIDTH) / 2, (HEIGHT + MIDDLE_WALL_DOOR_HEIGHT) / 2);
        ((Path2D.Double) mapLimits).lineTo((WIDTH - MIDDLE_WALL_WIDTH) / 2, (HEIGHT + MIDDLE_WALL_DOOR_HEIGHT) / 2);
        ((Path2D.Double) mapLimits).lineTo((WIDTH - MIDDLE_WALL_WIDTH) / 2, HEIGHT);
        ((Path2D.Double) mapLimits).lineTo(0, HEIGHT);
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
