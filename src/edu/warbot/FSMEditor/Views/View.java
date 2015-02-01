package edu.warbot.FSMEditor.Views;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.warbot.FSMEditor.Modeles.Modele;
import edu.warbot.FSMEditor.Modeles.ModeleBrain;

public class View extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	ArrayList<ViewBrain> viewBrains = new ArrayList<ViewBrain>();

	private Modele modele;
	
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
		CardLayout cardLayout = new CardLayout();
		JPanel panel = new JPanel(cardLayout);
		for (ViewBrain viewBrain : viewBrains) {
			panel.add(viewBrain);
		}
		this.setContentPane(panel);
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
		miSaveJar = new MenuItem("Export Jar");

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

	/*** Attributs ***/
	
	private MenuItem miSave;
	private MenuItem miLoad;
	private MenuItem miSaveJar;


}
