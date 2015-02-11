package edu.warbot.FSMEditor;

import edu.warbot.FSMEditor.controleurs.Controleur;
import edu.warbot.FSMEditor.controleurs.ControleurBrain;
import edu.warbot.FSMEditor.models.Modele;
import edu.warbot.FSMEditor.models.ModelCondition;
import edu.warbot.FSMEditor.models.ModelState;
import edu.warbot.FSMEditor.settings.ConditionEnum;
import edu.warbot.FSMEditor.settings.PlanEnum;
import edu.warbot.FSMEditor.settings.Settings;
import edu.warbot.FSMEditor.views.View;
import edu.warbot.agents.enums.WarAgentType;

public class FSMEditor {
	
	public FSMEditor() {
		Modele modele = new Modele();
		
		View view = new View(modele);
		
		Controleur controleur = new Controleur(modele, view);

		//Pour tester on ajoute directement des elements à la FSM
		controleur.createControleursBrains(WarAgentType.WarBase);
		controleur.createControleursBrains(WarAgentType.WarExplorer);
		controleur.createControleursBrains(WarAgentType.WarRocketLauncher);
		controleur.createControleursBrains(WarAgentType.WarEngineer);
		controleur.createControleursBrains(WarAgentType.WarTurret);
		controleur.createControleursBrains(WarAgentType.WarKamikaze);
		
		ModelState s1 = new ModelState("State Source", PlanEnum.WarPlanIdle, null);
		ModelState s2 = new ModelState("State Dest", PlanEnum.WarPlanIdle, null);
		
		ModelCondition c1 = new ModelCondition("Cond1", ConditionEnum.WarConditionActionTerminate, null);
		c1.setSource(s1);
		c1.setDestination(s2);

		controleur.getControleurBrain(WarAgentType.WarBase).addState(s1);
		controleur.getControleurBrain(WarAgentType.WarBase).addState(s2);
		
		controleur.getControleurBrain(WarAgentType.WarBase).addCondition(c1);
		
		controleur.getControleurBrain(WarAgentType.WarRocketLauncher).addState(
				new ModelState("State Idle", PlanEnum.WarPlanWiggle, null));
		controleur.getControleurBrain(WarAgentType.WarEngineer).addState(
				new ModelState("State Idle", PlanEnum.WarPlanWiggle, null));
		controleur.getControleurBrain(WarAgentType.WarTurret).addState(
				new ModelState("State Idle", PlanEnum.WarPlanIdle, null));
		controleur.getControleurBrain(WarAgentType.WarKamikaze).addState(
				new ModelState("State Idle", PlanEnum.WarPlanWiggle, null));
		controleur.getControleurBrain(WarAgentType.WarExplorer).addState(
				new ModelState("State Idle", PlanEnum.WarPlanWiggle, null));
		
//		ControleurBrain cb = controleur.getControleurBrain(WarAgentType.WarExplorer);

//		ModeleState state1 = new ModeleState("State1", Configuration.PLAN[1], new WarPlanSettings());
//		ModeleState state2 = new ModeleState("State2", Configuration.PLAN[0], new WarPlanSettings());
//		ModeleState state3 = new ModeleState("State3", Configuration.PLAN[0], new WarPlanSettings());
//		
//		ModeleCondition cond = new ModeleCondition("Condition1", Configuration.CONDITION[1], state1, state2);
//		ModeleCondition cond2 = new ModeleCondition("Condition2", Configuration.CONDITION[1], state1, state3);
//		
//		cb.addState(state1);
//		cb.addState(state2);
//		cb.addState(state3);
//		
//		cb.addCondition(cond);
//		cb.addCondition(cond2);
		
	}
}
