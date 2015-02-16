package edu.warbot.FSMEditor.views;

import java.awt.Dimension;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

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
	
	public void loadModel(Model model) {
		this.modele = model;
		
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
		
		this.setJMenuBar(getMainMenuBar());
		
		this.setContentPane(mainPanel);
		
		this.setVisible(true);
		
	}
	
	private JMenuBar getMainMenuBar() {
		JMenuBar mb = new JMenuBar();

		miSave = new JMenuItem("Save");
		miOpen = new JMenuItem("Open");
		miTest = new JMenuItem("Test validity");
		miPrint = new JMenuItem("Print");
		
		miSave.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_S, ActionEvent.CTRL_MASK));

		miOpen.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		
		miTest.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_T, ActionEvent.CTRL_MASK));

		miPrint.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_P, ActionEvent.CTRL_MASK));

		JMenu mFile = new JMenu("File");
		JMenu mTest = new JMenu("Test");
		
		mFile.add(miSave);
		mFile.add(miOpen);
		
		mTest.add(miTest);
		mTest.add(miPrint);
		
		mb.add(mFile);
		mb.add(mTest);
		
		return mb;
	}
	
	/*** Accesseurs ***/
	
	public JMenuItem getMenuBarItemSave() {
		return miSave;
	}

	public JMenuItem getMenuBarItemLoad() {
		return miOpen;
	}
	
	public JMenuItem getMenuBarItemTest() {
		return miTest;
	}
	
	public JMenuItem getMenuBarItemPrint() {
		return miPrint;
	}

	public ArrayList<ViewBrain> getViewBrains() {
		return this.viewBrains;
	}
	
	public void addViewBrain(ViewBrain viewBrain) {
		this.viewBrains.add(viewBrain);
		mainPanel.add(viewBrain.getModel().getAgentTypeName(), viewBrain);
	}

	/*** Attributs ***/
	
	private JMenuItem miSave;
	private JMenuItem miOpen;
	private JMenuItem miTest;
	private JMenuItem miPrint;






}
