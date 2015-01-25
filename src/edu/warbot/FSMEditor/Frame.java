package edu.warbot.FSMEditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.TitledBorder;

import org.jfree.ui.tabbedui.VerticalLayout;

import edu.warbot.FSMEditor.Modele.Modele;
import edu.warbot.FSMEditor.Panel.PanelCenter;

public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;
	Modele modele;

	public Frame(Modele modele) {
		this.modele = modele;
		initFrame();
	}

	public void initFrame() {

		this.setName("FSMEditor");
		this.setSize(new Dimension(800, 600));
		this.setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel();
		this.setContentPane(mainPanel);

		mainPanel.setLayout(new BorderLayout());

		// left panel les boutons

		JPanel panelLeft = new JPanel(new VerticalLayout());
		mainPanel.add(panelLeft, BorderLayout.WEST);
		panelLeft.setMinimumSize(new Dimension(200, 600));
		
		// panel pour les button des etats
		panelLeft.add(this.getPanelState());
		
		//panel pour les button des conditions
		panelLeft.add(this.getPanelCondition());
		
		// Center Panel
		panelCenter = new PanelCenter(this.modele);
		mainPanel.add(panelCenter, BorderLayout.CENTER);

		// Panel droite

		this.setVisible(true);

		// mainPanelEditor.afficherEnvironnement();
	}

	private Component getPanelCondition() {
		JPanel panel = new JPanel(new VerticalLayout());
		panel.setBorder(new TitledBorder("Gestion des onditions"));

		buttonAddCond = new JButton("Add cond");
		buttonEditCond = new JButton("Edit cond");
		buttonDelCond = new JButton("Del cond");
		
		listModeleCond = new DefaultListModel<String>();
		listCond = new JList<String>(listModeleCond);
		listCond.setVisibleRowCount(5); //TODO ici ca serait mieux si il affiche un truc de taille constante
		
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
		buttonDelSate = new JButton("Del State");

		panel.add(buttonAddSate);
		panel.add(new JSeparator());
		panel.add(buttonEditState);
		panel.add(new JSeparator());
		panel.add(buttonDelSate);
		
		return panel;
	}

	public JButton getButtonAddSate() {
		return this.buttonAddSate;
	}

	public PanelCenter getPanelCenter() {
		return this.panelCenter;
	}

	public JButton getButtonAddCond() {
		return this.buttonAddCond;
	}
	
	public Modele getModele(){
		return this.modele;
	}	

	public AbstractButton getButtonDelState() {
		return this.buttonDelSate;
	}
	
	public JList<String> getListeConditions(){
		return this.listCond;
	}

	public DefaultListModel<String> getListeModeleConditions() {
		return this.listModeleCond;
	}

	public JButton getButtonEditCond() {
		return this.buttonEditCond;
	}

	private JButton buttonAddSate;
	private JButton buttonEditState;
	private JButton buttonDelSate;
	private JButton buttonAddCond;
	private JButton buttonEditCond;
	private JButton buttonDelCond;
	
	private JList<String> listCond;
	private DefaultListModel<String> listModeleCond;
	
	private PanelCenter panelCenter;

	public JList<String> getListeCondition() {
		return this.listCond;
	}

}
