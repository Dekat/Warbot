package edu.warbot.FSM.WarGenericSettings;

import edu.warbot.FSMEditor.settings.EnumAction;
import edu.warbot.agents.enums.WarAgentType;


/**
 * Les attributs doivent etre public !
 * 
 * ATTENTION : les attributs peuvent etre seulement de types : Boolean, Ineteger, String, EnumAction
 * Integer[], WarAgentType[], String[] (plus à venir)
 */
public class WarConditionSettings extends AbstractGenericAttributSettings{
	
	public Integer Valeur;

	public String Operateur;
	
	public Integer Reference;

	public Integer Pourcentage;

	public Boolean Est_pourcentage;

	public EnumAction Action;

	public String Attribut_name;

	public String Message;

	public WarAgentType Agent_type;

	public Boolean Enemie = false;

	public Boolean Offensif = false;

	public Integer Time_out = 1000;



}
