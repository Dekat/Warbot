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
		panelSates = new ArrayList<>();
		panelsCondition = new ArrayList<>();
		
		//Crée les états
		for (ModelState modelState : modelBrain.getStates()) {
			addState(modelState);
		}
		//Crée les conditions
		for (ModelCondition modelCond : modelBrain.getConditions()) {
			addCondition(modelCond);
		}	
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

	public void addState(ModelState modelState) {
		PanelState ps = new PanelState(modelState);
		modelState.setViewState(ps);
		panelSates.add(ps);
	}

	public void addCondition(ModelCondition modelCondition) {
		PanelCondition pc = new PanelCondition(modelCondition);
		panelsCondition.add(pc);
	}

	public void unselectAllItems() {
		for(PanelState p : this.selectedState){
			p.isSelected = false;
		}
		
		if(selectedCondition != null)
			this.selectedCondition.isSelected = false;
		
		this.selectedCondition = null;
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
	
	public ArrayList<PanelState> getSelectedStates(){
		return selectedState;
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
	private PanelCondition selectedCondition;

	public void removePanelState(PanelState p){
		this.panelSates.remove(p);
	}
	
	public PanelCondition getSelectedCondition() {
		return selectedCondition;
	}
	
	public void setSelectedCondition(PanelCondition panelCondition) {
		this.selectedCondition = panelCondition;
	}

	public ArrayList<PanelCondition> getPanelconditions() {
		return this.panelsCondition;
	}
}
