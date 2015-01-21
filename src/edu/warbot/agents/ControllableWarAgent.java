package edu.warbot.agents;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

import madkit.kernel.Message;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.PerceptsGetter;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.agents.resources.WarFood;
import edu.warbot.brains.WarBrainController;
import edu.warbot.communications.WarKernelMessage;
import edu.warbot.communications.WarMessage;
import edu.warbot.game.Team;
import edu.warbot.launcher.Simulation;
import edu.warbot.launcher.WarConfig;
import edu.warbot.tools.CoordCartesian;
import edu.warbot.tools.CoordPolar;

@SuppressWarnings("serial")
public abstract class ControllableWarAgent extends WarAgent {

	public static final double MAX_DISTANCE_GIVE = WarConfig.getMaxDistanceGive();
	public static final String ACTION_GIVE = "give";
	public static final String ACTION_EAT = "eat";
	public static final String ACTION_IDLE = "idle";

	private double _angleOfView;
	private double _distanceOfView;
	private int _cost;
	private int _health;
	private int _maxHealth;
	private int _nbElementsInBag;
	private int _bagSize;
	private int _idNextAgentToGive;
	private double _viewDirection;
	private WarBrainController _brainController;
	private PerceptsGetter _perceptsGetter;
	private String _debugString;
	private Color _debugStringColor;

	public ControllableWarAgent(String firstActionToDo, Team team, double hitboxRadius, WarBrainController brainController, double distanceOfView, double angleOfView, int cost, int maxHealth, int bagSize) {
		super(firstActionToDo, team, hitboxRadius);

		_distanceOfView = distanceOfView;
		_angleOfView = angleOfView;
		_cost = cost;
		_maxHealth = maxHealth;
		_health = _maxHealth;
		_bagSize = bagSize;
		_nbElementsInBag = 0;
		_viewDirection = 180 * new Random().nextDouble();
		_brainController = brainController;
		_debugString = "";
		_debugStringColor = Color.BLACK;
	}

	@Override
	protected void activate() {
		super.activate();
		_perceptsGetter = Simulation.getInstance().getPerceptsGetterNewInstance(this);
		randomHeading();
		_brainController.activate();
	}
	
	@Override
	protected void doOnEachTick() {
		super.doOnEachTick();
		_perceptsGetter.setPerceptsAlreadyInit(false); // On indique au PerceptGetter qu'un nouveau tick est pass�
	}

	public String give() {
		logger.log(Level.FINEST, this.toString() + " giving...");
		WarAgent agent = getTeam().getAgentWithID(_idNextAgentToGive);
		if (agent != null) { // Si agent existe
			if (getDistanceFrom(agent) <= MAX_DISTANCE_GIVE) { // Si il n'est pas trop loins
				if (agent instanceof ControllableWarAgent) { // Si c'est un ControllableWarAgent
					if (((ControllableWarAgent) agent).getBagSize() > ((ControllableWarAgent) agent).getNbElementsInBag()) { // Si son sac a un emplacement vide
						logger.log(Level.FINER, this.toString() + " give WarFood to " + agent.toString());
						((ControllableWarAgent) agent).addElementInBag();
						_nbElementsInBag--;
					}
				}
			}
		}
		return getBrainController().action();
	}

	public String eat() {
		logger.finest(this.toString() + " eating...");
		if (getNbElementsInBag() > 0) {
			_nbElementsInBag--;
			heal(WarFood.HEALTH_GIVEN);
			logger.finer(this.toString() + " eat.");
		}
		return getBrainController().action();
	}

	public String idle() {
		logger.finer(this.toString() + " idle.");
		return getBrainController().action();
	}

	public WarBrainController getBrainController() {
		return _brainController;
	}
	
	public void setBrainController(WarBrainController brainController) {
		_brainController = brainController;
	}

	public ReturnCode sendMessage(int idAgent, String message, String[] content) {
		WarAgent agent = getTeam().getAgentWithID(idAgent);
		if (agent != null) {
			logger.finer(this.toString() + " send message to " + agent.toString());
			logger.finest("This message is : [" + message + "] " + content);
			return sendMessage(agent.getAgentAddressIn(getTeam().getName(), Team.DEFAULT_GROUP_NAME, agent.getClass().getSimpleName()), new WarKernelMessage(this, message, content));
		}
		return ReturnCode.INVALID_AGENT_ADDRESS;
	}

	public ReturnCode broadcastMessage(WarAgentType agentType, String message, String[] content) {
		logger.finer(this.toString() + " send message to default role agents : " + agentType);
		logger.finest("This message is : [" + message + "] " + content);
		return sendMessage(getTeam().getName(), Team.DEFAULT_GROUP_NAME, agentType.toString(), new WarKernelMessage(this, message, content));
	}

	public ReturnCode broadcastMessage(String groupName, String roleName, String message, String[] content) {
		logger.finer(this.toString() + " send message to agents from group " + groupName + " with role " + roleName);
		logger.finest("This message is : [" + message + "] " + content);
		return sendMessage(getTeam().getName(), groupName, roleName, new WarKernelMessage(this, message, content));
	}

	public void broadcastMessage(String message, String[] content) {
		logger.finer(this.toString() + " send message to all his team.");
		logger.finest("This message is : [" + message + "] " + content);
		getTeam().sendMessageToAllMembers(this, message, content);
	}

	public ReturnCode reply(WarMessage warMessage, String message, String[] content) {
		logger.log(Level.FINER, this.toString() + " send reply to " + warMessage.getSenderID());
		logger.log(Level.FINEST, "This message is : [" + message + "] " + content);
		return sendMessage(warMessage.getSenderID(), message, content);
	}

