package edu.warbot.FSMEditor;

import edu.warbot.FSMEditor.controleurs.Controleur;
import edu.warbot.FSMEditor.models.Model;
import edu.warbot.FSMEditor.models.ModelState;
import edu.warbot.FSMEditor.settings.EnumPlan;
import edu.warbot.FSMEditor.settings.GenericPlanSettings;
import edu.warbot.FSMEditor.views.View;
import edu.warbot.agents.enums.WarAgentType;

public class FSMEditor {
	
	public FSMEditor() {
		Model model = new Model();
		
		createBasicFSM(model);
		
//		model.getModelBrain(WarAgentType.WarExplorer).addState(
//				new ModelState("State Ramasser nourriture", EnumPlan.WarPlanRamasserNouriture, null));
//		
		/*
		WarPlanSettings pSW = new WarPlanSettings();
		pSW.Value = 100;
		ModelState sW = new ModelState("State wiggle", EnumPlan.WarPlanWiggle, pSW); //On met le meme plan au deux pas simplification mais normalement ils ont chanqun un plan
		ModelState sI = new ModelState("State idle", EnumPlan.WarPlanIdle, pSW);
		
		WarConditionSettings cSWToI = new WarConditionSettings();
		cSWToI.Time_out = 100;
		ModelCondition cWToI = new ModelCondition("Cond_W_To_I", EnumCondition.WarConditionTimeOut, cSWToI);
		sW.addConditionOut(cWToI);
		cWToI.setDestination(sI);
		
		ModelCondition cIToW = new ModelCondition("Cond_I_To_W", EnumCondition.WarConditionTimeOut, cSWToI);
		sI.addConditionOut(cIToW);
		cIToW.setDestination(sW);

		model.getModelBrain(WarAgentType.WarExplorer).addState(sW);
		model.getModelBrain(WarAgentType.WarExplorer).addState(sI);
		
		model.getModelBrain(WarAgentType.WarExplorer).addCondition(cWToI);
		model.getModelBrain(WarAgentType.WarExplorer).addCondition(cIToW);
		*/
		
		View view = new View(model);
		
		Controleur controleur = new Controleur(model, view);
		
	}

	private void createBasicFSM(Model model) {
		model.createModelBrain(WarAgentType.WarBase);
		model.createModelBrain(WarAgentType.WarExplorer);
		model.createModelBrain(WarAgentType.WarRocketLauncher);
		model.createModelBrain(WarAgentType.WarEngineer);
		model.createModelBrain(WarAgentType.WarTurret);
		model.createModelBrain(WarAgentType.WarKamikaze);
		
		model.getModelBrain(WarAgentType.WarBase).addState(
				new ModelState("State Idle", EnumPlan.WarPlanIdle, new GenericPlanSettings()));
		model.getModelBrain(WarAgentType.WarExplorer).addState(
				new ModelState("State Idle", EnumPlan.WarPlanWiggle, new GenericPlanSettings()));
		model.getModelBrain(WarAgentType.WarRocketLauncher).addState(
				new ModelState("State Wiggle", EnumPlan.WarPlanWiggle, new GenericPlanSettings()));
		model.getModelBrain(WarAgentType.WarEngineer).addState(
				new ModelState("State Wiggle", EnumPlan.WarPlanWiggle, new GenericPlanSettings()));
		model.getModelBrain(WarAgentType.WarTurret).addState(
				new ModelState("State Idle", EnumPlan.WarPlanIdle, new GenericPlanSettings()));
		model.getModelBrain(WarAgentType.WarKamikaze).addState(
				new ModelState("State Wiggle", EnumPlan.WarPlanWiggle, new GenericPlanSettings()));
	}
}
