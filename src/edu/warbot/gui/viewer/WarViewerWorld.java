package edu.warbot.gui.viewer;


import com.badlogic.gdx.utils.Array;

import edu.warbot.agents.WarAgent;
import edu.warbot.agents.WarProjectile;
import edu.warbot.agents.agents.WarBase;
import edu.warbot.agents.agents.WarEngineer;
import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.agents.agents.WarKamikaze;
import edu.warbot.agents.agents.WarRocketLauncher;
import edu.warbot.agents.agents.WarTurret;
import edu.warbot.game.Game;
import edu.warbot.game.Team;
import edu.warbot.gui.viewer.animations.WarViewerAnimationExplosion;
import edu.warbot.gui.viewer.entities.WarViewerEntity;
import edu.warbot.gui.viewer.entities.WarViewerEntityBase;
import edu.warbot.gui.viewer.entities.WarViewerEntityControllable;
import edu.warbot.gui.viewer.entities.WarViewerEntityEngineer;
import edu.warbot.gui.viewer.entities.WarViewerEntityExplorer;
import edu.warbot.gui.viewer.entities.WarViewerEntityFood;
import edu.warbot.gui.viewer.entities.WarViewerEntityKamikaze;
import edu.warbot.gui.viewer.entities.WarViewerEntityProjectile;
import edu.warbot.gui.viewer.entities.WarViewerEntityRocketLauncher;
import edu.warbot.gui.viewer.entities.WarViewerEntityTurret;

public class WarViewerWorld {
	
	private Game game;
	private WarViewerMap map;
	private Array<WarViewerEntityControllable> entitiesControllable;
	private Array<WarViewerEntityProjectile> entitiesProjectile;
	private Array<WarViewerEntityFood> entitiesResource;
	private Array<WarViewerAnimationExplosion> entitiesControllableExplosions;
	
	public WarViewerWorld(Game game) {
		this.game = game;
		map = new WarViewerMap(0, "map_8x8-patch_125_75_64_32_2", 125, 75, 64, 32);
		entitiesControllable = new Array<WarViewerEntityControllable>();
		entitiesProjectile = new Array<WarViewerEntityProjectile>();
		entitiesResource = new Array<WarViewerEntityFood>();
		entitiesControllableExplosions = new Array<WarViewerAnimationExplosion>();
	}
	
	public void initEntities() {
		
		//mothernature
		for (WarAgent ag: game.getMotherNatureTeam().getAllAgents()) {
			entitiesResource.add(new WarViewerEntityFood(ag.getID(),
					(int) ag.getX(), (int) ag.getY(), 32, 32));
		}
		
		
		for (Team t : game.getPlayerTeams()) {
			int color = determineTeamColor(t);
			for (WarAgent ag : t.getAllAgents()) {
				if (ag instanceof WarProjectile) {
					entitiesProjectile.add(new WarViewerEntityProjectile(ag
							.getID(), (int) ag.getX(), (int) ag.getY(), 32, 32,
							t));
				} else if (ag instanceof WarBase) {
					entitiesControllable
							.add(new WarViewerEntityBase(
									ag.getID(), (int) ag.getX(), (int) ag
											.getY(), color, ag
											.getHeading(), ((WarBase) ag)
											.getHealth(), ((WarBase) ag)
											.getMaxHealth()));

				} else if (ag instanceof WarExplorer) {
					entitiesControllable
					.add(new WarViewerEntityExplorer(
							ag.getID(), (int) ag.getX(), (int) ag
									.getY(), color, ag
									.getHeading(), ((WarExplorer) ag)
									.getHealth(), ((WarExplorer) ag)
									.getMaxHealth()));

				} else if (ag instanceof WarRocketLauncher) {
					entitiesControllable
					.add(new WarViewerEntityRocketLauncher(
							ag.getID(), (int) ag.getX(), (int) ag
									.getY(), color, ag
									.getHeading(), ((WarRocketLauncher) ag)
									.getHealth(), ((WarRocketLauncher) ag)
									.getMaxHealth()));

				} else if (ag instanceof WarTurret) {
					entitiesControllable
							.add(new WarViewerEntityTurret(ag.getID(), (int) ag
									.getX(), (int) ag.getY(), color,
									ag.getHeading(),
									((WarTurret) ag).getHealth(), ((WarTurret) ag)
											.getMaxHealth()));

				} else if (ag instanceof WarEngineer) {
					entitiesControllable
					.add(new WarViewerEntityEngineer(
							ag.getID(), (int) ag.getX(), (int) ag
									.getY(), color, ag
									.getHeading(), ((WarEngineer) ag)
									.getHealth(), ((WarEngineer) ag)
									.getMaxHealth()));

				} else if (ag instanceof WarKamikaze) {
					entitiesControllable
					.add(new WarViewerEntityKamikaze(
							ag.getID(), (int) ag.getX(), (int) ag
									.getY(), color, ag
									.getHeading(), ((WarKamikaze) ag)
									.getHealth(), ((WarKamikaze) ag)
									.getMaxHealth()));

				} 
			}
		}
	}
	
