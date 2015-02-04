package edu.warbot.FSMEditor.FSMXmlParser;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import edu.warbot.FSM.plan.WarPlanSettings;
import edu.warbot.FSMEditor.Modeles.Modele;
import edu.warbot.FSMEditor.Modeles.ModeleBrain;
import edu.warbot.FSMEditor.Modeles.ModeleCondition;
import edu.warbot.FSMEditor.Modeles.ModeleState;

public class FSMXmlSaver extends FSMXmlParser{
	
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

	private Element getContentConditionForBrain(ModeleBrain brain) {
		Element elemConditions = new Element(Conditions);
		
		for (ModeleCondition condition : brain.getConditions()) {
			elemConditions.addContent(getContentForCondition(condition));
		}
		return elemConditions;
	}

	private Element getContentForCondition(ModeleCondition cond) {
		Element elemCond = new Element(Condition);

		elemCond.addContent(new Element("Name").setText(cond.getName()));
		elemCond.addContent(new Element("Type").setText(cond.getType()));
		elemCond.addContent(new Element("StateOutName").setText(cond.getStateDestination().getName()));
		//elemCond.addContent(new Element("StateInName").setText(cond.getSource().getNom()));
		
		//Content pour le type de condition attriutCheck
		Element elemAttCheck = new Element("AttributCheck");
		elemCond.addContent(elemAttCheck);
		
		elemAttCheck.addContent(new Element("Name").setText(cond.getAttributCheckName()));
		elemAttCheck.addContent(new Element("operateur").setText(cond.getAttributCheckOperateur()));
		elemAttCheck.addContent(new Element("Value").setText(cond.getAttributCheckValue()));
		elemAttCheck.addContent(new Element("Pourcentage").setText(String.valueOf(cond.getAttributCheckPourcentage())));
		
		return elemCond;
	}

	private Element getContentStatesForBrain(ModeleBrain brain) {
		
		Element states = new Element(States);
		
		for (ModeleState currentState : brain.getStates()) {
			states.addContent(getContentForState(currentState));
		}
		return states;
	}

	private Element getContentForState(ModeleState state) {
		Element elemState = new Element(State);
		
		elemState.addContent(new Element("Name").setText(state.getName()));
		elemState.addContent(new Element("Plan").setText(state.getPlanName()));

		elemState.addContent(getContentPlanSettings(state));
		elemState.addContent(getContentConditionsOutNameForState(state));
		
		return elemState;
	}

	private Element getContentPlanSettings(ModeleState state) {
		Element elemPlanSetting = new Element(PlanSettings);
		
		WarPlanSettings planSet = state.getWarPlanSettings();
		Field[] fields = planSet.getClass().getDeclaredFields();
		
		for (int i = 0; i < fields.length; i++) {
			Object fieldValue = null;
			try {
				fieldValue = fields[i].get(planSet);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
			elemPlanSetting.addContent(
					new Element(fields[i].getName())
					.setText(String.valueOf(fieldValue)));
		}
		
		return elemPlanSetting;
	}

	private Element getContentConditionsOutNameForState(ModeleState state) {
		Element elemconditions = new Element(ConditionsOutID);
		
		for (ModeleCondition currentCond : state.getConditionsOut()) {
			elemconditions.addContent(new Element(ConditionOutID).setText(currentCond.getName()));
		}
		
		return elemconditions;
	}

}