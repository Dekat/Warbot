package edu.warbot.game;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

import madkit.kernel.Agent;
import edu.warbot.agents.WarAgent;
import edu.warbot.agents.WarResource;
import edu.warbot.agents.enums.WarAgentCategory;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.launcher.WarConfig;
import edu.warbot.tools.CoordCartesian;
import edu.warbot.tools.CoordPolar;
import edu.warbot.tools.WarCircle;
import edu.warbot.tools.WarMathTools;

public class MotherNatureTeam extends Team {

	private ArrayList<WarResource> _resources;
	
	public MotherNatureTeam() {
		super("M�re nature");
		_resources = new ArrayList<>();
		setColor(Color.GREEN);
	}

//	public void init() {
//		Dimension mapSize = Game.getInstance().getMap().getSize();
//		ArrayList<CoordCartesian> teamsPositions = Game.getInstance().getMap().getTeamsPositions(Game.getInstance().getPlayerTeams().size());
//		for (CoordCartesian pos : teamsPositions) {
//			for (int i = 0; i < WarConfig.getNbResourcesAreasPerTeam(); i++) {
//				CoordCartesian areaPos = WarMathTools.addTwoPoints(
//						pos,
//						CoordPolar.getRandomInBounds(WarConfig.getMaxDistanceOfResourcesAreasFromOwnerTeam())
//						);
//				areaPos.normalize(0, mapSize.width, 0, mapSize.height);
//				_resourcesAreas.add(areaPos);
//			}
//		}
//		CoordCartesian center = WarMathTools.getCenterOfPoints(teamsPositions);
//		for (int i = 0; i < 2; i++) {
//			CoordCartesian areaPos = WarMathTools.addTwoPoints(
//					center,
//					new CoordPolar(90, 90 + i * 180)
//					);
//			areaPos.normalize(0, mapSize.width, 0, mapSize.height);
//			_resourcesAreas.add(areaPos);
//		}
//	}
	
	@Override
	public void addWarAgent(WarAgent agent) {
		if (agent instanceof WarResource)
			_resources.add((WarResource) agent);
		else
			super.addWarAgent(agent);
	}

	public ArrayList<WarResource> getResources() {
		return _resources;
	}	

	@Override
	public void removeWarAgent(WarAgent agent) {
		if (agent instanceof WarResource)
			_resources.remove((WarResource) agent);
		else
			super.removeWarAgent(agent);
	}
		
	public ArrayList<WarResource> getAccessibleResourcesFor(WarAgent referenceAgent) {
		ArrayList<WarResource> toReturn = new ArrayList<>();
		for (WarResource a : _resources) {
			if (referenceAgent.getDistanceFrom(a) <= WarResource.MAX_DISTANCE_TAKE) {
				toReturn.add(a);
			}
		}
		return toReturn;
	}

	@Override
	public ArrayList<WarAgent> getAllAgents() {
		ArrayList<WarAgent> toReturn = super.getAllAgents();
		toReturn.addAll(_resources);
		return toReturn;
	}
	
	public void createAndLaunchNewResource(Agent launcher, WarAgentType resourceType) {
		if (resourceType.getCategory() == WarAgentCategory.Resource) {
			try {
				WarResource resource = Game.instantiateNewWarResource(resourceType.toString());
				launcher.launchAgent(resource);
				ArrayList<WarCircle> foodPositions = Game.getInstance().getMap().getFoodPositions();
				CoordCartesian newPos = WarMathTools.addTwoPoints(
						foodPositions.get(new Random().nextInt(foodPositions.size())).getCenterPosition(),
						CoordPolar.getRandomInBounds(WarConfig.getRadiusResourcesAreas()).toCartesian());
				newPos.normalize(0, Game.getInstance().getMap().getWidth(), 0, Game.getInstance().getMap().getHeight());
				resource.setPosition(newPos);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				System.err.println("Erreur lors de la cr�ation d'une nouvelle ressource : " + resourceType);
				e.printStackTrace();
			}
		} else {
			System.err.println(resourceType + " n'est pas une resource.");
		}
	}
}
