package edu.warbot.FSMEditor.Controleurs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import edu.warbot.FSM.WarFSM;
import edu.warbot.FSMEditor.FSMInstancier;
import edu.warbot.FSMEditor.FSMXmlSaver;
import edu.warbot.FSMEditor.FSMXmlReader;
import edu.warbot.FSMEditor.Modeles.Modele;
import edu.warbot.FSMEditor.Views.View;
import edu.warbot.FSMEditor.Views.ViewBrain;

public class Controleur {
	public Modele modele;
	public View view;
	
	private ArrayList<ControleurBrain> controleursBrains = new ArrayList<ControleurBrain>();
	
	public Controleur(Modele modele, View view) {
		this.modele = modele;
		this.view = view;
		
		placeListenerOnMenuBar();
		
		createControleursBrains();
	}

	public ControleurBrain getControleurBrain(String agentType) {
		for (ControleurBrain controleurBrain : controleursBrains) {
			if(controleurBrain.modeleBrain.getAgentTypeName().equals(agentType))
				return controleurBrain;
		}
		System.err.println("Impossible to find controleurBrain for agent type " + agentType);
		return null;
	}

	private void createControleursBrains() {
		for (ViewBrain vBrain : this.view.getViewBrains()) {
			controleursBrains.add(new ControleurBrain(vBrain.getModele(), vBrain));
		}
	}
	
	private void placeListenerOnMenuBar() {
		view.getMenuBarItemSave().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eventMenuBarItemSave();
			}
		});
		view.getMenuBarItemLoad().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eventMenuBarItemLoad();
			}
		});
		view.getMenuBarItemSaveJar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eventMenuBarItemSaveJar();
			}
		});
	}
	
	public void eventMenuBarItemSave() {
		FSMXmlSaver fsmSaver = new FSMXmlSaver();
		
		fsmSaver.saveFSM(modele, FSMXmlReader.xmlConfigurationFilename);
		System.out.println("Configuration file exported successfull");
		
	}
	
	public void eventMenuBarItemSaveJar() {
		FSMXmlSaver saver = new FSMXmlSaver();
		saver.saveFSM(this.modele, FSMXmlReader.xmlConfigurationFilename);
		
		System.out.println("Configuration file exported successfull");

		FSMXmlReader reader = new FSMXmlReader(FSMXmlReader.xmlConfigurationFilename);
		Modele modeleRead = reader.getGeneratedFSMModel();

		System.out.println("Configuration file imported successfull");
		
		FSMInstancier fsmSaver = new FSMInstancier(modeleRead);
		ArrayList<WarFSM> allFsm = fsmSaver.getGeneratedFSM();
		
		for (WarFSM fsm : allFsm) {
			fsm.initFSM();
		}
			
		
		System.out.println("FSM generated successfull");
		
	}
	
	public void eventMenuBarItemLoad(){
		
	}

}
