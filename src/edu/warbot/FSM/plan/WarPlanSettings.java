package edu.warbot.FSM.plan;

import java.util.ArrayList;

import edu.warbot.agents.enums.WarAgentType;

/**
 * Les attributs doivent etre public !
 * @author Olivier
 * 
 * ATTENTION : les attributs peuvent etre seulement de types : Boolean, Ineteger, String
 * Integer[]
 * ArrayList<WarAgentType>, ArrayList<Integer>, ArrayList<String> (plus à venir)
 * ATTENTION : les collections doivent obligatoirment être initialisé et contenir au moins un élément
 * (afin de pouvoir connaitre son type générique lors de l'introspection, sinon �a serait imposible � cause de l'effacement de type Java)
 * 
 */
public class WarPlanSettings {

	public ArrayList<WarAgentType> Agent_type_destination = new ArrayList<WarAgentType>();

	public Integer[] Number_agent_destination_Tab;

	public ArrayList<Integer> Number_agent_destination = new ArrayList<Integer>();

	public Integer Value_reference;

	public Integer Value_pourcentage;
	//Notament utilisé pour le poucentage de vie d'un allié pour le soigner
	public Integer Value_pourcentage_destination;

	public Boolean Offensif = true;
	
	public String Debug_String = "debug_sring";

	/**
	 * IMPORTANT il faut ajouter au moins une valeur dans les colletions pour
	 * pouvoir connaitre le type générique de la collection !
	 */
	public WarPlanSettings() {
		Number_agent_destination.add(new Integer(1));

		Agent_type_destination.add(WarAgentType.WarExplorer);
		
	}

}
