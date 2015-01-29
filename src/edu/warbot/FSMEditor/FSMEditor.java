package edu.warbot.FSMEditor;

import edu.warbot.FSMEditor.Controleurs.Controleur;
import edu.warbot.FSMEditor.Modele.Modele;

public class FSMEditor {
	
	public FSMEditor() {
		Modele modele = new Modele();
		
		View vu = new View(modele);
		
		Controleur controleur = new Controleur(modele, vu);
		
	}
}
