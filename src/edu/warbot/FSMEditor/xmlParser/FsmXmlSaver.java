package edu.warbot.FSMEditor.xmlParser;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import edu.warbot.FSM.WarGenericSettings.WarConditionSettings;
import edu.warbot.FSM.WarGenericSettings.WarPlanSettings;
import edu.warbot.FSMEditor.models.Modele;
import edu.warbot.FSMEditor.models.ModeleBrain;
import edu.warbot.FSMEditor.models.ModelCondition;
import edu.warbot.FSMEditor.models.ModelState;

public class FsmXmlSaver extends FsmXmlParser{
	
	Document document;
	FileWriter file;
	
	public void saveFSM(Modele modele, String fileName) {
		
		try {
			file = new FileWriter(fileName);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Element rootBrains = new Element(Brains);
		
		document = new Document(rootBrains);
		
		for (ModeleBrain currentBrain : modele.getModelsBrains()) {
			Element brain = new Element("Brain");
			rootBrains.addContent(brain);
			
			brain.addContent(new Element(AgentType).setText(currentBrain.getAgentTypeName()));

			brain.addContent(getContentStatesForBrain(currentBrain));
			brain.addContent(getContentConditionForBrain(currentBrain));
		}
		
		XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
		
		try {
			sortie.output(document, file);
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("FSMConfiguration file generated successfull");
	}

	private Element getContentStatesForBrain(ModeleBrain brain) {
		Element states = new Element(States);
		
		for (ModelState currentState : brain.getStates()) {
			states.addContent(getContentForState(currentState));
		}
		return states;
	}

	private Element getContentConditionForBrain(ModeleBrain brain) {
		Element elemConditions = new Element(Conditions);
		
		for (ModelCondition condition : brain.getConditions()) {
			elemConditions.addContent(getContentForCondition(condition));
		}
		return elemConditions;
	}

	private Element getContentForState(ModelState state) {
		Element elemState = new Element(State);
		
		elemState.addContent(new Element("Name").setText(state.getName()));
		elemState.addContent(new Element("Plan").setText(state.getPlanName().toString()));
	
		elemState.addContent(getContentConditionsOutIDForState(state));
		elemState.addContent(getContentPlanSettings(state));
		
		return elemState;
	}

	private Element getContentPlanSettings(ModelState state) {
		Element elemPlanSetting = new Element(PlanSettings);
		
		WarPlanSettings planSet = state.getPlanSettings();
		if(planSet == null)
			planSet = new WarPlanSettings();
		
		Field[] fields = planSet.getClass().getDeclaredFields();
		
		String fieldValueString = null;
		for (int i = 0; i < fields.length; i++) {
			try {
				//Pour les tableaux
				if(fields[i].getType().isArray()){
					if(fields[i].get(planSet) == null)
						fieldValueString = "";
					else{
						Object[] fieldValues = (Object[]) fields[i].get(planSet);
						fieldValueString = Arrays.toString(fieldValues);
					}
					
				}else{ //Pour les valeurs simples
					if(fields[i].get(planSet) == null)
						fieldValueString = "";
					else
						fieldValueString = String.valueOf(fields[i].get(planSet));
				}
					
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
			elemPlanSetting.addContent(
				new Element(fields[i].getName()).setText(fieldValueString));
		}
		return elemPlanSetting;
	}

	private Element getContentForCondition(ModelCondition cond) {
		Element elemCond = new Element(Condition);

		elemCond.addContent(new Element(Name).setText(cond.getName()));
		elemCond.addContent(new Element(Type).setText(cond.getType().toString()));
		elemCond.addContent(new Element(StateOutID).setText(cond.getStateDestination().getName()));
		
		elemCond.addContent(getContentConditionSettings(cond));
		
		return elemCond;
	}
	
	//TODO fusionner cette méthod avec celle des plans
	private Element getContentConditionSettings(ModelCondition modelCond) {
		Element elemCondSetting = new Element(ConditionSettings);
		
		WarConditionSettings planSet = modelCond.getConditionSettings();
		if(planSet == null)
			planSet = new WarConditionSettings();
		
		Field[] fields = planSet.getClass().getDeclaredFields();
		
		String fieldValueString = null;
		for (int i = 0; i < fields.length; i++) {
			try {
				//Pour les tableaux
				if(fields[i].getType().isArray()){
					if(fields[i].get(planSet) == null)
						fieldValueString = "";
					else{
						Object[] fieldValues = (Object[]) fields[i].get(planSet);
						fieldValueString = Arrays.toString(fieldValues);
					}
					
				}else{ //Pour les valeurs simples
					if(fields[i].get(planSet) == null)
						fieldValueString = "";
					else
						fieldValueString = String.valueOf(fields[i].get(planSet));
				}
					
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
			elemCondSetting.addContent(
				new Element(fields[i].getName()).setText(fieldValueString));
		}
		return elemCondSetting;
	}

	private Element getContentConditionsOutIDForState(ModelState state) {
		Element elemconditions = new Element(ConditionsOutID);
		
		for (ModelCondition currentCond : state.getConditionsOut()) {
			elemconditions.addContent(new Element(ConditionOutID).setText(currentCond.getName()));
		}
		
		return elemconditions;
	}

}
