package edu.warbot.FSMEditor;

import edu.warbot.FSM.plan.WarPlanSettings;
import edu.warbot.FSMEditor.Controleurs.Controleur;
import edu.warbot.FSMEditor.Controleurs.ControleurBrain;
import edu.warbot.FSMEditor.Modeles.Modele;
import edu.warbot.FSMEditor.Modeles.ModeleCondition;
import edu.warbot.FSMEditor.Modeles.ModeleState;
import edu.warbot.FSMEditor.Views.View;
import edu.warbot.agents.enums.WarAgentType;

public class FSMEditor {
	
	public FSMEditor() {
		Modele modele = new Modele();
		
		View view = new View(modele);
		
		Controleur controleur = new Controleur(modele, view);

		//Pour tester on ajoute directement des elements � la FSM
		controleur.createControleursBrains(WarAgentType.WarBase);
		controleur.createControleursBrains(WarAgentType.WarExplorer);
		controleur.createControleursBrains(WarAgentType.WarRocketLauncher);
		controleur.createControleursBrains(WarAgentType.WarEngineer);
		controleur.createControleursBrains(WarAgentType.WarTurret);
		controleur.createControleursBrains(WarAgentType.WarKamikaze);
		
		controleur.getControleurBrain(WarAgentType.WarBase).addState(
				new ModeleState("State Idle", Configuration.PLAN[6], null));
		controleur.getControleurBrain(WarAgentType.WarRocketLauncher).addState(
				new ModeleState("State Idle", Configuration.PLAN[6], null));
		controleur.getControleurBrain(WarAgentType.WarEngineer).addState(
				new ModeleState("State Idle", Configuration.PLAN[6], null));
		controleur.getControleurBrain(WarAgentType.WarTurret).addState(
				new ModeleState("State Idle", Configuration.PLAN[6], null));
		controleur.getControleurBrain(WarAgentType.WarKamikaze).addState(
				new ModeleState("State Idle", Configuration.PLAN[6], null));
		controleur.getControleurBrain(WarAgentType.WarExplorer).addState(
				new ModeleState("State Idle", Configuration.PLAN[5], null));
		
		ControleurBrain cb = controleur.getControleurBrain(WarAgentType.WarExplorer);

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
		
//		cb.addCondition(cond);
//		cb.addCondition(cond2);
		
	}
}
