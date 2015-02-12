package edu.warbot.FSMEditor.panels;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import edu.warbot.FSMEditor.models.ModelCondition;
import edu.warbot.FSMEditor.models.ModelState;
import edu.warbot.FSMEditor.models.ModeleBrain;

public class PanelEditor extends JPanel {

	private static final long serialVersionUID = 1L;

	ArrayList<PanelState> panelSates = new ArrayList<>();
	ArrayList<PanelCondition> panelsCondition = new ArrayList<>();

	ModeleBrain modelBrain;
	
	public PanelEditor(ModeleBrain model) {
		this.modelBrain = model;
		createComposant();
	}

	private void createComposant() {
		for (ModelState modelState : modelBrain.getStates()) {
			panelSates.add(new PanelState(modelState));
		}
		for (ModelCondition modelCond : modelBrain.getConditions()) {
			PanelCondition pc = new PanelCondition(modelCond);
			pc.setPanelSourceAndDestination(
					getPanelStateForModele(modelCond.getStateSource()), 
					getPanelStateForModele(modelCond.getStateDestination()));
			panelsCondition.add(pc);
		}		
	}
	
	//TODO pas super propre
	private PanelState getPanelStateForModele(ModelState model) {
		for (PanelState panel: panelSates) {
			if(panel.getModelState().equals(model))
				return panel;
		}
		return null;
	}

	public void paintComponent(Graphics g) {

		g.clearRect(0, 0, this.getWidth(), this.getHeight());

		g.drawRect(0, 0, this.getSize().width, this.getSize().height);

		paintConditions(g);
		paintState(g);

	}
	
	public ArrayList<PanelState> getPanelState(){
		return this.panelSates;
	}

	public void paintState(Graphics g) {
		for (PanelState p : this.panelSates) {
			p.paint(g);
		}
	}
	
	public void paintConditions(Graphics g) {
		for (PanelCondition c : this.panelsCondition) {
			c.paint(g);
		}
	}

	public void clickOnState(PanelState s) {
		if(s.isSelected){
			s.isSelected = false;
			this.selectedState.remove(s);
		}else{
			s.isSelected = true;
			this.selectedState.add(s);
		}
	}

	public void addCondition(ModelCondition modelCondition) {
		PanelCondition pc = new PanelCondition(modelCondition);
		pc.setPanelSourceAndDestination(
				getPanelStateForModele(modelCondition.getStateSource()), 
				getPanelStateForModele(modelCondition.getStateDestination()));
		panelsCondition.add(pc);
	}

	public void addState(PanelState panel) {
		this.panelSates.add(panel);
	}

	public void setNoItemSelected() {
		for(PanelState p : this.selectedState){
			if(p.isSelected)
				p.isSelected = false;
		}
		this.selectedState.clear();
	}

	public PanelState getFirstSelectedState() {
		if(this.selectedState.size() > 0)
			return this.selectedState.get(0);
		else{
			System.out.println(this.getClass() + "no item selected");
			return null;
		}
	}

	public PanelState getSecondeSelectedState() {
		if(this.selectedState.size() > 1)
			return this.selectedState.get(1);
		else{
			System.out.println(this.getClass() + "no enough items selected");
			return null;
		}
	}

	public boolean isOneStateSelected() {
		return this.selectedState.size() == 1;
	}

	public boolean isTwoStatesSelected() {
		return this.selectedState.size() == 2;
	}

	private ArrayList<PanelState> selectedState = new ArrayList<>();

	public void removePanelState(PanelState p){
		this.panelSates.remove(p);
		//TODO rebuild la listes des panels ?  			
	}

	public ArrayList<PanelCondition> getPanelcondition() {
		return this.panelsCondition;
	}

	public void createState(ModelState modelState) {
		// TODO Auto-generated method stub
		
	}
}
