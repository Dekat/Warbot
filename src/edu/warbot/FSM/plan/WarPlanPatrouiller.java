package edu.warbot.FSM.plan;

import edu.warbot.FSM.WarGenericSettings.WarConditionSettings;
import edu.warbot.FSM.WarGenericSettings.WarPlanSettings;
import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionAttaquer;
import edu.warbot.FSM.action.WarActionChercherBase;
import edu.warbot.FSM.condition.WarCondition;
import edu.warbot.FSM.condition.WarConditionBooleanCheck;
import edu.warbot.FSM.condition.WarConditionPerceptCounter;
import edu.warbot.FSMEditor.settings.EnumOperand;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.adapters.WarRocketLauncherAdapter;

/**
 * Pattrouiller de manière deffensive va chercher la base enemie
 * Pattrouiller de manière offensive va chercher la base enemie et attaquer les enemies
 * que l'agent croise en chemin si il est en position de force
 * (Etre en position de force siginifie que le nombre d'allié/enemie aux allentour est avantageux
 * @author Olivier
 */
public class WarPlanPatrouiller extends WarPlan<WarRocketLauncherAdapter> {
	
	boolean offensif;
	
	/**
	 * @param brain
	 * @param offensif = true, defensif = false
	 */
	//boolean offensif
	public WarPlanPatrouiller(WarRocketLauncherAdapter brain, WarPlanSettings planSettings) {
		super("Plan Patrouiller", brain, planSettings);
		this.offensif = getPlanSettings().Offensif;
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction<WarRocketLauncherAdapter> actionSeekBase = new WarActionChercherBase(getBrain());
		addAction(actionSeekBase);

		if(this.offensif){
			WarAction<WarRocketLauncherAdapter> actionKillEnemy = new WarActionAttaquer(getBrain(), WarAgentType.WarRocketLauncher);
			addAction(actionKillEnemy);
		
			WarConditionSettings condSet1 = new WarConditionSettings();
			condSet1.Agent_type = WarAgentType.WarRocketLauncher;
			condSet1.Pourcentage = true;
			condSet1.Operateur = EnumOperand.sup;
			condSet1.Reference = 1;
			WarCondition<WarRocketLauncherAdapter> condSeekToKill = 
					new WarConditionPerceptCounter<WarRocketLauncherAdapter>("cond_kill", getBrain(), condSet1);
			actionSeekBase.addCondition(condSeekToKill);
			condSeekToKill.setDestination(actionKillEnemy);
		
			WarConditionSettings condSet2 = new WarConditionSettings();
			condSet2.Agent_type = WarAgentType.WarRocketLauncher;
			condSet2.Pourcentage = true;
			condSet2.Operateur = EnumOperand.eg;
			condSet2.Reference = 0;
			WarCondition<WarRocketLauncherAdapter> condKillToSeek = 
					new WarConditionPerceptCounter<WarRocketLauncherAdapter>("cond_seek", getBrain(), condSet2);
			actionKillEnemy.addCondition(condKillToSeek);
			condKillToSeek.setDestination(actionSeekBase);
		
		}
		setFirstAction(actionSeekBase);
	}
	
}
