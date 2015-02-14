package edu.warbot.FSM.WarGenericSettings;

import edu.warbot.agents.enums.WarAgentType;

/**
 * Les attributs doivent etre public !
 * 
 * ATTENTION : les attributs peuvent etre seulement de types : Boolean, Ineteger, String
 * Integer[], WarAgentType[], String[] (plus à venir)
 */
public class WarPlanSettings extends AbstractGenericAttributSettings{
	
	public Integer Value;

	public WarAgentType[] Agent_type;

	public Integer[] Number_agent;

	public Integer Value_reference;

	public Integer Value_pourcentage;

	public Integer Value_pourcentage_destination;

	public Boolean Offensif;
	
	public Integer Nombre_pas = 1000;
	
	public Integer Time_out = 1000;
	
}
