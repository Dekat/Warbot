package edu.warbot.game;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.logging.Level;

import javax.swing.ImageIcon;

import edu.warbot.FSM.WarFSM;
import edu.warbot.FSM.WarFSMBrainController;
import edu.warbot.FSMEditor.FSMInstancier;
import edu.warbot.FSMEditor.models.Modele;
import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.WarAgent;
import edu.warbot.agents.WarProjectile;
import edu.warbot.agents.agents.WarBase;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.ControllableWarAgentAdapter;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.capacities.Creator;
import edu.warbot.communications.WarKernelMessage;
import edu.warbot.gui.launcher.WarLauncherInterface;
import edu.warbot.tools.WarMathTools;

public class Team extends Observable {
	
	public static int MAX_DYING_STEP = 5;
	public static final String DEFAULT_GROUP_NAME = "defaultGroup-Warbot";
	
	private String _name;
	private ImageIcon _teamLogo;
	private Color _color;
	private String _description;
	private HashMap<String, Class<? extends WarBrain>> _brainControllers;
	private ArrayList<ControllableWarAgent> _controllableAgents;
	private ArrayList<WarProjectile> _projectiles;
	private HashMap<WarAgentType, Integer> _nbUnitsLeft;
	private ArrayList<WarAgent> _dyingAgents;
	private WarGame game;
	private Modele fsmModel;
	
	public Team(String nom) {
		_name = nom;
		_color = Color.WHITE;
		_teamLogo = null;
		_description = "";
		_controllableAgents = new ArrayList<>();
		_projectiles = new ArrayList<>();
		_brainControllers = new HashMap<>();
		_nbUnitsLeft = new HashMap<>();
		for(WarAgentType type : WarAgentType.values())
			_nbUnitsLeft.put(type, 0);
		_dyingAgents = new ArrayList<>();
	}
	
	public Team(String nom, Color color, ImageIcon logo, String description, ArrayList<ControllableWarAgent> controllableAgents, ArrayList<WarProjectile> projectiles,
			HashMap<String, Class<? extends WarBrain>> brainControllers, HashMap<WarAgentType, Integer> nbUnitsLeft, ArrayList<WarAgent> dyingAgents) {
		_name = nom;
		_color = color;
		_teamLogo = logo;
		_description = description;
		_controllableAgents = controllableAgents;
		_projectiles = projectiles;
		_brainControllers = brainControllers;
		_nbUnitsLeft = nbUnitsLeft;
		_dyingAgents = dyingAgents;
	}
	
    public void setLogo(ImageIcon logo) {
        _teamLogo = logo;
    }
	
    public void addBrainControllerClassForAgent(String agent, Class<? extends WarBrain> brainController) {
    	_brainControllers.put(agent, brainController);
    }
    
    public Class<? extends WarBrain> getBrainControllerOfAgent(String agentName) {
    	return this._brainControllers.get(agentName);
    }
    
    public HashMap<String, Class<? extends WarBrain>> getAllBrainControllers() {
    	return _brainControllers;
    }
    
    public ImageIcon getImage() {
        return _teamLogo;
    }
    
	public String getName() {
		return _name;
	}
	
	public void setName(String newName) {
		_name = newName;
	}
	
	public String getDescription() {
		return _description;
	}
	
	public void setDescription(String description) {
		_description = description;
	}
	
	public void addWarAgent(WarAgent agent) {
		WarAgentType type = WarAgentType.valueOf(agent.getClass().getSimpleName());
		_nbUnitsLeft.put(type, _nbUnitsLeft.get(type) + 1);
		if (agent instanceof WarProjectile)
			_projectiles.add((WarProjectile) agent);
		else if (agent instanceof ControllableWarAgent)
			_controllableAgents.add((ControllableWarAgent) agent);
		agent.getLogger().log(Level.FINEST, agent.toString() + " added to team " + this.getName());
		
		setChanged();
		notifyObservers();
	}
	
	public ArrayList<ControllableWarAgent> getControllableAgents() {
		return _controllableAgents;
	}
	
	public ArrayList<WarProjectile> getProjectiles() {
		return _projectiles;
	}
	
	public void removeWarAgent(WarAgent agent) {
		WarAgentType type = WarAgentType.valueOf(agent.getClass().getSimpleName());
		_nbUnitsLeft.put(type, _nbUnitsLeft.get(type) - 1);
		if (agent instanceof WarProjectile)
			_projectiles.remove(agent);
		else
			_controllableAgents.remove(agent);
		
		setChanged();
		notifyObservers();
	}
	
	public void setWarAgentAsDying(WarAgent agent) {
		removeWarAgent(agent);
		_dyingAgents.add(agent);
	}
	
	public ArrayList<WarAgent> getAllAgents() {
		ArrayList<WarAgent> toReturn = new ArrayList<>();
		toReturn.addAll(_controllableAgents);
		toReturn.addAll(_projectiles);
		return toReturn;
	}
	
	public void removeAllAgents() {
		_controllableAgents.clear();
		_projectiles.clear();
		_dyingAgents.clear();
		for(WarAgentType type : WarAgentType.values())
			_nbUnitsLeft.put(type, 0);
	}

	/**
	 * Retourne l'agent dont l'id est celui passé en paramètre
	 * @param id - id de l'agent à récupérer
	 * @return l'agent dont l'id est passé en paramètre
	 * @return null si aucun agent n'a été trouvé
	 */
	public WarAgent getAgentWithID(int id) {
		for(WarAgent a : getAllAgents()) {
			if (a.getID() == id)
				return a;
		}
		return null;
	}
	
