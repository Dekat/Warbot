package edu.warbot.FSMEditor.Controleurs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.warbot.FSMEditor.Configuration;
import edu.warbot.FSMEditor.FSMJarGenerator;
import edu.warbot.FSMEditor.FSMXMLSaver;
import edu.warbot.FSMEditor.MouseListenerPanelCenter;
import edu.warbot.FSMEditor.View;
import edu.warbot.FSMEditor.Modele.Modele;
import edu.warbot.FSMEditor.Modele.ModeleBrain;
import edu.warbot.FSMEditor.Modele.ModeleCondition;
import edu.warbot.FSMEditor.Modele.ModeleState;
import edu.warbot.FSMEditor.Panel.PanelCondition;
import edu.warbot.FSMEditor.Panel.PanelState;
import edu.warbot.FSMEditor.dialogues.DialogueCondSetting;
import edu.warbot.FSMEditor.dialogues.DialogueStateSetting;

public class ControleurBrain {

	public ModeleBrain modele;
	public View view;
	
	public ControleurBrain(ModeleBrain modele, View view) {
		this.modele = modele;
		this.view = view;
		
		placeListenerOnView();
		placeListeerOnPanel();

	}
	
	
	private void placeListeerOnPanel() {
		MouseListenerPanelCenter mouseListener = new MouseListenerPanelCenter(this);
		view.getPanelCenter().addMouseListener(mouseListener);
		view.getPanelCenter().addMouseMotionListener(mouseListener);		
	}

	private void placeListenerOnView(){
		
		view.getButtonAddSate().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eventAddSate();
			}
		});
		
		view.getButtonAddCond().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eventAddCond();
			}
		});
		
		view.getButtonDelState().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eventDelState();
			}
		});
		
		view.getButtonEditCond().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eventEditCond();
			}
		});
		
		view.getListeCondition().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				eventListeConditionEdition(e);
			}
		});
		
	}
	
	private void eventListeConditionEdition(ListSelectionEvent e){
		//Deselctionne tous les elements
		for (PanelCondition p : this.view.getPanelCenter().getPanelcondition()) {
			p.isSelected = false;
		}
		
		String stringCond = view.getListeCondition().getSelectedValue();
		PanelCondition panelCond = this.getPanelConditionWithName(stringCond);
		
		panelCond.isSelected = true;
		
		this.view.getPanelCenter().repaint();
	}
	
	private void eventAddSate(){
		DialogueStateSetting d = new DialogueStateSetting(this.view); 

		if(d.isValideComponent()){
			//creation du modele
			ModeleState s = new ModeleState(d);
			this.modele.addState(s);
			
			//Ajoute du panel
			PanelState panel = new PanelState(s);
			this.view.getPanelCenter().addState(panel);
			
			view.getPanelCenter().repaint();
		}
		
	}
	
	private void eventAddCond(){
		
		if(this.view.getPanelCenter().isTwoStatesSelected()){
			
			DialogueCondSetting d = new DialogueCondSetting(this.view);
			
			PanelState panelSource;
			PanelState panelDest;
			ModeleState modeleSource;
			ModeleState modeleDest;
			
			panelSource = this.view.getPanelCenter().getFirstSelectedState();
			panelDest = this.view.getPanelCenter().getSecondeSelectedState();
			modeleSource = panelSource.getModele();
			modeleDest = panelDest.getModele();
			
			//Crée un nouveau modele condition et le donne au modele
			ModeleCondition mc = new ModeleCondition(d);
			this.modele.addCondition(mc);
			
			//Crée un nouveau panel condition et le donne au panel
			PanelCondition pc = new PanelCondition(mc);
			this.view.getPanelCenter().addCondition(pc);
			
			//Donne au modele source le modele condition
			modeleSource.addCondition(mc);
			//Donne au modele condition source le modele destination
			mc.setDestination(modeleDest);
			
			//Donne au panel condition le panel state source et destination
			pc.setPanelSourceAndDestination(panelSource, panelDest);
			
			//Met a jour la liste de conditions dans la vu
			this.view.getListeModeleConditions().addElement(mc.getNom());
			
		}else{
			System.out.println("Pour ajouter une condition deux etats doivent etre selectionnés");
		}
		
		view.getPanelCenter().repaint();
	}
	
	private void eventDelState(){
		if(this.view.getPanelCenter().isOneStateSelected()){
			
			PanelState panelToDelet = this.view.getPanelCenter().getFirstSelectedState();
			
			//ATTENTION : supprimer le modele avant le panel puisque on utilise le panel pour acceder au modele
			this.modele.removeState(panelToDelet.getModele());
			this.view.getPanelCenter().removePanelState(panelToDelet);
			
			this.view.getPanelCenter().setNoItemSelected();
			
			view.getPanelCenter().repaint();
		}
		
	}

	private void eventEditCond(){
		String condSelec = this.view.getListeConditions().getSelectedValue();
		
		if(condSelec != null){
			
			ModeleCondition modeleCond;
			
			for (ModeleCondition modeleC : this.modele.getConditions()) {
				if(modeleC.getNom().equals(condSelec)){
					modeleCond = modeleC;
					break;
				}
			}
			
			if(condSelec.equals(Configuration.WarConditionActionTerminate)){
				
				
				
			}
		}
	}
	
	private PanelCondition getPanelConditionWithName(String s){
		for (PanelCondition p : this.view.getPanelCenter().getPanelcondition()) {
			if(p.getModele().getNom().equals(s))
				return p;
		}
		return null;
	}
	

}
