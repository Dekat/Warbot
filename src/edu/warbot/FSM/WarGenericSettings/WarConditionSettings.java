package edu.warbot.FSM.WarGenericSettings;

import java.net.Inet4Address;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.agents.enums.WarAgentType;


/**
 * Les attributs doivent etre public !
 * 
 * ATTENTION : les attributs peuvent etre seulement de types : Boolean, Ineteger, String
 * Integer[], WarAgentType[], String[] (plus à venir)
 */
public class WarConditionSettings extends AbstractGenericAttributSettings{
	
	public String Operateur;
	
	public Boolean Est_pourcentage;

	public Integer Pourcentage;

	public WarAction Action;

	public String Attribut_name;

	public Integer Reference;

	public String Message;

	public WarAgentType Agent_type;

	public boolean Enemie;

	public boolean Offensif;

	public Integer Valeur;


}
