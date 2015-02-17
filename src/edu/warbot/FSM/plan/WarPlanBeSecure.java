package edu.warbot.FSM.plan;

import javax.swing.JOptionPane;

import edu.warbot.FSM.action.WarAction;
import edu.warbot.FSM.action.WarActionFuire;
import edu.warbot.FSMEditor.settings.GenericPlanSettings;
import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.brains.MovableWarAgentAdapter;

import javax.swing.*;

/**
 * A amélioré par exemple en disant que on fuit mais si on a plus de vie on va en chercher et on ce heal 
 */

public class WarPlanBeSecure<AgentAdapterType extends MovableWarAgentAdapter> extends WarPlan<AgentAdapterType> {
	
	public WarPlanBeSecure(AgentAdapterType brain, GenericPlanSettings planSettings) {
		super("PlanBeSecure", brain, planSettings);
		JOptionPane.showMessageDialog(null, "Attention le plan BeSecure n'est pas terminé et risque de ne pas fonctionner", "Waring not terminated plan", JOptionPane.INFORMATION_MESSAGE);
	}

	public void buildActionList() {
		
		setPrintTrace(true);
		
		WarAction<AgentAdapterType> actionFuire = new WarActionFuire<>(getBrain(), WarExplorer.MAX_HEALTH, 100);
		addAction(actionFuire);

//		WarAction<AgentAdapterType> actionHeal = new WarActionHealMe<>(getBrain());
//		addAction(actionHeal);
//		
//		WarAction<AgentAdapterType> actionFood = new WarActionChercherNouriture<>(getBrain());
//		addAction(actionFood);
		
//		WarCondition<AgentAdapterType> cond = new WarConditionActionTerminate(getBrain(), actionDontMove);
//		actionDontMove.addCondition(condFinitDontMove);
//		condFinitDontMove.setDestination(actionDontMove);
		
		setFirstAction(actionFuire);
	}
	
}
