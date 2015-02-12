package edu.warbot.FSMEditor.models;

import java.util.ArrayList;

import edu.warbot.agents.enums.WarAgentType;

public class Modele {

	ArrayList<ModeleBrain> modelBrains = new ArrayList<>();
	private boolean isRebuild = false;
	
	public void update() {
		//TODO ici a prioris pour l'instant ya rien a faire
	}

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
	
	public void createModelBrain(WarAgentType agentType){
		this.modelBrains.add(new ModeleBrain(agentType));
	}

	public boolean isRebuild() {
		return this.isRebuild;
	}
	
	public void setIsRebuild(boolean b) {
		isRebuild = b;
	}

}
