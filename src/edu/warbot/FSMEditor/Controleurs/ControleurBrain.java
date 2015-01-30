package edu.warbot.FSMEditor.Controleurs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.warbot.FSMEditor.Configuration;
import edu.warbot.FSMEditor.FSMJarGenerator;
import edu.warbot.FSMEditor.FSMXMLSaver;
import edu.warbot.FSMEditor.MouseListenerPanelCenter;
import edu.warbot.FSMEditor.Modeles.Modele;
import edu.warbot.FSMEditor.Modeles.ModeleBrain;
import edu.warbot.FSMEditor.Modeles.ModeleCondition;
import edu.warbot.FSMEditor.Modeles.ModeleState;
import edu.warbot.FSMEditor.Panel.PanelCondition;
import edu.warbot.FSMEditor.Panel.PanelState;
import edu.warbot.FSMEditor.Views.ViewBrain;
import edu.warbot.FSMEditor.dialogues.DialogueCondSetting;
import edu.warbot.FSMEditor.dialogues.DialogueStateSetting;

public class ControleurBrain {

	public ModeleBrain modeleBrain;
	public ViewBrain viewBrain;
	
	public ControleurBrain(ModeleBrain modele, ViewBrain view) {
		this.modeleBrain = modele;
		this.viewBrain = view;
		
		placeListenerOnView();
		placeListeerOnPanel();

	}
	
	
	private void placeListeerOnPanel() {
		MouseListenerPanelCenter mouseListener = new MouseListenerPanelCenter(this);
		viewBrain.getPanelCenter().addMouseListener(mouseListener);
		viewBrain.getPanelCenter().addMouseMotionListener(mouseListener);		
	}

	private void placeListenerOnView(){
		
		viewBrain.getButtonAddSate().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eventAddSate();
			}
		});
		
		viewBrain.getButtonAddCond().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eventAddCond();
			}
		});
		
		viewBrain.getButtonDelState().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eventDelState();
			}
		});
		
		viewBrain.getButtonEditCond().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eventEditCond();
			}
		});
		
		viewBrain.getListeCondition().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				eventListeConditionEdition(e);
			}
		});
		
	}
	
	private void eventListeConditionEdition(ListSelectionEvent e){
		//Deselctionne tous les elements
		for (PanelCondition p : this.viewBrain.getPanelCenter().getPanelcondition()) {
			p.isSelected = false;
		}
		
		String stringCond = viewBrain.getListeCondition().getSelectedValue();
		PanelCondition panelCond = this.getPanelConditionWithName(stringCond);
		
		if(panelCond != null){
			panelCond.isSelected = true;
		}
		
		this.viewBrain.getPanelCenter().repaint();
	}
	
	private void eventAddSate(){
		DialogueStateSetting d = new DialogueStateSetting(this.viewBrain); 

		if(d.isValideComponent()){
			
			//Creation du modele avec la fenetre de dialogue
			ModeleState s = new ModeleState(d.getNom(), d.getPlanName());
			this.addState(s);
			
			viewBrain.getPanelCenter().repaint();
		}
		
	}
	
	private void eventAddCond(){
		
		if(this.viewBrain.getPanelCenter().isTwoStatesSelected()){
			
			DialogueCondSetting d = new DialogueCondSetting(this.viewBrain);
			
			PanelState panelSource;
			PanelState panelDest;
			ModeleState modeleStateSource;
			ModeleState modeleStateDest;
			
			panelSource = this.viewBrain.getPanelCenter().getFirstSelectedState();
			panelDest = this.viewBrain.getPanelCenter().getSecondeSelectedState();
			modeleStateSource = panelSource.getModele();
			modeleStateDest = panelDest.getModele();
			
			//Crée le nouveau modele condition
			ModeleCondition mc = new ModeleCondition(d.getName(), d.getConditionType(), 
					modeleStateSource, modeleStateDest);
			
			addCondition(mc);
			
			
		}else{
			System.out.println("Pour ajouter une condition deux etats doivent être selectionnés");
		}
		
		viewBrain.getPanelCenter().repaint();
	}
	
	private void eventDelState(){
		if(this.viewBrain.getPanelCenter().isOneStateSelected()){
			
			PanelState panelToDelet = this.viewBrain.getPanelCenter().getFirstSelectedState();
			
			//ATTENTION : supprimer le modele avant le panel puisque on utilise le panel pour acceder au modele
			this.modeleBrain.removeState(panelToDelet.getModele());
			this.viewBrain.getPanelCenter().removePanelState(panelToDelet);
			
			this.viewBrain.getPanelCenter().setNoItemSelected();
			
			viewBrain.getPanelCenter().repaint();
		}
		
	}

	private void eventEditCond(){
		String condSelec = this.viewBrain.getListeConditions().getSelectedValue();
		
		if(condSelec != null){
			
			ModeleCondition modeleCond;
			
			for (ModeleCondition modeleC : this.modeleBrain.getConditions()) {
				if(modeleC.getNom().equals(condSelec)){
					modeleCond = modeleC;
					break;
				}
			}
			
			if(condSelec.equals(Configuration.WarConditionActionTerminate)){
				
				
				
			}
		}
	}
	
	private PanelCondition getPanelConditionWithName(String s){
		for (PanelCondition p : this.viewBrain.getPanelCenter().getPanelcondition()) {
			if(p.getModele().getNom().equals(s))
				return p;
		}
		return null;
	}


	public void addState(ModeleState state) {
		this.modeleBrain.addState(state);
		
		//Création du panel
		PanelState panel = new PanelState(state);
		//Ajoute le panel 
		this.viewBrain.getPanelCenter().addState(panel);		
	}


	public void addCondition(ModeleCondition condition) {

		//Dit au modele d'ajouter la nouvelle condition
		this.modeleBrain.addCondition(condition);
		
		//Dit à la vu d'ajouter la nouvelle condition
		this.viewBrain.addCondition(condition);
		
	}
	

}
