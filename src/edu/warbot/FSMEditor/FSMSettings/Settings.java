package edu.warbot.FSMEditor.FSMSettings;

public class Settings {

	public static final String FSM_CLASS_PATH = "edu.warbot.FSM.";

	public static final String FSM_CLASS_PATH_ACTION = FSM_CLASS_PATH + "action.";
	public static final String FSM_CLASS_PATH_CONDITION = FSM_CLASS_PATH + "condition.";
	public static final String FSM_CLASS_PATH_PLAN = FSM_CLASS_PATH + "plan.";
	public static final String FSM_CLASS_PATH_REFELEXE = FSM_CLASS_PATH + "reflexe.";

	//Normalement pour l'instant pas besoin
	//Si dans le future l'editeur permet d'éditer des plans on pourra créer aussi une enum
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

	//Je sais pas encore si il faut faire unen enum et comment !
	public static final String[] ATTRIBUTES = {
			edu.warbot.FSM.condition.WarConditionAttributCheck.HEALTH,
			edu.warbot.FSM.condition.WarConditionAttributCheck.NB_ELEMEN_IN_BAG };

	public static String getFullNameOf(PlanEnum warPlanName) {
		return FSM_CLASS_PATH_PLAN + warPlanName;
	}
	
	public static String getFullNameOf(ConditionEnum warPlanName) {
		return FSM_CLASS_PATH_CONDITION + warPlanName;
	}
	
	public static String getFullNameOf(ReflexeEnum warPlanName) {
		return FSM_CLASS_PATH_REFELEXE + warPlanName;
	}

}
