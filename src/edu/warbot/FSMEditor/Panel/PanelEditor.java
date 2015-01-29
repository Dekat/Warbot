package edu.warbot.FSMEditor.Panel;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import edu.warbot.FSMEditor.Modele.Modele;
import edu.warbot.FSMEditor.Modele.ModeleBrain;

public class PanelEditor extends JPanel {

	private static final long serialVersionUID = 1L;

	ArrayList<PanelState> panelSates = new ArrayList<>();
	ArrayList<PanelCondition> panelConditions = new ArrayList<>();

	ModeleBrain modele;
	String panelName;

	public PanelEditor(Modele modele, String panelName) {
		this.modele = modele.getModeleExplorer();
		this.panelName = panelName;
//		this.setLayout(new CardLayout());
	}

	public void paintComponent(Graphics g) {

		g.clearRect(0, 0, this.getWidth(), this.getHeight());

		g.drawRect(0, 0, this.getSize().width, this.getSize().height);

		paintState(g);
		paintConditions(g);

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
		for (PanelCondition c : this.panelConditions) {
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

	public void addCondition(PanelCondition pc) {
		this.panelConditions.add(pc);
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
		return this.panelConditions;
	}
}
