package edu.warbot.FSM.plan;

import java.util.ArrayList;

import edu.warbot.agents.enums.WarAgentType;

/**
 * Les attributs doivent etre public !
 * @author Olivier
 * 
 * ATTENTION : les attributs peuvent etre seulement de types : Boolean, Ineteger, 
 * ArrayList<WarAgentType>, ArrayList<Integer>, String, ArrayList<String> (plus � venir)
 * ATTENTION : les collections doivent obligatoirment etre initialis� et contenir au moins un �l�ment
 * (afin de pouvoir connaitre son type g�n�rique lors de l'introspection, sinon �a serait imposible � cause de l'effacement de type)
 * 
 */
public class WarPlanSettings {

	public ArrayList<WarAgentType> Agent_type_destination = new ArrayList<WarAgentType>();

	public ArrayList<Integer> Number_agent_destination = new ArrayList<Integer>();

	public Integer Value_reference;

	public Integer Value_pourcentage;

	public Boolean Offensif = false;
	
	public String Debug_String = "debug_sring";

	/**
	 * IMPORTANT il faut ajouter au moins une valeur dans les colletions pour
	 * pouvoir connaitre le type g�n�rique de la collection !
	 */
	public WarPlanSettings() {
		Number_agent_destination.add(new Integer(1));

		Agent_type_destination.add(WarAgentType.WarExplorer);
	}

}