	public void updateWorld() {
		addElements();
		deleteElements();
	}

	/**
	 * Verifie si il y a de nouvelles entites et les ajoutes
	 */
	private void addElements() {
		for (WarAgent ag : game.getMotherNatureTeam().getAllAgents()) {
			if (!existeFoodInEntities(ag)) {
				entitiesResource.add(new WarViewerEntityFood(ag.getID(), (int) ag
					.getX(), (int) ag.getY(), 32, 32));
			}
		}
		for (Team t: game.getPlayerTeams()) {
			int color = determineTeamColor(t);
			for (WarAgent ag: t.getAllAgents()) {	
				if (ag instanceof WarProjectile) {
					if (!existProjectileInEntities(ag)) {
					entitiesProjectile.add(new WarViewerEntityProjectile(ag
							.getID(), (int) ag.getX(), (int) ag.getY(), 32, 32,
							t));
					}
				} else if (ag instanceof WarBase) {
					if (!existControllableAgentInEntities(ag)) {
						entitiesControllable
								.add(new WarViewerEntityBase(
										ag.getID(), (int) ag.getX(), (int) ag
												.getY(), color, ag
												.getHeading(), ((WarBase) ag)
												.getHealth(), ((WarBase) ag)
												.getMaxHealth()));
					}
				} else if (ag instanceof WarExplorer) {
					if (!existControllableAgentInEntities(ag)) {
						entitiesControllable
						.add(new WarViewerEntityExplorer(
								ag.getID(), (int) ag.getX(), (int) ag
										.getY(), color, ag
										.getHeading(), ((WarExplorer) ag)
										.getHealth(), ((WarExplorer) ag)
										.getMaxHealth()));
					}

				} else if (ag instanceof WarRocketLauncher) {
					if (!existControllableAgentInEntities(ag)) {
						entitiesControllable
						.add(new WarViewerEntityRocketLauncher(
								ag.getID(), (int) ag.getX(), (int) ag
										.getY(), color, ag
										.getHeading(), ((WarRocketLauncher) ag)
										.getHealth(), ((WarRocketLauncher) ag)
										.getMaxHealth()));
					}
				} else if (ag instanceof WarTurret) {
					if (!existControllableAgentInEntities(ag)) {
						entitiesControllable.add(new WarViewerEntityTurret(ag
								.getID(), (int) ag.getX(), (int) ag.getY(),
								color, ag.getHeading(), ((WarTurret) ag)
										.getHealth(), ((WarTurret) ag)
										.getMaxHealth()));
					}

				} else if (ag instanceof WarEngineer) {
					if (!existControllableAgentInEntities(ag)) {
						entitiesControllable
						.add(new WarViewerEntityEngineer(
								ag.getID(), (int) ag.getX(), (int) ag
										.getY(), color, ag
										.getHeading(), ((WarEngineer) ag)
										.getHealth(), ((WarEngineer) ag)
										.getMaxHealth()));
					}

				} else if (ag instanceof WarKamikaze) {
					if (!existControllableAgentInEntities(ag)) {
						entitiesControllable
						.add(new WarViewerEntityKamikaze(
								ag.getID(), (int) ag.getX(), (int) ag
										.getY(), color, ag
										.getHeading(), ((WarKamikaze) ag)
										.getHealth(), ((WarKamikaze) ag)
										.getMaxHealth()));
					}
				} 
			}
		}
	}
	
	
	private boolean existeFoodInEntities(WarAgent ag) {
		boolean find = false;
		int i = 0; 
		while (i < entitiesResource.size && !find) {
			if (ag.getID() == entitiesResource.get(i).getId()) {
				find = true;
			} else {
				i++;
			}
		}
		return find;
	}

