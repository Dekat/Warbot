package edu.warbot.FSM.plan;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionAttaquer;
import edu.warbot.FSM.action.WarActionChercherBase;
import edu.warbot.FSM.condition.WarCondition;
import edu.warbot.FSM.condition.WarConditionPerceptCounter;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.adapters.WarRocketLauncherAdapter;

/**
 * @author Olivier
 */
public class WarPlanPatrouiller extends WarPlan<WarRocketLauncherAdapter> {
	
	boolean offensif;
	
	/**
	 * @param brain
	 * @param offensif = true, defensif = false
	 */
	public WarPlanPatrouiller(WarRocketLauncherAdapter brain, boolean offensif) {
		super(brain, "Plan Patrouiller");
		this.offensif = offensif;
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction<WarRocketLauncherAdapter> actionSeekBase = new WarActionChercherBase(getBrain());
		addAction(actionSeekBase);

		if(this.offensif){
			WarAction<WarRocketLauncherAdapter> actionKillEnemy = new WarActionAttaquer(getBrain(), WarAgentType.WarRocketLauncher);
			addAction(actionKillEnemy);
		
			WarCondition<WarRocketLauncherAdapter> condSeekToKill = new WarConditionPerceptCounter<WarRocketLauncherAdapter>(getBrain(), WarAgentType.WarRocketLauncher, true, ">", 1);
			actionSeekBase.addCondition(condSeekToKill);
			condSeekToKill.setDestination(actionKillEnemy);
		
			WarCondition<WarRocketLauncherAdapter> condKillToSeek = new WarConditionPerceptCounter<WarRocketLauncherAdapter>(getBrain(), WarAgentType.WarRocketLauncher, true, "==", 0);
			actionKillEnemy.addCondition(condKillToSeek);
			condKillToSeek.setDestination(actionSeekBase);
		
		}
		setFirstAction(actionSeekBase);
	}
	
}
