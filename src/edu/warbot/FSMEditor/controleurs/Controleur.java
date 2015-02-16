package edu.warbot.FSMEditor.controleurs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;
import javax.swing.Popup;

import edu.warbot.FSM.WarGenericSettings.ConditionSettings;
import edu.warbot.FSM.WarGenericSettings.PlanSettings;
import edu.warbot.FSMEditor.FSMModelRebuilder;
import edu.warbot.FSMEditor.models.ModelCondition;
import edu.warbot.FSMEditor.models.ModelState;
import edu.warbot.FSMEditor.models.Model;
import edu.warbot.FSMEditor.models.ModeleBrain;
import edu.warbot.FSMEditor.views.View;
import edu.warbot.FSMEditor.views.ViewBrain;
import edu.warbot.FSMEditor.xmlParser.FsmXmlReader;
import edu.warbot.FSMEditor.xmlParser.FsmXmlSaver;
import edu.warbot.agents.enums.WarAgentType;

public class Controleur {
	public Model model;
	public View view;
	
	private ArrayList<ControleurBrain> controleursBrains = new ArrayList<>();
	
	public Controleur(Model modele, View view) {
		this.model = modele;
		this.view = view;
		
		createControleurBrains();
		
		placeListenerOnMenuBar();
		
	}

	private void createControleurBrains() {
//		controleursBrains = new ArrayList<>();
		
		for (ViewBrain viewBrain : this.view.getViewBrains()) {
			this.controleursBrains.add(new ControleurBrain(viewBrain.getModel(), viewBrain));
		}
	}

	public void reloadModel() {
		model.update();
		
		//On donne le nouveau model à la vu
		view.loadModel(this.model);
		
		//La vu connait son model (à ce niveau ca ne sert a rien mais ca ne gene rien non plus pour 'linstant on le laisse
		model.setView(view);
		
		this.update();

	}

	private void update() {
		controleursBrains.clear();
		createControleurBrains();		
	}

	public ControleurBrain getControleurBrain(WarAgentType agentType) {
		for (ControleurBrain controleurBrain : controleursBrains) {
			if(controleurBrain.modeleBrain.getAgentType().equals(agentType))
				return controleurBrain;
		}
		System.err.println("Controleur : Impossible to find controleurBrain for agent type " + agentType);
		return null;
	}

