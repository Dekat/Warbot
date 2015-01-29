package edu.warbot.FSMEditor.Controleurs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import edu.warbot.FSMEditor.FSMJarGenerator;
import edu.warbot.FSMEditor.FSMXMLSaver;
import edu.warbot.FSMEditor.View;
import edu.warbot.FSMEditor.Modele.Modele;
import edu.warbot.FSMEditor.Modele.ModeleBrain;

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

	private void createControleursBrains() {
		for (ModeleBrain mBrain : this.modele.getModeleBrains()) {
			controleursBrains.add(new ControleurBrain(mBrain, view));
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
		FSMJarGenerator fsmSaver = new FSMJarGenerator(this.modele, "export.jar");
		
	}
	
	public void eventMenuBarItemLoad(){
		
	}

}
