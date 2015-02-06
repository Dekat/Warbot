package edu.warbot.FSMEditor.FSMXmlParser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.sun.org.apache.regexp.internal.recompile;

import edu.warbot.FSM.plan.WarPlanSettings;
import edu.warbot.FSMEditor.Modeles.Modele;
import edu.warbot.FSMEditor.Modeles.ModeleBrain;
import edu.warbot.FSMEditor.Modeles.ModeleState;
import edu.warbot.agents.enums.WarAgentType;

public class FSMXmlReader extends FSMXmlParser{

	public static String xmlConfigurationFilename = "XMLConfiguration.xml";
	private Modele modeleFSM;
	
	public FSMXmlReader(String jarFilename) {

		// Ouvre le XML
		File xmlFile = new File(xmlConfigurationFilename);

		Document doc = null;
		try {
			doc = new SAXBuilder().build(xmlFile);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		modeleFSM = new Modele();
		
		Element root = doc.getRootElement();
		
		//Pour chaque brains
		for (Element currentBrain : root.getChildren()) {
			createBrain(currentBrain, modeleFSM);
		}

	}

	private void createBrain(Element brain, Modele modele) {
		//Pour chaque conditions et chaque �tats
		String agentTypeName = brain.getChild(AgentType).getValue();
		WarAgentType agentType = WarAgentType.valueOf(agentTypeName);
		
		ModeleBrain modelBrain = new ModeleBrain(agentType);
		modele.addModelBrain(modelBrain);
		
		createStates(brain.getChild(States), modelBrain);
		createConditions(brain.getChild(Conditions), modelBrain);
	}

	private void createConditions(Element child, ModeleBrain modeleBrain) {
		// TODO Auto-generated method stub
	}

	private void createStates(Element states, ModeleBrain modeleBrain) {
		for (Element state : states.getChildren()) {
			createState(state, modeleBrain);
		}
	}

	private void createState(Element state, ModeleBrain modeleBrain) {
		
		String name = state.getChild("Name").getValue();
		String plan = state.getChild("Plan").getValue();
		
		ArrayList<String> condID = getConditionsOutID(state.getChild(ConditionsOutID));
		
		WarPlanSettings warPlanSetting = getWarPlanSettings(state.getChild(PlanSettings));
		
		ModeleState modeleState = new ModeleState(name, plan, warPlanSetting);
		modeleState.setConditionsOutID(condID);
		
		modeleBrain.addState(modeleState);
	}

	private WarPlanSettings getWarPlanSettings(Element elemPlanSetting) {
		WarPlanSettings planSet = new WarPlanSettings();
		
		Field[] fields = planSet.getClass().getDeclaredFields();
		
		for (int i = 0; i < fields.length; i++) {
			try {
				
				//Pour les attribtus simples
				if (fields[i].getType().equals(Integer.class))
					fields[i].set(planSet,getFieldForInteger(fields[i], elemPlanSetting));
				else if (fields[i].getType().equals(Boolean.class))
					fields[i].set(planSet,getFieldForBoolean(fields[i], elemPlanSetting));
				else if (fields[i].getType().equals(String.class))
					fields[i].set(planSet,getFieldForString(fields[i], elemPlanSetting));
				else if (fields[i].getType().equals(Integer[].class))
					fields[i].set(planSet,getFieldForIntegerTab(fields[i], elemPlanSetting));
				else if (fields[i].getType().equals(String[].class))
					fields[i].set(planSet,getFieldForStringTab(fields[i], elemPlanSetting));
				else if (fields[i].getType().equals(WarAgentType[].class))
					fields[i].set(planSet,getFieldForAgentTypeTab(fields[i], elemPlanSetting));
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return planSet;
	}

	private String[] parseStringTab(String stringTab) {
		return stringTab.replaceAll("\\]", "").replaceAll("\\[", "").split(",");
	}

	/**
	 * Cette méthode est peut etre générique pour d'autre type comme integer (je testerais après)
	 * @param field
	 * @param elemPlanSetting
	 * @return
	 */
	private Object getFieldForString(Field field, Element elemPlanSetting) {
		return field.getType().cast(elemPlanSetting.getChild(field.getName()).getValue());
//		return elemPlanSetting.getChild(field.getName()).getValue();
	}

	private Integer getFieldForInteger(Field field, Element elemPlanSetting) {
		try{
			return Integer.valueOf(elemPlanSetting.getChild(field.getName()).getValue());
		}catch(NumberFormatException e){
			return null;
		}
	}
	
	private Integer[] getFieldForIntegerTab(Field field, Element elemPlanSetting) {
		String fieldValue = elemPlanSetting.getChild(field.getName()).getValue();
		
		String[] stringValues = parseStringTab(fieldValue);
		
		Integer valuesInteger[] = new Integer[stringValues.length];
		
		for (int i = 0; i < stringValues.length; i++) {
			valuesInteger[i] = Integer.valueOf(stringValues[i]);
		}
		return valuesInteger;
	}
	
	private String[] getFieldForStringTab(Field field, Element elemPlanSetting) {
		String fieldValue = elemPlanSetting.getChild(field.getName()).getValue();
		
		String[] stringValues = parseStringTab(fieldValue);
		
		//Je fais des opération inutiles mais quand je fusionnerais cette méthode avec d'autre pour plus de généricité ça sera plus facile comme ça
		String valuesInteger[] = new String[stringValues.length];
		
		for (int i = 0; i < stringValues.length; i++) {
			valuesInteger[i] = String.valueOf(stringValues[i]);
		}
		return valuesInteger;
	}
	
	private WarAgentType[] getFieldForAgentTypeTab(Field field, Element elemPlanSetting) {
		String fieldValue = elemPlanSetting.getChild(field.getName()).getValue();
		
		String[] stringValues = parseStringTab(fieldValue);
		
		//Je fais des opération inutiles mais quand je fusionnerais cette méthode avec d'autre pour plus de généricité ça sera plus facile comme ça
		WarAgentType valuesInteger[] = new WarAgentType [stringValues.length];
		
		for (int i = 0; i < stringValues.length; i++) {
			valuesInteger[i] = WarAgentType.valueOf(stringValues[i]);
		}
		return valuesInteger;
	}

	private Boolean getFieldForBoolean(Field field, Element elemPlanSetting) {
		try{
			return Boolean.valueOf(elemPlanSetting.getChild(field.getName()).getValue());
		}catch(NumberFormatException e){
			return false;
		}
	}

	private ArrayList<String> getConditionsOutID(Element element) {
		ArrayList<String> res = new ArrayList<String>();
		for (Element currentID : element.getChildren(ConditionOutID)) {
			res.add(currentID.getValue());
		}
		return res;
	}

	public Modele getGeneratedFSMModel() {
		return this.modeleFSM;
	}
	

}
