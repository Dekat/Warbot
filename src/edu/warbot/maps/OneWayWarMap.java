package edu.warbot.maps;

import edu.warbot.tools.CoordCartesian;

import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class OneWayWarMap extends AbstractWarMap {

    private static final double WIDTH = 1000;
    private static final double HEIGHT = 600;
    private static final double MIDDLE_WALL_WIDTH = 20;
    private static final double MIDDLE_WALL_DOOR_HEIGHT = 60;

    public OneWayWarMap() {
        super(WIDTH, HEIGHT);

        forbidAllBorders();

        forbidArea(new Rectangle2D.Double((WIDTH - MIDDLE_WALL_WIDTH) / 2., 0, MIDDLE_WALL_WIDTH, (HEIGHT - MIDDLE_WALL_DOOR_HEIGHT) / 2.));
        forbidArea(new Rectangle2D.Double((WIDTH - MIDDLE_WALL_WIDTH) / 2., ((HEIGHT - MIDDLE_WALL_DOOR_HEIGHT) / 2.) + MIDDLE_WALL_DOOR_HEIGHT, MIDDLE_WALL_WIDTH, (HEIGHT - MIDDLE_WALL_DOOR_HEIGHT) / 2.));

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
