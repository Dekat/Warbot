package edu.warbot.agents;

import edu.warbot.game.Team;
import edu.warbot.launcher.WarConfig;

import java.awt.*;

public abstract class WarResource extends WarAgent {

	private static final String ACTION_DEFAULT = "action";

	public static final double MAX_DISTANCE_TAKE = WarConfig.getMaxDistanceTake();
	
	public WarResource(Shape hitbox, Team team) {
		super(ACTION_DEFAULT, team, hitbox);
	}

	@Override
	protected void activate() {
		super.activate();
		do {
			randomLocation();
		} while (isGoingToBeOutOfMap() || isGoingToBeOverAnOtherAgent());
	}
	
	public String action() {
		return ACTION_DEFAULT;
	}
	
}
