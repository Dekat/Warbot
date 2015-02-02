package edu.warbot.FSMEditor.Controleurs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import edu.warbot.FSM.WarFSM;
import edu.warbot.FSMEditor.FSMInstancier;
import edu.warbot.FSMEditor.FSMXmlParser.FSMXmlReader;
import edu.warbot.FSMEditor.FSMXmlParser.FSMXmlSaver;
import edu.warbot.FSMEditor.Modeles.Modele;
import edu.warbot.FSMEditor.Modeles.ModeleBrain;
import edu.warbot.FSMEditor.Views.View;
import edu.warbot.FSMEditor.Views.ViewBrain;
import edu.warbot.agents.enums.WarAgentType;

public class Controleur {
	public Modele modele;
	public View view;
	
	private ArrayList<ControleurBrain> controleursBrains = new ArrayList<ControleurBrain>();
	
	public Controleur(Modele modele, View view) {
		this.modele = modele;
		this.view = view;
		
		placeListenerOnMenuBar();
	}

	public ControleurBrain getControleurBrain(WarAgentType agentType) {
		for (ControleurBrain controleurBrain : controleursBrains) {
			if(controleurBrain.modeleBrain.getAgentType().equals(agentType))
				return controleurBrain;
		}
		System.err.println("Impossible to find controleurBrain for agent type " + agentType);
		return null;
	}

	public void createControleursBrains(WarAgentType agentType) {
		ModeleBrain mb = new ModeleBrain(agentType);
		ViewBrain vb = new ViewBrain(mb);
		ControleurBrain cb = new ControleurBrain(mb, vb);
		
		this.modele.addModelBrain(mb);
		this.view.addViewBrain(vb);
		
		this.controleursBrains.add(cb);
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
		
		//Affichage des informations du modele de la FSM pour vérifier la validité
		

		//Fin affiche informations
		
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
