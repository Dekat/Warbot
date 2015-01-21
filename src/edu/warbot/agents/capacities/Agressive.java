package edu.warbot.agents.capacities;

public interface Agressive {

	public static String ACTION_FIRE = "fire";
	public static String ACTION_RELOAD = "beginReloadWeapon";
	
	public String fire();
	public String beginReloadWeapon();
	public boolean isReloaded();
	public boolean isReloading();
}