	public void createControleursBrains(WarAgentType agentType) {
		ModeleBrain mb = new ModeleBrain(agentType);
		ViewBrain vb = new ViewBrain(mb);
		ControleurBrain cb = new ControleurBrain(mb, vb);
		
		this.model.addModelBrain(mb);
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
				eventMenuBarItemTest();
			}
		});
	}
	
	public void eventMenuBarItemSave() {
		FsmXmlSaver fsmSaver = new FsmXmlSaver();
		
		fsmSaver.saveFSM(model, FsmXmlReader.xmlConfigurationDefaultFilename);
		System.out.println("Controleur : Configuration file exported successfull");
	}
	
	public void eventMenuBarItemTest() {
		//Sauvegarde le model
//		eventMenuBarItemSave();

		//Charge le model
//		eventMenuBarItemLoad();
		
		boolean isValid = true;
		
		printModelInformations(this.model);
		
		//Check if ID are unique
		for (ModeleBrain modelBrain: this.model.getModelsBrains()) {
			
			//State ID
			ArrayList<String> stateName = new ArrayList<>();
			for (ModelState modState : modelBrain.getStates()) {
				if(stateName.contains(modState.getName())){
					JOptionPane.showMessageDialog(this.view,
						    "States ID have to be unique !",
						    "ID error",
						    JOptionPane.ERROR_MESSAGE);
					isValid = false;
				}
				stateName.add(modState.getName());
				
			}
			
			//Cond ID
			ArrayList<String> CondName = new ArrayList<>();
			for (ModelCondition modCond : modelBrain.getConditions()) {
				if(CondName.contains(modCond.getName())){
					JOptionPane.showMessageDialog(this.view,
						    "Conditions ID have to be unique !",
						    "ID error",
						    JOptionPane.ERROR_MESSAGE);
					isValid = false;
				}
				CondName.add(modCond.getName());
				
			}
		}
		
		//Check if declared ID exist
		for (ModeleBrain modelBrain: this.model.getModelsBrains()) {
			
			//State ID
			ArrayList<String> stateName = new ArrayList<>();
			ArrayList<String> condOutID = new ArrayList<>();
			for (ModelState modState : modelBrain.getStates()) {
				stateName.add(modState.getName());
				for (ModelCondition string : modState.getConditionsOut()) {
					condOutID.add(string.getName());
				}
			}
			
			//Cond ID
			ArrayList<String> CondName = new ArrayList<>();
			ArrayList<String> stateOutID = new ArrayList<>();
			for (ModelCondition modCond : modelBrain.getConditions()) {
				stateOutID.add(modCond.getStateDestination().getName());
				CondName.add(modCond.getName());
			}

			for (String string : stateOutID) {
				if(! stateName.contains(string)){
					JOptionPane.showMessageDialog(this.view,
						    "Condition with state out " + string + " have no associated state",
						    "ID error",
						    JOptionPane.ERROR_MESSAGE);
					isValid = false;
				}
			}
			
			for (String string : condOutID) {
				if(! CondName.contains(string)){
					JOptionPane.showMessageDialog(this.view,
						    "State with condition out " + string + " have no associated condition",
						    "ID error",
						    JOptionPane.ERROR_MESSAGE);
					isValid = false;
				}
			}
		}

		if(isValid)
			JOptionPane.showMessageDialog(this.view,
				    "Your FSM seen to be corect",
				    "Succes",
				    JOptionPane.INFORMATION_MESSAGE);
		
		System.out.println("Controleur : Your FSM seen to be valid, check it in console");
		
	}
	
	public void eventMenuBarItemLoad(){
		FsmXmlReader reader = new FsmXmlReader(FsmXmlReader.xmlConfigurationDefaultFilename);
		this.model = reader.getGeneratedFSMModel();
		
		FSMModelRebuilder rebuilder = new FSMModelRebuilder(this.model);
		this.model = rebuilder.getRebuildModel();

		this.reloadModel();
		
		System.out.println("Controleur : Configuration file imported successfull");
	}

	private void printModelInformations(Model modeleRead) {
		if(!modeleRead.isRebuild())
			System.out.println("Controleur : WARNING model is not rebuild the print will probabli crash");
		
		System.out.println("*** Vérification du modele généré dynamiquement pour la FSM ***");
		for (ModeleBrain modBrain : modeleRead.getModelsBrains()) {
			System.out.println("* Traitement du modele pour le type d'agent " + modBrain.getAgentTypeName() + " *");
		
			System.out.println("Liste des états " + modBrain.getStates().size());
			for (ModelState modState : modBrain.getStates()) {
				System.out.println("\tEtat : Name=" + modState.getName() + " plan=" + modState.getPlanLoaderName());
				System.out.println("\tConditions de sorties ID : " + modState.getConditionsOutID().size());
				for (String condID : modState.getConditionsOutID()) {
					System.out.println("\t\t" + condID);
				}
				System.out.println("\tConditions de sorties objet: " + modState.getConditionsOut().size());
				for (ModelCondition condMod : modState.getConditionsOut()) {
					System.out.println("\t\tName=\"" + condMod.getName() + "\"");
				}
				
				//Afichage des parametres du plan
				PlanSettings planSet = modState.getPlanSettings();
				Field field[] = planSet.getClass().getDeclaredFields();
				System.out.println("\tPlan settings : ");
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
				//TODO la ca va plnater car il y aura l'id de l'état destination mais pas le pointeur vers l'objet de l'état
				System.out.println("\tCondition : Name=" + modCond.getName() + " Type=" + modCond.getType());
				System.out.println("\tEtat destination ID : " + modCond.getStateOutId());
				System.out.println("\tEtat destination objet : Name=" + modCond.getStateDestination().getName());
				
				//Affichage des conditions settings
				ConditionSettings condSet = modCond.getConditionSettings();
				Field field[] = condSet.getClass().getDeclaredFields();
				System.out.println("\tCondition settings : ");
				for (int i = 0; i < field.length; i++) {
					try {
						String fieldValue = null;
						if(field[i].getType().isArray()){
							Object[] arrayO = (Object[]) field[i].get(condSet);
							fieldValue = Arrays.toString(arrayO);
						}else
							fieldValue = String.valueOf(field[i].get(condSet));
						
						System.out.println("\t\t" + field[i].getName() + "=" + fieldValue);
						
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
