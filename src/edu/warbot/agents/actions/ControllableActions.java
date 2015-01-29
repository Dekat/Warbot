package edu.warbot.agents.actions;

import edu.warbot.launcher.WarConfig;

public interface ControllableActions {

	public static final double MAX_DISTANCE_GIVE = WarConfig.getMaxDistanceGive();
	public static final String ACTION_GIVE = "give";
	public static final String ACTION_EAT = "eat";
	public static final String ACTION_IDLE = "idle";

	public String give();
	public String eat();
	public String idle();
}
