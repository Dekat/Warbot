package edu.warbot.FSMEditor.views;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import edu.warbot.FSMEditor.models.Modele;
import edu.warbot.FSMEditor.models.ModeleBrain;

public class View extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	ArrayList<ViewBrain> viewBrains = new ArrayList<ViewBrain>();

	private Modele modele;
	
	private JTabbedPane mainPanel;
	
	public View(Modele modele) {
		this.modele = modele;
		
		initiateViewBrains();
		
		createFrame();
		
		createPanel();
	}

	private void initiateViewBrains() {
		for (ModeleBrain modeleBrain : modele.getModelsBrains()) {
			viewBrains.add(new ViewBrain(modeleBrain));
		}
	}

	private void createPanel() {//TODO (si on veut rajouter des nouveaux panel il faut les remetre dans ce panel pricipal
		mainPanel = new JTabbedPane();
		for (ViewBrain viewBrain : viewBrains) {
			mainPanel.add(viewBrain.getModele().getAgentTypeName(), viewBrain);
		}
		this.setContentPane(mainPanel);
	}

	private void createFrame() {
		
		this.setName("FSMEditor");
		this.setSize(new Dimension(800, 600));
		this.setLocationRelativeTo(null);
		
		this.setMenuBar(getMainMenuBar());

		this.setVisible(true);
		
	}
	
	private MenuBar getMainMenuBar() {
		MenuBar mb = new MenuBar();

		miSave = new MenuItem("Save");
		miLoad = new MenuItem("Load");
		miSaveJar = new MenuItem("Test validity");

		Menu mSave = new Menu("Save");
		Menu mLoad = new Menu("Load");
		
		mSave.add(miSave);
		mSave.add(miSaveJar);
		mLoad.add(miLoad);
		
		mb.add(mSave);
		mb.add(mLoad);
		
		return mb;
	}
	
	/*** Accesseurs ***/
	
	public MenuItem getMenuBarItemSave() {
		return miSave;
	}

	public MenuItem getMenuBarItemLoad() {
		return miLoad;
	}
	
	public MenuItem getMenuBarItemSaveJar() {
		return miSaveJar;
	}
	
	public ArrayList<ViewBrain> getViewBrains() {
		return this.viewBrains;
	}
	
	public void addViewBrain(ViewBrain viewBrain) {
		this.viewBrains.add(viewBrain);
		mainPanel.add(viewBrain.getModele().getAgentTypeName(), viewBrain);
	}

	/*** Attributs ***/
	
	private MenuItem miSave;
	private MenuItem miLoad;
	private MenuItem miSaveJar;




}
