package edu.warbot.FSM.action;

import java.util.ArrayList;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.agents.WarBase;
import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.agents.agents.WarRocketLauncher;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.brains.MovableWarAgentAdapter;

/**
 * Description de l'action
 * Si ma vie est inferieur au pourcentage passé en parametre je me heal 
 * si ma vie est supperieur au pourentage passé en parametre j'ai finit
 * Idem pour constructeur avec deux pourcentages (le deuxieme pour les unité allié)
 * Si plus de nourriure je finit
 * @author Olivier
 *
 */
public class WarActionHeal<AgentAdapterType extends MovableWarAgentAdapter> extends WarAction<AgentAdapterType>{

	boolean healAlly = true;
	
	int maxLifeUnit;
	
	int pourcentageLife;
	int life;
	
	int pourcentageLifeAlly;
	
	public WarActionHeal(AgentAdapterType brain, int  pourcentageLifeBeforHeal,
			int pourcentageVieAllyBeforHeal, boolean healAlly) {
		super(brain);
		
		this.pourcentageLife = pourcentageLifeBeforHeal;
		this.pourcentageLifeAlly = pourcentageVieAllyBeforHeal;
		this.healAlly = healAlly;
		
		this.maxLifeUnit = brain.getMaxHealth();
		this.life = this.pourcentageLife * this.maxLifeUnit;
		
	}
	
	public String executeAction(){
		
		if(getAgent().isBagEmpty()){
			return MovableWarAgent.ACTION_IDLE;
		}
		
		if(!this.healAlly){
			if(getAgent().getHealth() >= this.pourcentageLife){
				return MovableWarAgent.ACTION_IDLE;
			}else{
				return MovableWarAgent.ACTION_EAT;
			}
			
		}else{
			// Je heal mes allies
			ArrayList<WarPercept> percept = getAgent().getPerceptsAllies();
			
			if(percept.size() == 0){
				return MovableWarAgent.ACTION_MOVE;
			}
			
			for (WarPercept p : percept) {
				int maxHeath = maxHeathOfAgentType(p);
				
				if(maxHeath != -1){
					if(p.getHealth() < this.pourcentageLifeAlly * maxHeath){
						// TODO pour le give il faut mettre < ou <= ?
						if(p.getDistance() < MovableWarAgent.MAX_DISTANCE_GIVE){
							System.out.println("Je heal un allié");
							getAgent().setIdNextAgentToGive(p.getID());
							return MovableWarAgent.ACTION_GIVE;
						}else{
							getAgent().setHeading(p.getAngle());
							return MovableWarAgent.ACTION_MOVE;
						}
						
					}
				}
			}
		}
		
		return MovableWarAgent.ACTION_IDLE;
		
	}

	private int maxHeathOfAgentType(WarPercept p) {
		if(p.getType().equals(WarAgentType.WarRocketLauncher))
			return WarRocketLauncher.MAX_HEALTH;
		else if(p.getType().equals(WarAgentType.WarExplorer))
			return WarExplorer.MAX_HEALTH;
		else if(p.getType().equals(WarAgentType.WarBase))
			return WarBase.MAX_HEALTH;
		else if(p.getType().equals(WarAgentType.WarRocket)){}
		else
			System.out.println("unknow agent type " + p.getType() + " " + this.getClass());
		return -1;
	}

	@Override
	public void actionWillBegin() {
		super.actionWillBegin();
	}
}
