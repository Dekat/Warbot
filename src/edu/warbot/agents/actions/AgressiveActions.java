package edu.warbot.agents.actions;

public interface AgressiveActions {

	public static String ACTION_FIRE = "fire";
	public static String ACTION_RELOAD = "beginReloadWeapon";
	
	public String fire();
	public String beginReloadWeapon();
}