	public void sendMessageToAllMembers(ControllableWarAgent sender, String message, String[] content) {
		// A savoir que Madkit exclut la possibilité qu'un agent s'envoie un message à lui-même, nous ne faisons donc pas le test ici
		for (WarAgent a : _controllableAgents) {
			sender.sendMessage(a.getAgentAddressIn(getName(), DEFAULT_GROUP_NAME, a.getClass().getSimpleName()),
					new WarKernelMessage(sender, message, content));
		}
	}
	
	@Override
	public boolean equals(Object team) {
		if (team instanceof Team)
			return this.getName().equals(((Team) team).getName());
		else if (team instanceof String)
			return this.getName().equals((String) team);
		else
			return false;
	}
	
	public ArrayList<WarAgent> getAllAgentsInRadiusOf(WarAgent referenceAgent, double radius) {
		ArrayList<WarAgent> toReturn = new ArrayList<>();
		for (WarAgent a : getAllAgents()) {
			if (referenceAgent.getDistanceFrom(a) <= radius) {
				toReturn.add(a);
			}
		}
		return toReturn;
	}
	
	public ArrayList<WarAgent> getAllAgentsInRadius(double posX, double posY, double radius) {
		ArrayList<WarAgent> toReturn = new ArrayList<>();
		for (WarAgent a : getAllAgents()) {
			if ((WarMathTools.getDistanceBetweenTwoPoints(posX, posY, a.getX(), a.getY()) - a.getHitboxMaxRadius()) <= radius) {
				toReturn.add(a);
			}
		}
		return toReturn;
	}

	public void setColor(Color color) {
		this._color = color;
	}
	
	public Color getColor() {
		return _color;
	}
	
	public int getNbUnitsLeftOfType(WarAgentType type) {
		return _nbUnitsLeft.get(type);
	}
	
	public HashMap<WarAgentType, Integer> getAllNbUnitsLeft() {
		return _nbUnitsLeft;
	}
	
	public void destroy() {
		for (WarAgent a : getAllAgents())
			a.killAgent(a);
	}
	
	public static Team duplicate(Team t, String newName) {
		return new Team(newName,
				((t.getColor()==null)?null:(new Color(t.getColor().getRGB()))),
				t.getImage(),
				t.getDescription(),
				new ArrayList<>(t.getControllableAgents()),
				new ArrayList<>(t.getProjectiles()),
				new HashMap<>(t.getAllBrainControllers()),
				new HashMap<>(t.getAllNbUnitsLeft()),
				new ArrayList<>(t.getDyingAgents())
				);
	}
	
	public static String getRealNameFromTeamName(String teamName) {
		if (teamName.endsWith(WarLauncherInterface.TEXT_ADDED_TO_DUPLICATE_TEAM_NAME))
			return teamName.substring(0, teamName.lastIndexOf(WarLauncherInterface.TEXT_ADDED_TO_DUPLICATE_TEAM_NAME));
		else
			return teamName;
	}

	public void doOnEachTick() {
		for (int i = 0; i < _dyingAgents.size(); i++) {
			WarAgent a = _dyingAgents.get(i);
			a.incrementDyingStep();
			if (a.getDyingStep() > MAX_DYING_STEP) {
				_dyingAgents.remove(i);
			}
		}
	}
	
	public ArrayList<WarAgent> getDyingAgents() {
		return new ArrayList<>(_dyingAgents);
	}

	public WarGame getGame() {
		return game;
	}

	public void setGame(WarGame game) {
		this.game = game;
	}
	
	public ControllableWarAgent instantiateNewControllableWarAgent(String agentName) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		String agentToCreateClassName = WarBase.class.getPackage().getName() + "." + agentName;
        Class<? extends WarBrain> brainControllerClass = getBrainControllerOfAgent(agentName);
        
        ControllableWarAgent a = (ControllableWarAgent) Class.forName(agentToCreateClassName)
				.getConstructor(Team.class, brainControllerClass.getSuperclass())
				.newInstance(this, brainControllerClass.newInstance());
		
		a.setLogLevel(getGame().getSettings().getLogLevel());

		if(a.getBrain() instanceof WarFSMBrainController){
			System.out.println("Team : Instance of FSM brain found");
			System.out.println("Team : Generating fsm instance for " + agentName + "...");
			//Intancie la fsm et la donne comme brain à l'agent
			FSMInstancier fsmInstancier = new FSMInstancier(getFSMModel());
			ControllableWarAgentAdapter adapter = a.getBrain().getAgent();
			WarFSM warFsm = fsmInstancier.getBrainControleurForAgent(WarAgentType.valueOf(agentName), adapter);
			System.out.println("Generation succesfull");
			
			WarFSMBrainController fsmBrainController = (WarFSMBrainController)a.getBrain();
			System.out.println("Team : adding fsm to FSMBrainController...");
			fsmBrainController.setFSM(warFsm);
				
		}
		
		return a;
	}
	
	private Modele getFSMModel() {
		return this.fsmModel;
	}
	
	public void setFSMModel(Modele fsmModel) {
		this.fsmModel = fsmModel;
	}

	public void createUnit(Creator creatorAgent, WarAgentType agentTypeToCreate) {
		if (creatorAgent instanceof WarAgent) {
			// TODO ajout des conditions de création
			// TODO contrôler si la base n'est pas encerclée. Dans ce cas, elle ne pourra pas créer d'agent car il n'y aura pas de place.
			((WarAgent) creatorAgent).getLogger().log(Level.FINEST, creatorAgent.toString() + " creating " + agentTypeToCreate);
			try {
				if (creatorAgent.isAbleToCreate(agentTypeToCreate)) {
					WarAgent a = instantiateNewControllableWarAgent(agentTypeToCreate.toString());
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
