package edu.warbot.FSMEditor.controleurs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.warbot.FSM.WarGenericSettings.WarConditionSettings;
import edu.warbot.FSM.WarGenericSettings.WarPlanSettings;
import edu.warbot.FSMEditor.dialogues.DialogueCondSetting;
import edu.warbot.FSMEditor.dialogues.DialogueStateSetting;
import edu.warbot.FSMEditor.models.ModelCondition;
import edu.warbot.FSMEditor.models.ModelState;
import edu.warbot.FSMEditor.models.ModeleBrain;
import edu.warbot.FSMEditor.panels.PanelCondition;
import edu.warbot.FSMEditor.panels.PanelState;
import edu.warbot.FSMEditor.views.ViewBrain;

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
		
		DialogueStateSetting d = new DialogueStateSetting(this.viewBrain, planSetting);
		d.createDialog();

		if(d.isValideComponent()){
			
			ModelState s = new ModelState(d.getStateName(), d.getPlanName(), d.getPlanSettings());

			this.addState(s);
			
			viewBrain.getViewEditor().repaint();
		}
	}
	
	private void eventEditState(){
		ModelState modelState = 
				this.viewBrain.getViewEditor().getFirstSelectedState().getModelState();
		
		DialogueStateSetting d = new DialogueStateSetting(this.viewBrain, modelState);
		d.createDialog();
		
		if(d.isValideComponent()){
			
			modelState.setName(d.getStateName());
			modelState.setPlanName(d.getPlanName());
			modelState.setPlanSettings(d.getPlanSettings());
			
			viewBrain.getViewEditor().repaint();
		}
	}
	
	private void eventAddCond(){
		
		if(this.viewBrain.getViewEditor().isTwoStatesSelected()){
			
			WarConditionSettings condSett = new WarConditionSettings();

			DialogueCondSetting d = new DialogueCondSetting(this.viewBrain, condSett);
			d.createDialog();
			
			if(d.isValideComponent()){
			
				PanelState panelSource;
				PanelState panelDest;
				ModelState modeleStateSource;
				ModelState modeleStateDest;
				
				panelSource = this.viewBrain.getViewEditor().getFirstSelectedState();
				panelDest = this.viewBrain.getViewEditor().getSecondeSelectedState();
				modeleStateSource = panelSource.getModelState();
				modeleStateDest = panelDest.getModelState();
				
				//Crée le nouveau modele condition
				ModelCondition mc = new ModelCondition(d.getConditionName(), d.getConditionType(), condSett);
//						modeleStateSource, modeleStateDest);
				mc.setSource(modeleStateSource);
				mc.setDestination(modeleStateDest);
				
				addCondition(mc);
			}
			
			
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
			
			ModelCondition modeleCond;
			
			for (ModelCondition modeleC : this.modeleBrain.getConditions()) {
				if(modeleC.getName().equals(condSelec)){
					modeleCond = modeleC;
					break;
				}
			}
			
//			if(condSelec.equals(Settings.WarConditionActionTerminate)){
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


	public void addState(ModelState state) {
		//Dit au model le nouvel état
		this.modeleBrain.addState(state);
		
		//Dit à la vu le nouvel état
		this.viewBrain.addState(state);
	}


	public void addCondition(ModelCondition condition) {

		//Dit au modele d'ajouter la nouvelle condition
		this.modeleBrain.addCondition(condition);
		
		//Dit à la vu d'ajouter la nouvelle condition
		this.viewBrain.addCondition(condition);
		
	}
	

}
