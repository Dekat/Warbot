package edu.warbot.FSMEditor.Modeles;

import java.util.ArrayList;

import edu.warbot.agents.enums.WarAgentType;

public class Modele {

	ArrayList<ModeleBrain> modeleBrains = new ArrayList<ModeleBrain>();
	
	public Modele() {
		//TODO on ajoute un modele que pour les explorers (pour l'instant)
		ModeleBrain modeleBrainExplorer = new ModeleBrain(WarAgentType.WarExplorer.name());
		modeleBrains.add(modeleBrainExplorer);
	}

	public ArrayList<ModeleBrain> getModeleBrains() {
		return modeleBrains;
	}

	public ModeleBrain getModeleExplorer() {
		for (ModeleBrain modeleBrain : modeleBrains) {
			if(modeleBrain.getAgentType().equals(WarAgentType.WarExplorer.name()))
				return modeleBrain;
		}
		return null;
	}

}
