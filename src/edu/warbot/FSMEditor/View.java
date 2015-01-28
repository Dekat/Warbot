package edu.warbot.FSMEditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;

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
import edu.warbot.FSMEditor.Modele.ModeleBrain;
import edu.warbot.FSMEditor.Panel.PanelCenter;

public class View extends JFrame {

	private static final long serialVersionUID = 1L;
	
	Modele modele;

	public View(Modele modele) {
		this.modele = modele;
		createView();
	}

	public void createView() {

		this.setName("FSMEditor");
		this.setSize(new Dimension(800, 600));
		this.setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel();
		this.setContentPane(mainPanel);

		mainPanel.setLayout(new BorderLayout());

		// left panel les boutons
		JPanel panelLeft = new JPanel(new VerticalLayout());
		mainPanel.add(getPanelLeft(), BorderLayout.WEST);
		
		// Center Panel
		panelCenter = new PanelCenter(this.modele);
		mainPanel.add(panelCenter, BorderLayout.CENTER);

		// Panel droite
		
		//Menu barre
		this.setMenuBar(getMainMenuBar());

		this.setVisible(true);

		// mainPanelEditor.afficherEnvironnement();
	}

	private MenuBar getMainMenuBar() {
		MenuBar mb = new MenuBar();

		miSave = new MenuItem("Save");
		miLoad = new MenuItem("Load");

		Menu mSave = new Menu("Save");
		Menu mLoad = new Menu("Load");
		
		mSave.add(miSave);
		mLoad.add(miLoad);
		
		mb.add(mSave);
		mb.add(mLoad);
		
		return mb;
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
		buttonDelSate = new JButton("Delete State");

		panel.add(buttonAddSate);
		panel.add(new JSeparator());
		panel.add(buttonEditState);
		panel.add(new JSeparator());
		panel.add(buttonDelSate);
		
		return panel;
	}

	/*** Accesseurs ***/
	
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
	
	private MenuItem miSave;
	private MenuItem miLoad;
	
	public MenuItem getMenuBarItemSave() {
		return miSave;
	}

	public MenuItem getMenuBarItemLoad() {
		return miLoad;
	}

	private JList<String> listCond;
	private DefaultListModel<String> listModeleCond;
	
	private PanelCenter panelCenter;

	public JList<String> getListeCondition() {
		return this.listCond;
	}

}
