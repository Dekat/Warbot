package edu.warbot.FSMEditor.controleurs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import edu.warbot.FSM.WarFSM;
import edu.warbot.FSM.WarGenericSettings.WarPlanSettings;
import edu.warbot.FSMEditor.FSMInstancier;
import edu.warbot.FSMEditor.FSMModelRebuilder;
import edu.warbot.FSMEditor.models.Modele;
import edu.warbot.FSMEditor.models.ModeleBrain;
import edu.warbot.FSMEditor.models.ModelCondition;
import edu.warbot.FSMEditor.models.ModelState;
import edu.warbot.FSMEditor.views.View;
import edu.warbot.FSMEditor.views.ViewBrain;
import edu.warbot.FSMEditor.xmlParser.FsmXmlReader;
import edu.warbot.FSMEditor.xmlParser.FsmXmlSaver;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.adapters.WarExplorerAdapter;

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
		FsmXmlSaver fsmSaver = new FsmXmlSaver();
		
		fsmSaver.saveFSM(modele, FsmXmlReader.xmlConfigurationDefaultFilename);
		System.out.println("Configuration file exported successfull");
		
	}
	
	public void eventMenuBarItemSaveJar() {
		FsmXmlSaver saver = new FsmXmlSaver();
		saver.saveFSM(this.modele, FsmXmlReader.xmlConfigurationDefaultFilename);
		
		System.out.println("Configuration file exported successfull");

		FsmXmlReader reader = new FsmXmlReader(FsmXmlReader.xmlConfigurationDefaultFilename);
		Modele modeleRead = reader.getGeneratedFSMModel();
		
		System.out.println("Configuration file imported successfull");
		
//		printModelInformations(modeleRead);
		
		FSMInstancier fsmInstancier = new FSMInstancier(modeleRead);
		
		//Crée un agent pour tester
		WarExplorerAdapter explorerAdapter = null;
			//new WarExplorerAdapter(new WarExplorer(new Team("Team_debug_FSM"), brain));
		
		WarFSM fsm = fsmInstancier.getBrainControleurForAgent(WarAgentType.WarExplorer, explorerAdapter);
		
		fsm.initFSM();
		
		System.out.println("FSM generated successfull");
		
	}
	
	private void printModelInformations(Modele modeleRead) {
		System.out.println("*** Vérification du modele gnééré dynamiquement pour la FSM ***");
		for (ModeleBrain modBrain : modeleRead.getModelsBrains()) {
			System.out.println("* Traitement du modele pour le type d'agent " + modBrain.getAgentTypeName() + " *");
		
			System.out.println("Liste des états " + modBrain.getStates().size());
			for (ModelState modState : modBrain.getStates()) {
				System.out.println("\tEtat : Name=" + modState.getName() + " plan=" + modState.getPlanLoaderName());
				System.out.println("\tConditions de sorties ID : " + modState.getConditionsOutID().size());
				for (String condID : modState.getConditionsOutID()) {
					System.out.println("\t\t" + condID);
				}
				System.out.println("\tConditions de sorties : " + modState.getConditionsOut().size());
				for (ModelCondition condMod : modState.getConditionsOut()) {
					System.out.println("\t\t" + condMod.getName());
				}
				
				//Afichage des parametres du plan
				WarPlanSettings planSet = modState.getPlanSettings();
				Field field[] = planSet.getClass().getDeclaredFields();
				System.out.println("\tState settings : " + field.length);
				for (int i = 0; i < field.length; i++) {
					try {
						String fieldValue = null;
						if(field[i].getType().isArray()){
							Object[] arrayO = (Object[]) field[i].get(planSet);
							fieldValue = Arrays.toString(arrayO);
						}else
							fieldValue = String.valueOf(field[i].get(planSet));
						
						System.out.println("\t\t" + field[i].getName() + "=" + fieldValue);
						
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			
			System.out.println("Liste des conditions " + modBrain.getConditions().size());
			for (ModelCondition modCond : modBrain.getConditions()) {
				//TODO la ca va plnat� car il y aura l'id de l�tat destination mais pas le pointeur vers l'objet de l'�tat
				System.out.println("\tConditin : Name=" + modCond.getName() + " type=" + modCond.getType() + " stateOutID=" + modCond.getStateOutId() + " stateOut=" + modCond.getStateDestination().getName());
				
				//Affichage des informations des parametres de la condition
				//TODO faire la suite de l'affichage
			}
		}
	}

	public void eventMenuBarItemLoad(){
		
	}

}
