package edu.warbot.FSMEditor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import edu.warbot.FSM.plan.WarPlanSettings;
import edu.warbot.FSMEditor.Modeles.Modele;
import edu.warbot.FSMEditor.Modeles.ModeleState;

public class FSMXmlReader {

	public static String xmlConfigurationFilename = "XMLConfiguration.xml";
	private Modele modeleFSM;
	
	//TODO : finir la lecture du XML
	//Vérifier que la lecture ce fasse bien
	//Ajouter la lecture des elements qui seront ajouté par la suite à lecriture du XML

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
		createStates(brain.getChild("States"), modele);
		createConditions(brain.getChild("Conditions"), modele);
	}

	private void createConditions(Element child, Modele modele) {
		// TODO Auto-generated method stub
	}

	private void createStates(Element states, Modele modele) {
		for (Element state : states.getChildren()) {
			createState(state, modele);
		}
	}

	private void createState(Element state, Modele modele) {
		String name = state.getChild("Name").getValue();
		String plan = state.getChild("Plan").getValue();
		
		ArrayList<String> condID = getConditionsOutID(state.getChild("ConditionsOutName"));
		
		WarPlanSettings warPlanSetting = getWarPlanSettings(state);
		
		ModeleState modeleState = new ModeleState(name, plan, warPlanSetting);
		modeleState.setConditionsOutID(condID);
	}

	private WarPlanSettings getWarPlanSettings(Element state) {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList<String> getConditionsOutID(Element element) {
		ArrayList<String> res = new ArrayList<String>();
		for (Element currentID : element.getChildren()) {
			res.add(currentID.getValue());
		}
		return null;
	}

	public Modele getGeneratedFSMModel() {
		return this.modeleFSM;
	}
	

}
