package edu.warbot.FSM.action;

import java.util.ArrayList;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.agents.WarBase;
import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.agents.agents.WarRocketLauncher;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.brains.MovableWarAgentBrain;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.brains.WarBaseBrain;
import edu.warbot.brains.brains.WarExplorerBrain;
import edu.warbot.brains.brains.WarRocketLauncherBrain;

/**
 * Description de l'action
 * Si ma vie est inferieur au pourcentage passé en parametre je me heal 
 * si ma vie est supperieur au pourentage passé en parametre j'ai finit
 * Idem pour constructeur avec deux pourcentages (le deuxieme pour les unité allié)
 * Si plus de nourriure je finit
 * @author Olivier
 *
 */
public class WarActionHeal extends WarAction{

	boolean healAlly = true;
	
	int maxLifeUnit;
	
	int pourcentageLife;
	int life;
	
	int pourcentageLifeAlly;
	
	public WarActionHeal(WarBrain brain, int  pourcentageLifeBeforHeal,
			int pourcentageVieAllyBeforHeal, boolean healAlly) {
		super(brain);
		
		this.pourcentageLife = pourcentageLifeBeforHeal;
		this.pourcentageLifeAlly = pourcentageVieAllyBeforHeal;
		this.healAlly = healAlly;
		
		// Recupere la vie max grace au type d'unité
		if(brain instanceof WarExplorerBrain)
			this.maxLifeUnit = (WarExplorer.MAX_HEALTH);
		else if(brain instanceof WarRocketLauncherBrain)
			this.maxLifeUnit = (WarRocketLauncher.MAX_HEALTH);
		else if(brain instanceof WarBaseBrain)
			this.maxLifeUnit = (WarBase.MAX_HEALTH);
		else
			System.out.println("Brain not know " + this.getClass());
		
		this.life = this.pourcentageLife * this.maxLifeUnit;
		
	}
	
	public String executeAction(){
		
		if(getBrain().isBagEmpty()){
			setActionTerminate(true);
			return MovableWarAgent.ACTION_IDLE;
		}
		
		if(!this.healAlly){
			if(getBrain().getHealth() >= this.pourcentageLife){
				setActionTerminate(true);
				return MovableWarAgent.ACTION_IDLE;
			}else{
				return MovableWarAgent.ACTION_EAT;
			}
			
		}else{
			// Je heal mes allies
			ArrayList<WarPercept> percept = getBrain().getPerceptsAllies();
			
			if(percept.size() == 0){
				setActionTerminate(true);
				return MovableWarAgent.ACTION_MOVE;
			}
			
			for (WarPercept p : percept) {
				int maxHeath = maxHeathOfAgentType(p);
				
				if(maxHeath != -1){
					if(p.getHealth() < this.pourcentageLifeAlly * maxHeath){
						// TODO pour le give il faut mettre < ou <= ?
						if(p.getDistance() < MovableWarAgent.MAX_DISTANCE_GIVE){
							System.out.println("Je heal un allié");
							getBrain().setIdNextAgentToGive(p.getID());
							return MovableWarAgent.ACTION_GIVE;
						}else{
							getBrain().setHeading(p.getAngle());
							return MovableWarAgent.ACTION_MOVE;
						}
						
					}
				}
			}
		}
		
		setActionTerminate(true);
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
	
	@Override
	public MovableWarAgentBrain getBrain(){
		return (MovableWarAgentBrain)(super.getBrain());
	}

}
