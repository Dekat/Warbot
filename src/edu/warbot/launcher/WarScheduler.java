package edu.warbot.launcher;

import java.util.Observable;
import java.util.Observer;

import madkit.action.KernelAction;
import madkit.agr.LocalCommunity;
import madkit.agr.LocalCommunity.Groups;
import madkit.agr.Organization;
import madkit.message.KernelMessage;
import madkit.simulation.activator.GenericBehaviorActivator;
import turtlekit.agr.TKOrganization;
import turtlekit.kernel.TKScheduler;
import edu.warbot.agents.WarAgent;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.game.Game;
import edu.warbot.game.Team;

@SuppressWarnings("serial")
public class WarScheduler extends TKScheduler implements Observer {

	// Délai initial entre chaque tick. Evite que le jeu aille trop vite.
	public static final int INITIAL_DELAY = 10;

	private GenericBehaviorActivator<WarAgent> _warAgentDoOnEachTickActivator;

	@Override
	protected void activate() {
		super.activate();

		_warAgentDoOnEachTickActivator = new GenericBehaviorActivator<WarAgent>(community, TKOrganization.TURTLES_GROUP, TKOrganization.TURTLE_ROLE, "doOnEachTick");
		addActivator(_warAgentDoOnEachTickActivator);

		setDelay(INITIAL_DELAY);

		Game.getInstance().addObserver(this);
	}

	@Override
	public void doSimulationStep() {
		if (logger != null) {
			logger.finest("Doing simulation step " + getGVT());
		}

		logger.finest("Activating --------> " + getPheroMaxReset());
		getPheroMaxReset().execute();
		logger.finest("Activating --------> " + getWarAgentDoOnEachTickActivator());
		getWarAgentDoOnEachTickActivator().execute();
		logger.finest("Activating --------> " + getTurtleActivator());
		getTurtleActivator().execute();
		logger.finest("Activating --------> " + getEnvironmentUpdateActivator());
		getEnvironmentUpdateActivator().execute();
		logger.finest("Activating --------> " + getViewerActivator());
		getViewerActivator().execute();

		setGVT(getGVT() + 1);

		// Apparition de WarResource
		if(getGVT() % Simulation.getInstance().getFoodAppearanceRate() == 0) {
			Game.getInstance().getMotherNatureTeam().createAndLaunchNewResource(this, WarAgentType.WarFood);
		}

		// Testes pour voir si une équipe n'a plus de base
		for (Team t : Game.getInstance().getPlayerTeams()) {
			if (t.getNbUnitsLeftOfType(WarAgentType.WarBase) == 0) {
				Game.getInstance().removePlayerTeam(t);
			}
		}

		Game.getInstance().doOnEachTick();
	}

	protected GenericBehaviorActivator<WarAgent> getWarAgentDoOnEachTickActivator() {
		return _warAgentDoOnEachTickActivator;
	}

	@Override
	public void update(Observable o, Object arg) {
		Integer reason = (Integer) arg;
		if ((reason == Game.UPDATE_TEAM_REMOVED && Game.getInstance().getPlayerTeams().size() <= 1) ||
				reason == Game.GAME_STOPPED) {
			sendMessage(
					LocalCommunity.NAME, 
					Groups.SYSTEM, 
					Organization.GROUP_MANAGER_ROLE, 
					new KernelMessage(KernelAction.EXIT));
		}
	}
}
