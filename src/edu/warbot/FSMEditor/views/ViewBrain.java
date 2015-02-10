package edu.warbot.FSMEditor.views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.TitledBorder;

import org.jfree.ui.tabbedui.VerticalLayout;

import edu.warbot.FSMEditor.models.ModeleBrain;
import edu.warbot.FSMEditor.models.ModeleCondition;
import edu.warbot.FSMEditor.models.ModeleState;
import edu.warbot.FSMEditor.panels.PanelCondition;
import edu.warbot.FSMEditor.panels.PanelEditor;
import edu.warbot.FSMEditor.panels.PanelState;

public class ViewBrain extends JPanel{

	private static final long serialVersionUID = 1L;

	ModeleBrain modeleBrain;

	public ViewBrain(ModeleBrain modeleBrain) {
		this.modeleBrain = modeleBrain;
		createView();
	}

	public void createView() {

		this.setLayout(new BorderLayout());

		// left panel les boutons
		this.add(getPanelLeft(), BorderLayout.WEST);
		
		// Panel center (l'éditeur)
		panelEditor = new PanelEditor(this.modeleBrain, "Explorer (class ViewBrain)");
		
		this.add(panelEditor, BorderLayout.CENTER);

		// Panel droite
		
		// mainPanelEditor.afficherEnvironnement();
	}

	private JPanel getPanelLeft() {
		JPanel p = new JPanel(new GridLayout(2, 1));
		p.add(getPanelState());
		p.add(getPanelCondition());
		return p;
	}

	private JPanel getPanelCondition() {
		JPanel panel = new JPanel(new VerticalLayout());
		panel.setBorder(new TitledBorder("Gestion des conditions"));

		buttonAddCond = new JButton("Add condition");
		buttonEditCond = new JButton("Edit condition");
		buttonDelCond = new JButton("Delete condition");
		
		listModeleCond = new DefaultListModel<String>();
		listModeleCond.addElement("None");
		
		listCond = new JList<String>(listModeleCond);
		listCond.setVisibleRowCount(5); //TODO ici ca serait mieux si il affiche un truc de taille dynamique
		
		panel.add(buttonAddCond);
		panel.add(new JSeparator());
		panel.add(buttonDelCond);
		panel.add(new JSeparator());
		panel.add(buttonEditCond);
		panel.add(new JSeparator());
		panel.add(listCond);
		
		return panel;
	}

	private Component getPanelState() {
		JPanel panel = new JPanel(new VerticalLayout());
		panel.setBorder(new TitledBorder("Gestion des états"));
		
		buttonAddSate = new JButton("Add State");
		buttonEditState = new JButton("Edit State");
		buttonDelSate = new JButton("Delete State");

		panel.add(buttonAddSate);
		panel.add(new JSeparator());
		panel.add(buttonEditState);
		panel.add(new JSeparator());
		panel.add(buttonDelSate);
		
		return panel;
	}

	public void addState(ModeleState state) {
		this.panelEditor.addState(new PanelState(state));
	}

	public void addCondition(ModeleCondition condition) {
		//Crée le nouveau panel condition
		PanelCondition pc = new PanelCondition(condition);	
	
		//Récupère les panelSource et destination pour avoir leurs positions
		PanelState panelSource = getPanelStateForModele(condition.getStateSource());
		PanelState panelDest = getPanelStateForModele(condition.getStateDestination());
		
		pc.setPanelSourceAndDestination(panelSource, panelDest);
		
		panelEditor.addCondition(pc);
		
		this.getListeModeleConditions().addElement(condition.getName());	
	}

	public PanelEditor getViewEditor() {
		return (PanelEditor) this.panelEditor;
	}

	public ModeleBrain getModele(){
		return this.modeleBrain;
	}	

	public JList<String> getListeConditions(){
		return this.listCond;
	}

	public JList<String> getListeCondition() {
		return this.listCond;
	}

	public DefaultListModel<String> getListeModeleConditions() {
		return this.listModeleCond;
	}

	/*** Accesseurs ***/
	
	public JButton getButtonAddState() {
		return this.buttonAddSate;
	}

	public AbstractButton getButtonDelState() {
		return this.buttonDelSate;
	}

	public JButton getButtonEditState() {
		return this.buttonEditState;
	}

	public JButton getButtonAddCond() {
		return this.buttonAddCond;
	}

	public JButton getButtonEditCond() {
		return this.buttonEditCond;
	}

	/**** Attributs ***/
	private JList<String> listCond;
	private DefaultListModel<String> listModeleCond;
	
	private PanelEditor panelEditor;

	private JButton buttonAddSate;
	private JButton buttonEditState;
	private JButton buttonDelSate;
	private JButton buttonAddCond;
	private JButton buttonEditCond;
	private JButton buttonDelCond;

	//TODO pas super propre
	private PanelState getPanelStateForModele(ModeleState modele) {
		for (PanelState panel: panelEditor.getPanelState()) {
			if(panel.getModelState().equals(modele))
				return panel;
		}
		return null;
	}
	
	

}
