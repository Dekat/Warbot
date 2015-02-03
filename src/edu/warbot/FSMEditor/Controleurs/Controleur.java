package edu.warbot.FSMEditor.Controleurs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;

import edu.warbot.FSM.WarFSM;
import edu.warbot.FSM.plan.WarPlanSettings;
import edu.warbot.FSMEditor.FSMInstancier;
import edu.warbot.FSMEditor.FSMModelRebuilder;
import edu.warbot.FSMEditor.FSMXmlParser.FSMXmlReader;
import edu.warbot.FSMEditor.FSMXmlParser.FSMXmlSaver;
import edu.warbot.FSMEditor.Modeles.Modele;
import edu.warbot.FSMEditor.Modeles.ModeleBrain;
import edu.warbot.FSMEditor.Modeles.ModeleCondition;
import edu.warbot.FSMEditor.Modeles.ModeleState;
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
		
		//Affichage des informations pour vérifier la validité (ici avec des id)
		printModelInformations(modeleRead);
		
		FSMModelRebuilder fsmModRebuilder = new FSMModelRebuilder(modeleRead);
		fsmModRebuilder.rebuildModel();
		Modele modelRebuild = fsmModRebuilder.getRebuildModel();
		
		System.out.println("Configuration file imported successfull");
		
		FSMInstancier fsmSaver = new FSMInstancier(modeleRead);
		ArrayList<WarFSM> allFsm = fsmSaver.getGeneratedFSM();
		
		for (WarFSM fsm : allFsm) {
			fsm.initFSM();
		}
		
		System.out.println("FSM generated successfull");
		
	}
	
	private void printModelInformations(Modele modeleRead) {
		System.out.println("*** Vérification du modele généré dynamiquement pour la FSM ***");
		for (ModeleBrain modBrain : modeleRead.getModelsBrains()) {
			System.out.println("* Traitement du modele pour le type d'agent " + modBrain.getAgentTypeName() + " *");
		
			System.out.println("Liste des états " + modBrain.getStates().size());
			for (ModeleState modState : modBrain.getStates()) {
				System.out.println("\tEtat : Name=" + modState.getName() + " plan=" + modState.getPlanName());
				System.out.println("\tConditions de sorties ID : " + modState.getConditionsOutID().size());
				for (String condID : modState.getConditionsOutID()) {
					System.out.println("\t\t" + condID);
				}
				System.out.println("\tConditions de sorties : " + modState.getConditionsOut().size());
				for (ModeleCondition condMod : modState.getConditionsOut()) {
					System.out.println("\t\t" + condMod.getName());
				}
				
				//Afichage des parametres du plan
				WarPlanSettings planSet = modState.getWarPlanSettings();
				Field field[] = planSet.getClass().getDeclaredFields();
				System.out.println("\tState settings : " + field.length);
				for (int i = 0; i < field.length; i++) {
					try {
						System.out.println("\t\t" + field[i].getName() + "=" + field[i].get(planSet).toString());
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			
			System.out.println("Liste des conditions " + modBrain.getConditions().size());
			for (ModeleCondition modCond : modBrain.getConditions()) {
				//TODO la ca va plnaté car il y aura l'id de létat destination mais pas le pointeur vers l'objet de l'état
				System.out.println("\tConditin : Name=" + modCond.getName() + " type=" + modCond.getType() + " stateOutID=" + modCond.getStateOutId() + " stateOut=" + modCond.getStateDestination().getName());
				
				//Affichage des informations des parametres de la condition
				//TODO faire la suite de l'affichage
			}
		}
	}

	public void eventMenuBarItemLoad(){
		
	}

}
