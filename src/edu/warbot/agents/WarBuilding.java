package edu.warbot.agents;

import edu.warbot.game.Team;
import edu.warbot.launcher.WarConfig;

public abstract class WarBuilding extends AliveWarAgent {

    public static final double MAX_DISTANCE_BUILD = WarConfig.getMaxDistanceBuild();
    public static final double REPAIRS_MULTIPLIER = WarConfig.getRepairsMultiplier();

    public WarBuilding(Team team, Hitbox hitbox, int cost, int maxHealth) {
		super(ACTION_IDLE, team, hitbox, cost, maxHealth);
        init(getRepairsAmountWithCost(cost));
	}

	@Override
	public String idle() {
		return ACTION_IDLE;
	}

    public static int getRepairsAmountWithCost(int cost) {
        return ((Double) (REPAIRS_MULTIPLIER * cost)).intValue();
    }

    public static int getCostToRepair(int repairsAmout) {
        return ((Double) (repairsAmout / REPAIRS_MULTIPLIER)).intValue();
    }

}
