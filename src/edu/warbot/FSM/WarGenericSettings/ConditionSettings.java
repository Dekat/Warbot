package edu.warbot.FSM.WarGenericSettings;

import edu.warbot.FSMEditor.settings.EnumMessage;
import edu.warbot.FSMEditor.settings.EnumMethod;
import edu.warbot.FSMEditor.settings.EnumOperand;
import edu.warbot.agents.enums.WarAgentType;

/**
 * Les attributs doivent etre public !
 * 
 * ATTENTION : les attributs peuvent etre de types : Boolean, Integer, String, EnumAction,
 * WarAgentType, EnumMethod, EnumOperand
 */
public class ConditionSettings extends AbstractGenericAttributSettings{
	
	public EnumMethod Methode;

	public EnumOperand Operateur;
	
	public Integer Reference;

	public Boolean Pourcentage;

	public EnumMessage Message;

	public WarAgentType Agent_type;

	public Boolean Enemie = false;

	public Boolean Offensif = false;

	public Integer Tik_number = 500;





}
