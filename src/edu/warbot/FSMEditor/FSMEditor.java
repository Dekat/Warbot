package edu.warbot.FSMEditor;

import edu.warbot.FSM.WarGenericSettings.WarConditionSettings;
import edu.warbot.FSM.WarGenericSettings.WarPlanSettings;
import edu.warbot.FSMEditor.controleurs.Controleur;
import edu.warbot.FSMEditor.models.ModelCondition;
import edu.warbot.FSMEditor.models.ModelState;
import edu.warbot.FSMEditor.models.Modele;
import edu.warbot.FSMEditor.settings.EnumCondition;
import edu.warbot.FSMEditor.settings.EnumPlan;
import edu.warbot.FSMEditor.views.View;
import edu.warbot.agents.enums.WarAgentType;

public class FSMEditor {
	
	public FSMEditor() {
		Modele modele = new Modele();
		
		//Pour tester on ajoute directement des elements au model de la FSM
		modele.createModelBrain(WarAgentType.WarBase);
		modele.createModelBrain(WarAgentType.WarExplorer);
		modele.createModelBrain(WarAgentType.WarRocketLauncher);
		modele.createModelBrain(WarAgentType.WarEngineer);
		modele.createModelBrain(WarAgentType.WarTurret);
		modele.createModelBrain(WarAgentType.WarKamikaze);
		
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

		modele.getModelBrain(WarAgentType.WarExplorer).addState(sW);
		modele.getModelBrain(WarAgentType.WarExplorer).addState(sI);
		
		modele.getModelBrain(WarAgentType.WarExplorer).addCondition(cWToI);
		modele.getModelBrain(WarAgentType.WarExplorer).addCondition(cIToW);
		
		modele.getModelBrain(WarAgentType.WarBase).addState(
				new ModelState("State Idle", EnumPlan.WarPlanIdle, null));
		modele.getModelBrain(WarAgentType.WarRocketLauncher).addState(
				new ModelState("State Wiggle", EnumPlan.WarPlanWiggle, null));
		modele.getModelBrain(WarAgentType.WarEngineer).addState(
				new ModelState("State Wiggle", EnumPlan.WarPlanWiggle, null));
		modele.getModelBrain(WarAgentType.WarTurret).addState(
				new ModelState("State Idle", EnumPlan.WarPlanIdle, null));
		modele.getModelBrain(WarAgentType.WarKamikaze).addState(
				new ModelState("State Wiggle", EnumPlan.WarPlanWiggle, null));
		
		View view = new View(modele);
		
		Controleur controleur = new Controleur(modele, view);
		
//		controleur.update();
		

//		//Pour tester on ajoute directement des elements à la FSM
//		controleur.createControleursBrains(WarAgentType.WarBase);
//		controleur.createControleursBrains(WarAgentType.WarExplorer);
//		controleur.createControleursBrains(WarAgentType.WarRocketLauncher);
//		controleur.createControleursBrains(WarAgentType.WarEngineer);
//		controleur.createControleursBrains(WarAgentType.WarTurret);
//		controleur.createControleursBrains(WarAgentType.WarKamikaze);
//		
//		//Test avec un vrai comportement avec plusieurs états
//		//Pour l'explorer il est dans l'état wiggle jusqu'a la condition actionTerminate (nombre pas définit dans les parametres de l'états)
//		//L'agent va ensuite dans un état Idle jusqu'a action terminate de la meme manière
//		
//		WarPlanSettings pSW = new WarPlanSettings();
//		pSW.Value = 100;
//		ModelState sW = new ModelState("State wiggle", EnumPlan.WarPlanWiggle, pSW); //On met le meme plan au deux pas simplification mais normalement ils ont chanqun un plan
//		ModelState sI = new ModelState("State idle", EnumPlan.WarPlanIdle, pSW);
//		
//		WarConditionSettings cSWToI = new WarConditionSettings();
//		cSWToI.Time_out = 100;
//		ModelCondition cWToI = new ModelCondition("Cond_W_To_I", EnumCondition.WarConditionTimeOut, cSWToI);
//		cWToI.setSource(sW);
//		cWToI.setDestination(sI);
//		
//		ModelCondition cIToW = new ModelCondition("Cond_I_To_W", EnumCondition.WarConditionTimeOut, cSWToI);
//		cIToW.setSource(sI);
//		cIToW.setDestination(sW);
//
//		controleur.getControleurBrain(WarAgentType.WarExplorer).addState(sW);
//		controleur.getControleurBrain(WarAgentType.WarExplorer).addState(sI);
//		
//		controleur.getControleurBrain(WarAgentType.WarExplorer).addCondition(cWToI);
//		controleur.getControleurBrain(WarAgentType.WarExplorer).addCondition(cIToW);
//		
//		controleur.getControleurBrain(WarAgentType.WarBase).addState(
//				new ModelState("State Idle", EnumPlan.WarPlanIdle, null));
//		controleur.getControleurBrain(WarAgentType.WarRocketLauncher).addState(
//				new ModelState("State Wiggle", EnumPlan.WarPlanWiggle, null));
//		controleur.getControleurBrain(WarAgentType.WarEngineer).addState(
//				new ModelState("State Wiggle", EnumPlan.WarPlanWiggle, null));
//		controleur.getControleurBrain(WarAgentType.WarTurret).addState(
//				new ModelState("State Idle", EnumPlan.WarPlanIdle, null));
//		controleur.getControleurBrain(WarAgentType.WarKamikaze).addState(
//				new ModelState("State Wiggle", EnumPlan.WarPlanWiggle, null));
		
		
	}
}
