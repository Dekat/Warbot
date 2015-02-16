package edu.warbot.FSM.WarGenericSettings;

import edu.warbot.FSMEditor.settings.EnumMessage;
import edu.warbot.FSMEditor.settings.EnumMethod;
import edu.warbot.agents.enums.WarAgentType;

/**
 * Les attributs doivent etre public !
 * 
 * ATTENTION : les attributs peuvent etre de types : Boolean, Integer, String, EnumAction,
 * WarAgentType, EnumMethod, EnumOperand
 */
public class PlanSettings extends AbstractGenericAttributSettings{

	public WarAgentType Agent_type;
	
	public Integer Number_agent;

	public Integer Min_life;
	
	public EnumMessage Message;

	public Boolean Pourcentage;

	public Boolean Offensif;
	
	public Integer Tik_number = 500;
	
}
