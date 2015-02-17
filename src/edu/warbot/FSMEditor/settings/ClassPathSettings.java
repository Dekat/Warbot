package edu.warbot.FSMEditor.settings;

public class ClassPathSettings {

	public static final String FSM_CLASS_PATH = "edu.warbot.FSM.";

	public static final String FSM_CLASS_PATH_ACTION = FSM_CLASS_PATH + "action.";
	public static final String FSM_CLASS_PATH_CONDITION = FSM_CLASS_PATH + "condition.";
	public static final String FSM_CLASS_PATH_PLAN = FSM_CLASS_PATH + "plan.";
	public static final String FSM_CLASS_PATH_REFELEXE = FSM_CLASS_PATH + "reflexe.";

	public static String getFullNameOf(EnumPlan plan) {
		return FSM_CLASS_PATH_PLAN + plan;
	}
	
	public static String getFullNameOf(EnumCondition condition) {
		return FSM_CLASS_PATH_CONDITION + condition;
	}
	
	public static String getFullNameOf(EnumReflexe reflexe) {
		return FSM_CLASS_PATH_REFELEXE + reflexe;
	}
	
	public static String getFullNameOf(EnumAction action) {
		return FSM_CLASS_PATH_ACTION + action;
	}

}
