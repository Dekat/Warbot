package edu.warbot.brains.adapters;

import edu.warbot.agents.agents.WarEngineer;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.MovableWarAgentAdapter;
import edu.warbot.brains.capacities.Builder;
import edu.warbot.brains.capacities.Creator;

public class WarEngineerAdapter extends MovableWarAgentAdapter implements Creator, Builder {

	public WarEngineerAdapter(WarEngineer agent) {
		super(agent);
	}

	@Override
	protected WarEngineer getAgent() {
		return (WarEngineer) super.getAgent();
	}
	
	@Override
	public void setNextAgentToCreate(WarAgentType nextAgentToCreate) {
		getAgent().setNextAgentToCreate(nextAgentToCreate);
	}

	@Override
	public WarAgentType getNextAgentToCreate() {
		return getAgent().getNextAgentToCreate();
	}

	@Override
	public boolean isAbleToCreate(WarAgentType agent) {
		return getAgent().isAbleToCreate(agent);
	}

    @Override
    public void setNextBuildingToBuild(WarAgentType nextBuildingToBuild) {
        getAgent().setNextBuildingToBuild(nextBuildingToBuild);
    }

    @Override
    public WarAgentType getNextBuildingToBuild() {
        return getAgent().getNextBuildingToBuild();
    }

    @Override
    public boolean isAbleToBuild(WarAgentType building) {
        return getAgent().isAbleToBuild(building);
    }

    @Override
    public void setIdNextBuildingToRepair(int idNextBuildingToRepair) {
        getAgent().setIdNextBuildingToRepair(idNextBuildingToRepair);
    }

    @Override
    public int getIdNextBuildingToRepair() {
        return getAgent().getIdNextBuildingToRepair();
    }
}
