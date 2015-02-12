package edu.warbot.FSMEditor;

import edu.warbot.FSM.WarGenericSettings.WarConditionSettings;
import edu.warbot.FSM.WarGenericSettings.WarPlanSettings;
import edu.warbot.FSMEditor.controleurs.Controleur;
import edu.warbot.FSMEditor.controleurs.ControleurBrain;
import edu.warbot.FSMEditor.models.Modele;
import edu.warbot.FSMEditor.models.ModelCondition;
import edu.warbot.FSMEditor.models.ModelState;
import edu.warbot.FSMEditor.settings.EnumAction;
import edu.warbot.FSMEditor.settings.EnumCondition;
import edu.warbot.FSMEditor.settings.EnumPlan;
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
		
		//Test avec un vrai comportement avec plusieurs états
		//Pour l'explorer il est dans l'état wiggle jusqu'a la condition actionTerminate (nombre pas définit dans les parametres de l'états)
		//L'agent va ensuite dans un état Idle jusqu'a action terminate de la meme manière
		
		WarPlanSettings pSW = new WarPlanSettings();
		pSW.Value = 100;
		ModelState sW = new ModelState("State wiggle", EnumPlan.WarPlanWiggle, pSW); //On met le meme plan au deux pas simplification mais normalement ils ont chanqun un plan
		ModelState sI = new ModelState("State idle", EnumPlan.WarPlanIdle, pSW);
		
		WarConditionSettings cSWToI = new WarConditionSettings();
		cSWToI.Time_out = 100;
		ModelCondition cWToI = new ModelCondition("Cond_W_To_I", EnumCondition.WarConditionTimeOut, cSWToI);
		cWToI.setSource(sW);
		cWToI.setDestination(sI);
		
		ModelCondition cIToW = new ModelCondition("Cond_I_To_W", EnumCondition.WarConditionTimeOut, cSWToI);
		cIToW.setSource(sI);
		cIToW.setDestination(sW);

		controleur.getControleurBrain(WarAgentType.WarBase).addState(sW);
		controleur.getControleurBrain(WarAgentType.WarBase).addState(sI);
		
		controleur.getControleurBrain(WarAgentType.WarBase).addCondition(cWToI);
		
		controleur.getControleurBrain(WarAgentType.WarRocketLauncher).addState(
				new ModelState("State Idle", EnumPlan.WarPlanWiggle, null));
		controleur.getControleurBrain(WarAgentType.WarEngineer).addState(
				new ModelState("State Idle", EnumPlan.WarPlanWiggle, null));
		controleur.getControleurBrain(WarAgentType.WarTurret).addState(
				new ModelState("State Idle", EnumPlan.WarPlanIdle, null));
		controleur.getControleurBrain(WarAgentType.WarKamikaze).addState(
				new ModelState("State Idle", EnumPlan.WarPlanWiggle, null));
		controleur.getControleurBrain(WarAgentType.WarExplorer).addState(
				new ModelState("State Idle", EnumPlan.WarPlanWiggle, null));
		
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
