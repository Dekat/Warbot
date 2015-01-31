package edu.warbot.agents;

import edu.warbot.game.Team;
import edu.warbot.launcher.WarConfig;

public abstract class WarResource extends WarAgent {

	private static final String ACTION_DEFAULT = "action";

	public static final double MAX_DISTANCE_TAKE = WarConfig.getMaxDistanceTake();
	
	public WarResource(double hitboxRadius, Team team) {
		super(ACTION_DEFAULT, team, hitboxRadius);
	}

	@Override
	protected void activate() {
		super.activate();
		do {
			randomLocation();
		} while (isGoingToBeOutOfMap() || isGoingToBeOnAnOtherAgent());
	}
	
	public String action() {
		return ACTION_DEFAULT;
	}
	
}