	public ArrayList<WarMessage> getMessages() {
		ArrayList<WarMessage> messages = new ArrayList<>();
		Message msg;
		while ((msg = nextMessage()) != null) {
			if (msg instanceof WarKernelMessage) {
				WarMessage warMsg = new WarMessage((WarKernelMessage) msg, this);
				logger.log(Level.FINER, this.toString() + " received message from " + warMsg.getSenderID());
				logger.log(Level.FINEST, "This message is : " + warMsg.toString());
				messages.add(warMsg);
			}
		}
		return messages;
	}

	public double getAngleOfView() {
		return _angleOfView;
	}
	
	public double getDistanceOfView() {
		return _distanceOfView;
	}

	public int getCost() {
		return _cost;
	}

	public int getHealth() {
		return _health;
	}

	public int getMaxHealth() {
		return _maxHealth;
	}

	public void heal(int quantity) {
		logger.log(Level.FINEST, this.toString() + "healed of " + quantity + "life points.");
		_health += quantity;
		if (_health > getMaxHealth()) {
			_health = getMaxHealth();
		}
	}

	public void damage(int quantity) {
		logger.finest(this.toString() + "damaged of " + quantity + "life points.");
		_health -= quantity;
		if (_health <= 0) {
			logger.finer(this.toString() + "killed.");
			killAgent(this);
		}
	}

	public int getNbElementsInBag() {
		return _nbElementsInBag;
	}

	public int getBagSize() {
		return _bagSize;
	}

	public void addElementInBag() {
		_nbElementsInBag++;
	}

	public void setIdNextAgentToGive(int idNextAgentToGive) {
		_idNextAgentToGive = idNextAgentToGive;
	}

	public void setViewDirection(double newViewDirection) {
		this._viewDirection = newViewDirection;
	}

	public double getViewDirection() {
		return _viewDirection;
	}

	public ArrayList<WarPercept> getPercepts() {
		return _perceptsGetter.getPercepts();
	}

	public ArrayList<WarPercept> getPercepts(boolean ally) {
		return _perceptsGetter.getAgentsPercepts(ally);
	}
	
	public ArrayList<WarPercept> getPerceptsRessource() {
		return _perceptsGetter.getResourcesPercepts();
	}
	
	public ArrayList<WarPercept> getPerceptsOfAgentByType(WarAgentType agentType, boolean ally) {
		return _perceptsGetter.getPerceptsByType(agentType, ally);
	}


	/**
	 * Renvoi la position d'un agent qui n'est pas dans le percept mais qui est
	 * vu par un autre agent allie. L'agent allie doit envoyer un message aux
	 * unites qui vont pouvoir connaitre la postion de l'agent enemie.
	 *
	 * @param message
	 *            Le message recu par l'agent qui voit l'enemie dans son
	 *            percept. Le message doit OBLIGATOIRETMENT contenir un tableau
	 *            de DEUX string : un pour la distance et l'autre pour l'angle
	 *            correspondant a la distance et l'angle que l'agent allie voit l'enemie.
	 * @return Coordonnee polaire de l'agent enemie percu indirectement
	 */
	public CoordPolar getIndirectPositionOfAgentWithMessage(WarMessage message) {
		if (message.getContent().length != 2) {
			System.out.println("ATTENTION vous devez envoyer un message avec des informations corrects dans getSecondPositionAgent");
			return null;
		}

		CoordPolar positionAllie = new CoordPolar(message.getDistance(), message.getAngle());
		CoordPolar positionEnnemi = new CoordPolar(Double.valueOf(message.getContent()[0]), Double.valueOf(message.getContent()[1]));

		CoordCartesian vecteurPositionAllie = positionAllie.toCartesian();
		CoordCartesian vecteurPositionEnemie = positionEnnemi.toCartesian();

		CoordCartesian positionfinal = new CoordCartesian(vecteurPositionAllie.getX() + vecteurPositionEnemie.getX(), 
				vecteurPositionAllie.getY() + vecteurPositionEnemie.getY());

		CoordPolar polaireFinal = positionfinal.toPolar();

		return polaireFinal;
	}

	/**
	 * Donne la position moyenne d'un groupe d'unite
	 * 
	 * @param agentType le type d'agent dont on veut connaitre la position moyenne
	 * @param ally = true, annemy = false
	 * 
	 * @return Coordonnee polaire de la position moyenne du groupe ou null si aucune unite
	 */
	public CoordPolar getAveragePositionOfUnitInPercept(WarAgentType agentType, boolean ally) {
		ArrayList<WarPercept> listPercept = this.getPerceptsOfAgentByType(agentType, ally);

		if (listPercept.size() == 0)
			return null;

		int nbEnemie = listPercept.size();
		CoordPolar listePolaireEnemie[] = new CoordPolar[nbEnemie];
		CoordCartesian listeVecteurEnemie[] = new CoordCartesian[nbEnemie];

		double sommeX = 0, sommeY = 0;

		for (int i = 0; i < nbEnemie; i++) {
			listeVecteurEnemie[i] = listePolaireEnemie[i].toCartesian();

			sommeX += listeVecteurEnemie[i].getX();
			sommeY += listeVecteurEnemie[i].getY();
		}

		CoordCartesian baricentre = new CoordCartesian(sommeX / nbEnemie, sommeY / nbEnemie);

		CoordPolar baricentrePolaire = baricentre.toPolar();

		return baricentrePolaire;
	}
	
	public String getDebugString() {
		return _debugString;
	}
	
	public void setDebugString(String debugString) {
		_debugString = debugString;
	}
	
	public Color getDebugStringColor() {
		return _debugStringColor;
	}

	public void setDebugStringColor(Color color) {
		_debugStringColor = color;
	}
	
	public void init(int health, int nbElementsInBag) {
		_health = health;
		_nbElementsInBag = nbElementsInBag;
	}
}
