package edu.warbot.FSMEditor.views;

import java.awt.Dimension;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import edu.warbot.FSMEditor.models.Model;
import edu.warbot.FSMEditor.models.ModeleBrain;

public class View extends JFrame {

	private static final long serialVersionUID = 1L;

	private Model modele;
	
	private ArrayList<ViewBrain> viewBrains = new ArrayList<>();
	
	private JTabbedPane mainPanel = new JTabbedPane();
	
	public View(Model modele) {
		this.modele = modele;
		
		createViewBrains();
		
		createFrame();
	}
	
	public void update(){
		//On reconstruit la liste des vu
//		modele.update(); necessaire ou pas ?
		viewBrains.clear();
		mainPanel.removeAll();
		createViewBrains();
	}

	private void createViewBrains() {
//		viewBrains = new ArrayList<>();
//		mainPanel = new JTabbedPane();
		
		for (ModeleBrain modeleBrain : modele.getModelsBrains()) {
			ViewBrain vb = new ViewBrain(modeleBrain);
			modeleBrain.setViewBrain(vb);
			
			viewBrains.add(vb);
			mainPanel.add(vb.getModel().getAgentTypeName(), vb);
		}
	}

	private void createFrame() {
		
		this.setName("FSMEditor");
		this.setSize(new Dimension(800, 600));
		this.setLocationRelativeTo(null);
		
		this.setMenuBar(getMainMenuBar());
		
		this.setContentPane(mainPanel);

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
	
	public void setModel(Model model) {
		this.modele = model;
	}
	
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
		mainPanel.add(viewBrain.getModel().getAgentTypeName(), viewBrain);
	}

	/*** Attributs ***/
	
	private MenuItem miSave;
	private MenuItem miLoad;
	private MenuItem miSaveJar;






}
