package edu.warbot.FSMEditor;

public class Configuration {

	public static final String FSM_CLASS_PATH = "edu.warbot.FSM";

	public static final String FSM_CLASS_PATH_ACTION = FSM_CLASS_PATH + ".action";
	public static final String FSM_CLASS_PATH_CONDITION = FSM_CLASS_PATH + ".condition";
	public static final String FSM_CLASS_PATH_PLAN = FSM_CLASS_PATH + ".plan";
	public static final String FSM_CLASS_PATH_REFELEXE = FSM_CLASS_PATH + ".reflexe";

	public static final String[] PLAN = {
			FSM_CLASS_PATH_PLAN + ".WarPlanBeSecure",
			FSM_CLASS_PATH_PLAN + ".WarPlanRamasserNouriture",
			FSM_CLASS_PATH_PLAN + ".WarPlanAttaquer",
			FSM_CLASS_PATH_PLAN + ".WarPlanCreateUnit",
			FSM_CLASS_PATH_PLAN + ".WarPlanDefendre",
			FSM_CLASS_PATH_PLAN + ".WarPlanHealer",
			FSM_CLASS_PATH_PLAN + ".WarPlanPatrouiller" };

	public static final String[] CONDITION = { 
		FSM_CLASS_PATH_CONDITION + ".WarConditionActionTerminate",
		FSM_CLASS_PATH_CONDITION + ".WarConditionAttributCheck",
		FSM_CLASS_PATH_CONDITION + ".WarConditionMessageChecker",
		FSM_CLASS_PATH_CONDITION + ".WarConditionPerceptAttributCheck",
		FSM_CLASS_PATH_CONDITION + ".WarConditionPerceptCounter"
	};
	
	public static final String[] REFLEXE = { 
		FSM_CLASS_PATH_REFELEXE + ".WarReflexeAnswerMessage",
		FSM_CLASS_PATH_REFELEXE + ".WarReflexeWarnWithCondition"
	};
	public static final String[] ACTION = { 
		FSM_CLASS_PATH_ACTION + ".WarActionAttaquer",
		FSM_CLASS_PATH_ACTION + ".WarActionChercherBase",
		FSM_CLASS_PATH_ACTION + ".WarActionChercherNouriture",
		FSM_CLASS_PATH_ACTION + ".WarActionCreateUnit",
		FSM_CLASS_PATH_ACTION + ".WarActionDefendre",
		FSM_CLASS_PATH_ACTION + ".WarActionDontMove",
		FSM_CLASS_PATH_ACTION + ".WarActionFuire",
		FSM_CLASS_PATH_ACTION + ".WarActionHeal",
		FSM_CLASS_PATH_ACTION + ".WarActionRaporterNouriture",
		FSM_CLASS_PATH_ACTION + ".WarActionWiggle"
	};

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
