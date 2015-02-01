package edu.warbot.FSM.plan;

import java.util.ArrayList;

import edu.warbot.agents.enums.WarAgentType;

/**
 * Les attributs doivent etre public !
 * @author Olivier
 *
 */
public class WarPlanSettings {

	public ArrayList<WarAgentType> Agent_type_destination = new ArrayList<WarAgentType>();

	public ArrayList<Integer> Number_agent_destination = new ArrayList<Integer>();

	public Integer Value_reference;

	public Integer Value_pourcentage;

	public Boolean Offensif = false;

	/**
	 * IMPORTANT il faut ajouter au moins une valeur dans les colletions pour
	 * pouvoir connaitre le type générique de la collection !
	 */
	public WarPlanSettings() {
		Number_agent_destination.add(new Integer(1));

		Agent_type_destination.add(WarAgentType.WarExplorer);
	}

}
