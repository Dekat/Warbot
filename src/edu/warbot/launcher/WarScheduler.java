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
import edu.warbot.game.Team;
import edu.warbot.game.WarGame;
import edu.warbot.launcher.WarMain.Shared;

public class WarScheduler extends TKScheduler implements Observer {

	// Délai initial entre chaque tick. Evite que le jeu aille trop vite.
	public static final int INITIAL_DELAY = 10;

	private GenericBehaviorActivator<WarAgent> _warAgentDoOnEachTickActivator;
	private WarGame game;
	
	public WarScheduler() {
		this.game = Shared.getGame();
	}
	
	@Override
	protected void activate() {
		super.activate();

		_warAgentDoOnEachTickActivator = new GenericBehaviorActivator<WarAgent>(community, TKOrganization.TURTLES_GROUP, TKOrganization.TURTLE_ROLE, "doOnEachTick");
		addActivator(_warAgentDoOnEachTickActivator);

		setDelay(INITIAL_DELAY);

		game.addObserver(this);
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
		if(getGVT() % game.getSettings().getFoodAppearanceRate() == 0) {
			game.getMotherNatureTeam().createAndLaunchNewResource(game.getMap(), this, WarAgentType.WarFood);
		}

		// Testes pour voir si une équipe n'a plus de base
		for (Team t : game.getPlayerTeams()) {
			if (t.getNbUnitsLeftOfType(WarAgentType.WarBase) == 0) {
				game.removePlayerTeam(t);
			}
		}

		game.doOnEachTick();
	}

	protected GenericBehaviorActivator<WarAgent> getWarAgentDoOnEachTickActivator() {
		return _warAgentDoOnEachTickActivator;
	}

	@Override
	public void update(Observable o, Object arg) {
		Integer reason = (Integer) arg;
		if ((reason == WarGame.UPDATE_TEAM_REMOVED && game.getPlayerTeams().size() <= 1) ||
				reason == WarGame.GAME_STOPPED) {
			sendMessage(
					LocalCommunity.NAME, 
					Groups.SYSTEM, 
					Organization.GROUP_MANAGER_ROLE, 
					new KernelMessage(KernelAction.EXIT));
		}
	}
}
