package edu.warbot.agents.agents;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.WarAgent;
import edu.warbot.agents.WarBuilding;
import edu.warbot.agents.actions.BuilderActions;
import edu.warbot.agents.actions.CreatorActions;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.adapters.WarEngineerAdapter;
import edu.warbot.brains.capacities.Builder;
import edu.warbot.brains.capacities.Creator;
import edu.warbot.game.Team;
import edu.warbot.launcher.WarGameConfig;

import java.util.HashMap;

public class WarEngineer extends MovableWarAgent implements CreatorActions, Creator, BuilderActions, Builder {
	
	public static final double ANGLE_OF_VIEW;
	public static final double DISTANCE_OF_VIEW;
	public static final int COST;
	public static final int MAX_HEALTH;
	public static final int BAG_SIZE;
	public static final double SPEED;
    public static final int MAX_REPAIRS_PER_TICK;
	
	private WarAgentType nextAgentToCreate;
    private WarAgentType nextBuildingToBuild;
    private int idNextBuildingToRepair;

	static {
		HashMap<String, String> data = WarGameConfig.getConfigOfWarAgent(WarAgentType.WarEngineer);
		ANGLE_OF_VIEW = Double.valueOf(data.get(WarGameConfig.AGENT_CONFIG_ANGLE_OF_VIEW));
		DISTANCE_OF_VIEW = Double.valueOf(data.get(WarGameConfig.AGENT_CONFIG_DISTANCE_OF_VIEW));
		COST = Integer.valueOf(data.get(WarGameConfig.AGENT_CONFIG_COST));
		MAX_HEALTH = Integer.valueOf(data.get(WarGameConfig.AGENT_CONFIG_MAX_HEALTH));
		BAG_SIZE = Integer.valueOf(data.get(WarGameConfig.AGENT_CONFIG_BAG_SIZE));
		SPEED = Double.valueOf(data.get(WarGameConfig.AGENT_CONFIG_SPEED));
        MAX_REPAIRS_PER_TICK = Integer.valueOf(data.get(WarGameConfig.AGENT_CONFIG_MAX_REPAIRS_PER_TICK));
	}
	
	public WarEngineer(Team team, WarBrain<WarEngineerAdapter> brain) {
		super(ACTION_IDLE, team, WarGameConfig.getHitboxOfWarAgent(WarAgentType.WarEngineer), brain, DISTANCE_OF_VIEW, ANGLE_OF_VIEW, COST, MAX_HEALTH, BAG_SIZE, SPEED);
		
		brain.setAgentAdapter(new WarEngineerAdapter(this));

        nextAgentToCreate = WarAgentType.WarTurret;
        nextBuildingToBuild = WarAgentType.Wall;
	}

	@Override
	public String create() {
		getTeam().createUnit(this, nextAgentToCreate);
		return getBrain().action();
	}

    @Override
    public String build() {
        getTeam().build(this, nextBuildingToBuild);
        return getBrain().action();
    }

    @Override
    public String repair() {
        if(getHealth() > WarBuilding.getCostToRepair(MAX_REPAIRS_PER_TICK)) {
            WarAgent agentToRepair = getTeam().getAgentWithID(idNextBuildingToRepair);
            if (agentToRepair != null) {
                if (agentToRepair instanceof WarBuilding) {
                    ((WarBuilding) agentToRepair).heal(MAX_REPAIRS_PER_TICK);
                    damage(WarBuilding.getCostToRepair(MAX_REPAIRS_PER_TICK));
                }
            }
        }
        return getBrain().action();
    }

    @Override
	public void setNextAgentToCreate(WarAgentType nextAgentToCreate) {
		this.nextAgentToCreate = nextAgentToCreate;
	}

	@Override
	public WarAgentType getNextAgentToCreate() {
		return nextAgentToCreate;
	}

	@Override
	public boolean isAbleToCreate(WarAgentType agent) {
        return agent == WarAgentType.WarTurret;
	}

    @Override
    public void setNextBuildingToBuild(WarAgentType nextBuildingToBuild) {
        this.nextBuildingToBuild = nextBuildingToBuild;
    }

    @Override
    public WarAgentType getNextBuildingToBuild() {
        return nextBuildingToBuild;
    }

    @Override
    public boolean isAbleToBuild(WarAgentType building) {
        return building == WarAgentType.Wall;
    }

    @Override
    public void setIdNextBuildingToRepair(int idNextBuildingToRepair) {
        this.idNextBuildingToRepair = idNextBuildingToRepair;
    }

    @Override
    public int getIdNextBuildingToRepair() {
        return idNextBuildingToRepair;
    }

}
