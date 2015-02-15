package edu.warbot.agents.actions;


public interface BuilderActions {

	public static String ACTION_BUILD = "build";
    public static String ACTION_REPAIR = "repair";

	public String build();
    public String repair();
}
