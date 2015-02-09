package edu.warbot.FSMEditor.Controleurs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.warbot.FSM.plan.WarPlanSettings;
import edu.warbot.FSMEditor.MouseListenerPanelCenter;
import edu.warbot.FSMEditor.FSMSettings.PlanEnum;
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
		viewBrain.getViewEditor().addMouseListener(mouseListener);
		viewBrain.getViewEditor().addMouseMotionListener(mouseListener);		
	}

	private void placeListenerOnView(){
		
		viewBrain.getButtonAddState().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eventAddState();
			}
		});
		
		viewBrain.getButtonEditState().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eventEditState();
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
		for (PanelCondition p : this.viewBrain.getViewEditor().getPanelcondition()) {
			p.isSelected = false;
		}
		
		String stringCond = viewBrain.getListeCondition().getSelectedValue();
		PanelCondition panelCond = this.getPanelConditionWithName(stringCond);
		
		if(panelCond != null){
			panelCond.isSelected = true;
		}
		
		this.viewBrain.getViewEditor().repaint();
	}
	
	private void eventAddState(){
		WarPlanSettings planSetting = new WarPlanSettings();
		
		ModeleState s = new ModeleState("State_", PlanEnum.WarPlanWiggle, planSetting);
		
		DialogueStateSetting d = new DialogueStateSetting(this.viewBrain, s); 

		if(d.isValideComponent()){
			
			this.addState(s);
			
			viewBrain.getViewEditor().repaint();
		}
	}
	
	private void eventEditState(){
		ModeleState modelState = 
				this.viewBrain.getViewEditor().getFirstSelectedState().getModelState();
		
		DialogueStateSetting d = new DialogueStateSetting(this.viewBrain, modelState); 

		if(d.isValideComponent()){
			
			viewBrain.getViewEditor().repaint();
		}
	}
	
	private void eventAddCond(){
		
		if(this.viewBrain.getViewEditor().isTwoStatesSelected()){
			
			DialogueCondSetting d = new DialogueCondSetting(this.viewBrain);
			
			PanelState panelSource;
			PanelState panelDest;
			ModeleState modeleStateSource;
			ModeleState modeleStateDest;
			
			panelSource = this.viewBrain.getViewEditor().getFirstSelectedState();
			panelDest = this.viewBrain.getViewEditor().getSecondeSelectedState();
			modeleStateSource = panelSource.getModelState();
			modeleStateDest = panelDest.getModelState();
			
			//Crée le nouveau modele condition
			ModeleCondition mc = new ModeleCondition(d.getName(), d.getConditionType(), 
					modeleStateSource, modeleStateDest);
			
			addCondition(mc);
			
			
		}else{
			System.out.println("Pour ajouter une condition deux etats doivent �tre selectionn�s");
		}
		
		viewBrain.getViewEditor().repaint();
	}
	
	private void eventDelState(){
		if(this.viewBrain.getViewEditor().isOneStateSelected()){
			
			PanelState panelToDelet = this.viewBrain.getViewEditor().getFirstSelectedState();
			
			//ATTENTION : supprimer le modele avant le panel puisque on utilise le panel pour acceder au modele
			this.modeleBrain.removeState(panelToDelet.getModelState());
			this.viewBrain.getViewEditor().removePanelState(panelToDelet);
			
			this.viewBrain.getViewEditor().setNoItemSelected();
			
			viewBrain.getViewEditor().repaint();
		}
		
	}

	private void eventEditCond(){
		String condSelec = this.viewBrain.getListeConditions().getSelectedValue();
		
		if(condSelec != null){
			
			ModeleCondition modeleCond;
			
			for (ModeleCondition modeleC : this.modeleBrain.getConditions()) {
				if(modeleC.getName().equals(condSelec)){
					modeleCond = modeleC;
					break;
				}
			}
			
//			if(condSelec.equals(Settings.WarConditionActionTerminate)){
//				
//				
//				
//			}
		}
	}
	
	private PanelCondition getPanelConditionWithName(String s){
		for (PanelCondition p : this.viewBrain.getViewEditor().getPanelcondition()) {
			if(p.getModele().getName().equals(s))
				return p;
		}
		return null;
	}


	public void addState(ModeleState state) {
		this.modeleBrain.addState(state);
		
		//Cr�ation du panel
		PanelState panel = new PanelState(state);
		//Ajoute le panel 
		this.viewBrain.getViewEditor().addState(panel);		
	}


	public void addCondition(ModeleCondition condition) {

		//Dit au modele d'ajouter la nouvelle condition
		this.modeleBrain.addCondition(condition);
		
		//Dit � la vu d'ajouter la nouvelle condition
		this.viewBrain.addCondition(condition);
		
	}
	

}
