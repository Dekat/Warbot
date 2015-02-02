package edu.warbot.FSMEditor.Modeles;

import java.util.ArrayList;

import edu.warbot.agents.enums.WarAgentType;

public class Modele {

	ArrayList<ModeleBrain> modelBrains = new ArrayList<ModeleBrain>();
	
	public ArrayList<ModeleBrain> getModelsBrains() {
		return modelBrains;
	}

	public ModeleBrain getModelBrain(WarAgentType agentType) {
		for (ModeleBrain modeleBrain : modelBrains) {
			if(modeleBrain.getAgentType().equals(agentType))
				return modeleBrain;
		}
		return null;
	}

	public void addModelBrain(ModeleBrain modelBrain) {
		this.modelBrains.add(modelBrain);
	}

}
