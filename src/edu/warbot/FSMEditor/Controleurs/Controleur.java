package edu.warbot.FSMEditor.Controleurs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import edu.warbot.FSMEditor.FSMJarGenerator;
import edu.warbot.FSMEditor.FSMXMLSaver;
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
			if(controleurBrain.modeleBrain.getAgentType().equals(agentType))
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
		FSMXMLSaver fsmSaver = new FSMXMLSaver();
		
		fsmSaver.saveFSM(modele, "FSM.xml");
		
	}
	
	public void eventMenuBarItemSaveJar() {
		FSMJarGenerator fsmSaver = new FSMJarGenerator(this.modele, "FSM.jar");
		fsmSaver.pushConfigurationInJarFile();
		
		System.out.println("Configuration file exported with success");
	}
	
	public void eventMenuBarItemLoad(){
		
	}

}
