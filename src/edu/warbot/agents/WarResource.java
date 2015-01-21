package edu.warbot.agents;

import edu.warbot.game.Game;
import edu.warbot.launcher.WarConfig;

@SuppressWarnings("serial")
public abstract class WarResource extends WarAgent {

	private static final String ACTION_DEFAULT = "action";

	public static final double MAX_DISTANCE_TAKE = WarConfig.getMaxDistanceTake();
	
	public WarResource(double hitboxRadius) {
		super(ACTION_DEFAULT, Game.getInstance().getMotherNatureTeam(), hitboxRadius);
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
