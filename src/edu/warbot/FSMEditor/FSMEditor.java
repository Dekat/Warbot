package edu.warbot.FSMEditor;

import edu.warbot.FSMEditor.Modele.Modele;

public class FSMEditor {
	
	public FSMEditor() {
		Modele modele = new Modele();
		
		Frame vu = new Frame(modele);
		
		Controleur controleur = new Controleur(modele, vu);
		
	}

}
