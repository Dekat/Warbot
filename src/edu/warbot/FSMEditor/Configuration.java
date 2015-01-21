package edu.warbot.FSMEditor;


public class Configuration {
	
	public static final String[] PLAN = { "WarPlanBeSecure", "WarPlanRamasserNouriture" };
	
	public static final String[] CONDITION = { "Action_terminate", "Attribute_check" };
	
	public static final String WarConditionActionTerminate = "Action_terminate";
	public static final String WarConditionAttributCheck = "Attribute_check";
	
	public static final String[] ATTRIBUTES = { edu.warbot.FSM.condition.WarConditionAttributCheck.HEALTH,
		edu.warbot.FSM.condition.WarConditionAttributCheck.NB_ELEMEN_IN_BAG
	};	

}