	private boolean existControllableAgentInEntities(WarAgent ag) {
		boolean find = false;
		int i = 0; 
		while (i < entitiesControllable.size && !find) {
			if (ag.getID() == entitiesControllable.get(i).getId()) {
				find = true;
			} else {
				i++;
			}
		}
		return find;
	}

	private boolean existProjectileInEntities(WarAgent ag) {
		boolean find = false;
		int i = 0; 
		while (i < entitiesProjectile.size && !find) {
			if (ag.getID() == entitiesProjectile.get(i).getId()) {
				find = true;
			} else {
				i++;
			}
		}
		return find;
	}
	
	/**
	 * Verifie si des entites n existent plus et les supprimes
	 */
	public void deleteElements() {
		for (WarViewerEntityFood food: entitiesResource) {
			if (!existFoodInGame(food)) {
				entitiesResource.removeValue(food, false);
			}
		}
		
		for (WarViewerEntityControllable agent: entitiesControllable) {
			if (existDyingAgentsInGame(agent)) {
				entitiesControllableExplosions.add(agent.explode());
				entitiesControllable.removeValue(agent, false);
			}
		}
		
		for (WarViewerEntityProjectile projectile: entitiesProjectile) {
			if (!existProjectileInGame(projectile)) {
				entitiesControllableExplosions.add(projectile.explode());
				entitiesProjectile.removeValue(projectile, false);
			}
		}
	}
	
	private boolean existProjectileInGame(WarViewerEntityProjectile projectile) {
		return projectile.getTeam().getAgentWithID(projectile.getId()) != null;

	}

	private boolean existDyingAgentsInGame(WarViewerEntity agent) {
		for (Team t : game.getPlayerTeams()) {
			for (WarAgent ag: t.getDyingAgents()) {
				if (ag.getID() == agent.getId()) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean existFoodInGame(WarViewerEntityFood food) {
		boolean find = false;
		int i = 0;
		while (i < game.getMotherNatureTeam().getAllAgents().size() && !find) {
			if (food.getId() == game.getMotherNatureTeam().getAllAgents()
					.get(i).getID()) {
				find = true;
			} else {
				i++;
			}
		}
		return find;
	}

	private int determineTeamColor(Team t) {
		int color = 0;
		if (t.getColor().getRed() == 149
				&& t.getColor().getGreen() == 149
				&& t.getColor().getBlue() == 255) {
			color = 0;
		} else if (t.getColor().getRed() == 255
				&& t.getColor().getGreen() == 98
				&& t.getColor().getBlue() == 98) {
			color = 1;
		}
		
		return color;
	}

	public Array<WarViewerEntityControllable> getEntitiesControllable() {
		return entitiesControllable;
	}

	public Array<WarViewerEntityProjectile> getEntitiesProjectile() {
		return entitiesProjectile;
	}

	public Array<WarViewerEntityFood> getEntitiesResource() {
		return entitiesResource;
	}

	public Array<WarViewerAnimationExplosion> getEntitiesControllableExplosions() {
		return entitiesControllableExplosions;
	}

	public void disposeEntities() {
		map.getMap().dispose();
		entitiesControllable.clear();
		entitiesControllableExplosions.clear();
		entitiesProjectile.clear();
		entitiesResource.clear();
	}

	public WarViewerMap getMap() {
		return map;
	}
	
	
}
