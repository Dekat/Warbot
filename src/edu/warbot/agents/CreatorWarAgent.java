package edu.warbot.agents;

import java.util.logging.Level;

import edu.warbot.agents.actions.CreatorActions;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.CreatorWarAgentAdapter;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.capacities.Creator;
import edu.warbot.game.Game;
import edu.warbot.game.Team;

@SuppressWarnings("serial")
public abstract class CreatorWarAgent extends ControllableWarAgent implements CreatorActions, Creator {

	private WarAgentType _nextAgentToCreate;

	public CreatorWarAgent(String firstActionToDo, Team team, double hitboxRadius, WarBrain<? extends CreatorWarAgentAdapter> brainController, double distanceOfView, double angleOfView, int cost,	int maxHealth, int bagSize) {
		super(firstActionToDo, team, hitboxRadius, brainController, distanceOfView, angleOfView, cost, maxHealth, bagSize);

		_nextAgentToCreate = WarAgentType.WarExplorer;
	}

	@Override
	public String create() {
		defaultCreateUnit(this, _nextAgentToCreate);
		return getBrain().action();
	}

	@Override
	public void setNextAgentToCreate(WarAgentType nextAgentToCreate) {
		_nextAgentToCreate = nextAgentToCreate;
	}

	@Override
	public WarAgentType getNextAgentToCreate() {
		return _nextAgentToCreate;
	}

	public static void defaultCreateUnit(Creator creatorAgent, WarAgentType agentTypeToCreate) {
		if (creatorAgent instanceof WarAgent) {
			// TODO ajout des conditions de création
			// TODO contrôler si la base n'est pas encerclée. Dans ce cas, elle ne pourra pas créer d'agent car il n'y aura pas de place.
			((WarAgent) creatorAgent).getLogger().log(Level.FINEST, creatorAgent.toString() + " creating " + agentTypeToCreate);
			try {
				if (creatorAgent.isAbleToCreate(agentTypeToCreate)) {
					WarAgent a = Game.instantiateNewControllableWarAgent(agentTypeToCreate.toString(), ((WarAgent) creatorAgent).getTeam());
					((WarAgent) creatorAgent).launchAgent(a);
					a.setPositionAroundOtherAgent(((WarAgent) creatorAgent));
					((ControllableWarAgent) creatorAgent).damage(((ControllableWarAgent) a).getCost());
					((WarAgent) creatorAgent).getLogger().log(Level.FINER, creatorAgent.toString() + " create " + agentTypeToCreate);
				} else {
					((WarAgent) creatorAgent).getLogger().log(Level.FINER, creatorAgent.toString() + " can't create " + agentTypeToCreate);
				}
			} catch (Exception e) {
				System.err.println("Erreur lors de l'instanciation du brainController de l'agent " + agentTypeToCreate.toString());
				e.printStackTrace();
			}
		}
	}
}
