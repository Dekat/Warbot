package edu.warbot.FSMEditor.FSMXmlParser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

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
		//Pour chaque conditions et chaque états
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
				
				//Pour les collections
				else if (fields[i].getType().equals(ArrayList.class)){

					ArrayList fieldArray = (ArrayList) fields[i].get(planSet);
					//On récupère une entré de la collection pour connaitre son type générique 
					//(impossible sinon a cause de l'effacement de type)
					if(fieldArray != null & fieldArray.size() > 0){
						if(fieldArray.get(0).getClass().equals(WarAgentType.class))
							fields[i].set(planSet,getFieldForArrayOfWarAgentType(fields[i], elemPlanSetting));
						else if(fieldArray.get(0).getClass().equals(Integer.class))
							fields[i].set(planSet,getFieldForArrayOfInteger(fields[i], elemPlanSetting));
						else if(fieldArray.get(0).getClass().equals(String.class))
							fields[i].set(planSet,getFieldForArrayOfString(fields[i], elemPlanSetting));
					}else{
						System.err.println("ERRER : imposible to read field for name " + fields[i].getName() + " are you sur you correctly instenciate it and it container at least one element (please see class WarPlanSetting)");
					}
				}
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return planSet;
	}

	private ArrayList<Integer> getFieldForArrayOfInteger(Field field,
			Element elemPlanSetting) {
		ArrayList<Integer> arrayRes= new ArrayList<>();
		
		String fieldValue = elemPlanSetting.getChild(field.getName()).getValue();
		
		//Parse le string en tableau
		String[] stringValues = parseStringTab(fieldValue);
		
		for (int i = 0; i < stringValues.length; i++) {
			arrayRes.add(Integer.valueOf(stringValues[i]));
		}
		return arrayRes;
	}

	private ArrayList<WarAgentType> getFieldForArrayOfWarAgentType(Field field, Element elemPlanSetting) {
		ArrayList<WarAgentType> arrayRes= new ArrayList<>();
		
		String fieldValue = elemPlanSetting.getChild(field.getName()).getValue();
		
		//Parse le string en tableau
		String[] stringValues = parseStringTab(fieldValue);
		
		for (int i = 0; i < stringValues.length; i++) {
			arrayRes.add(WarAgentType.valueOf(stringValues[i]));
		}
		return arrayRes;
	}
	
	private String[] parseStringTab(String stringTab) {
		return stringTab.replaceAll("\\]", "").replaceAll("\\[", "").split(",");
	}

	private ArrayList<String> getFieldForArrayOfString(Field field, Element elemPlanSetting) {
		ArrayList<String> arrayRes= new ArrayList<>();
		
		String fieldValue = elemPlanSetting.getChild(field.getName()).getValue();
		
		//Parse le string en tableau
		String[] stringValues = parseStringTab(fieldValue);
		
		for (int i = 0; i < stringValues.length; i++) {
			arrayRes.add(stringValues[i]);
		}
		return arrayRes;
	}
	
	private String getFieldForString(Field field, Element elemPlanSetting) {
		return elemPlanSetting.getChild(field.getName()).getValue();
	}

	private Boolean getFieldForBoolean(Field field, Element elemPlanSetting) {
		try{
			return Boolean.valueOf(elemPlanSetting.getChild(field.getName()).getValue());
		}catch(NumberFormatException e){
			return false;
		}
	}

	private Integer getFieldForInteger(Field field, Element elemPlanSetting) {
		try{
			return Integer.valueOf(elemPlanSetting.getChild(field.getName()).getValue());
		}catch(NumberFormatException e){
			return -1;
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
