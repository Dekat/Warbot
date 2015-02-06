package edu.warbot.FSM.plan;

import edu.warbot.agents.enums.WarAgentType;

/**
 * Les attributs doivent etre public !
 * @author Olivier
 * 
 * ATTENTION : les attributs peuvent etre seulement de types : Boolean, Ineteger, String
 * Integer[], WarAgentType[], String[] (plus à venir)

 */
public class WarPlanSettings {

	public WarAgentType[] Agent_type_destination;

	public Integer[] Number_agent_destination;

	public Integer Value_reference;

	public Integer Value_pourcentage;
	//Notament utilisé pour le poucentage de vie d'un allié pour le soigner
	public Integer Value_pourcentage_destination;

	public Boolean Offensif = true;
	
	public String Debug_String = "debug_sring";

}
