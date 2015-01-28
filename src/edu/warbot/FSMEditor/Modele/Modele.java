package edu.warbot.FSMEditor.Modele;

import java.util.ArrayList;

public class Modele {

	ArrayList<ModeleBrain> modeleBrains = new ArrayList<ModeleBrain>();
	
	public Modele() {
		//Ici on ajoute un modele pour les explorer
		ModeleBrain modeleBrainExplorer = new ModeleBrain("Explorer");
		modeleBrains.add(modeleBrainExplorer);
	}

	public ArrayList<ModeleBrain> getModeleBrains() {
		return modeleBrains;
	}

	public ModeleBrain getModeleExplorer() {
		for (ModeleBrain modeleBrain : modeleBrains) {
			if(modeleBrain.getAgentType().equals("Explorer"))
				return modeleBrain;
		}
		return null;
	}

}
