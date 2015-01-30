package edu.warbot.FSMEditor;

public class Configuration {

	public static final String FSM_CLASS_PATH = "edu.warbot.FSM";

	public static final String[] PLAN = {
			"edu.warbot.FSM.plan.WarPlanBeSecure",
			"edu.warbot.FSM.plan.WarPlanRamasserNouriture",
			"edu.warbot.FSM.plan.WarPlanAttaquer",
			"edu.warbot.FSM.plan.WarPlanCreateUnit",
			"edu.warbot.FSM.plan.WarPlanDefendre",
			"edu.warbot.FSM.plan.WarPlanHealer",
			"edu.warbot.FSM.plan.WarPlanPatrouiller" };

	public static final String[] CONDITION = { "Action_terminate",
			"Attribute_check" };

	public static final String WarConditionActionTerminate = "Action_terminate";
	public static final String WarConditionAttributCheck = "Attribute_check";

	public static final String[] ATTRIBUTES = {
			edu.warbot.FSM.condition.WarConditionAttributCheck.HEALTH,
			edu.warbot.FSM.condition.WarConditionAttributCheck.NB_ELEMEN_IN_BAG };

	public static String getSimpleName(String s) {
		return s.substring(s.lastIndexOf(".") + 1, s.length());
	}

	public static String[] getSimpleName(String[] s) {
		String res[] = new String[s.length];
		for (int i = 0; i < s.length; i++) {
			res[i] = getSimpleName(s[i]);
		}
		return res;
	}

}
