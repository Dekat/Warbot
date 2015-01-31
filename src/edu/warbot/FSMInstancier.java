package edu.warbot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import edu.warbot.FSMEditor.Modeles.Modele;
import edu.warbot.FSMEditor.Modeles.ModeleState;

public class FSMInstancier {

	JarOutputStream jarStream;
	JarFile jarFile;

	String xmlConfigurationFilename = "XMLConfiguration.xml";

	public FSMInstancier(String jarFilename) {

		// Récupère le fichier de configuration
		try {
			jarFile = new JarFile(jarFilename);
			// jarStream = new JarOutputStream(new
			// FileOutputStream(jarFilename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JarEntry xmlEntry = jarFile.getJarEntry(xmlConfigurationFilename);

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
		
		Modele modeleFSM = new Modele();
		
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
		
		ModeleState modeleState = new ModeleState(name, plan);
		modeleState.setConditionsOutID(condID);
	}

	private ArrayList<String> getConditionsOutID(Element element) {
		ArrayList<String> res = new ArrayList<String>();
		for (Element currentID : element.getChildren()) {
			res.add(currentID.getValue());
		}
		return null;
	}
	

}
