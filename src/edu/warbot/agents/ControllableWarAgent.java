package edu.warbot.agents;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

import madkit.kernel.Message;
import edu.warbot.agents.actions.ControllableActions;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.PerceptsGetter;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.agents.resources.WarFood;
import edu.warbot.brains.ControllableWarAgentAdapter;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.capacities.Controllable;
import edu.warbot.communications.WarKernelMessage;
import edu.warbot.communications.WarMessage;
import edu.warbot.game.Team;
import edu.warbot.tools.CoordCartesian;
import edu.warbot.tools.CoordPolar;
import edu.warbot.tools.WarMathTools;

public abstract class ControllableWarAgent extends WarAgent implements ControllableActions, Controllable {

	private double _angleOfView;
	private double _distanceOfView;
	private int _cost;
	private int _health;
	private int _maxHealth;
	private int _nbElementsInBag;
	private int _bagSize;
	private int _idNextAgentToGive;
	private double _viewDirection;
	private WarBrain<? extends ControllableWarAgentAdapter> _brain;
	private PerceptsGetter _perceptsGetter;
	private String _debugString;
	private Color _debugStringColor;
    private ArrayList<WarMessage> thisTickMessages;

	public ControllableWarAgent(String firstActionToDo, Team team, Shape hitbox, WarBrain<? extends ControllableWarAgentAdapter> brain, double distanceOfView, double angleOfView, int cost, int maxHealth, int bagSize) {
		super(firstActionToDo, team, hitbox);

		_distanceOfView = distanceOfView;
		_angleOfView = angleOfView;
		_cost = cost;
		_maxHealth = maxHealth;
		_health = _maxHealth;
		_bagSize = bagSize;
		_nbElementsInBag = 0;
		_viewDirection = 180 * new Random().nextDouble();
		_brain = brain;
		_debugString = "";
		_debugStringColor = Color.BLACK;
	}

	@Override
	protected void activate() {
		super.activate();
		_perceptsGetter = getTeam().getGame().getSettings().getPerceptsGetterNewInstance(this, getTeam().getGame());
		randomHeading();
		_brain.activate();
	}
	
	@Override
	protected void doOnEachTick() {
		super.doOnEachTick();
		_perceptsGetter.setPerceptsOutdated(); // On indique au PerceptGetter qu'un nouveau tick est passé
        thisTickMessages = null;
	}

	@Override
	public String give() {
		logger.log(Level.FINEST, this.toString() + " giving...");
		if(getNbElementsInBag() > 0) { // Si l'agent courant a quelque chose à donner
			WarAgent agentToGive = getTeam().getAgentWithID(_idNextAgentToGive);
			if (agentToGive != null) { // Si agent existe
				if (getDistanceFrom(agentToGive) <= MAX_DISTANCE_GIVE) { // Si il n'est pas trop loin
					if (agentToGive instanceof ControllableWarAgent) { // Si c'est un ControllableWarAgent
						if (((ControllableWarAgent) agentToGive).getBagSize() > ((ControllableWarAgent) agentToGive).getNbElementsInBag()) { // Si son sac a un emplacement vide
							logger.log(Level.FINER, this.toString() + " give WarFood to " + agentToGive.toString());
							((ControllableWarAgent) agentToGive).addElementInBag();
							_nbElementsInBag--;
						}
					}
				}
			}
		}
		return getBrain().action();
	}

	@Override
	public String eat() {
		logger.finest(this.toString() + " eating...");
		if (getNbElementsInBag() > 0) {
			_nbElementsInBag--;
			heal(WarFood.HEALTH_GIVEN);
			logger.finer(this.toString() + " eat.");
		}
		return getBrain().action();
	}

	@Override
	public String idle() {
		logger.finer(this.toString() + " idle.");
		return getBrain().action();
	}

	public WarBrain<? extends ControllableWarAgentAdapter> getBrain() {
		return _brain;
	}
	
	public void setBrain(WarBrain<? extends ControllableWarAgentAdapter> brain) {
		_brain = brain;
	}

	@Override
	public ReturnCode sendMessage(int idAgent, String message, String... content) {
		WarAgent agent = getTeam().getAgentWithID(idAgent);
		if (agent != null) {
			logger.finer(this.toString() + " send message to " + agent.toString());
			logger.finest("This message is : [" + message + "] " + content);
			return sendMessage(agent.getAgentAddressIn(getTeam().getName(), Team.DEFAULT_GROUP_NAME, agent.getClass().getSimpleName()), new WarKernelMessage(this, message, content));
		}
		return ReturnCode.INVALID_AGENT_ADDRESS;
	}

	@Override
	public ReturnCode broadcastMessageToAgentType(WarAgentType agentType, String message, String... content) {
		logger.finer(this.toString() + " send message to default role agents : " + agentType);
		logger.finest("This message is : [" + message + "] " + content);
		return sendMessage(getTeam().getName(), Team.DEFAULT_GROUP_NAME, agentType.toString(), new WarKernelMessage(this, message, content));
	}

	@Override
	public ReturnCode broadcastMessage(String groupName, String roleName, String message, String... content) {
		logger.finer(this.toString() + " send message to agents from group " + groupName + " with role " + roleName);
		logger.finest("This message is : [" + message + "] " + content);
		return sendMessage(getTeam().getName(), groupName, roleName, new WarKernelMessage(this, message, content));
	}

	@Override
	public void broadcastMessageToAll(String message, String... content) {
		logger.finer(this.toString() + " send message to all his team.");
		logger.finest("This message is : [" + message + "] " + content);
		getTeam().sendMessageToAllMembers(this, message, content);
	}

	@Override
	public ReturnCode reply(WarMessage warMessage, String message, String... content) {
		logger.log(Level.FINER, this.toString() + " send reply to " + warMessage.getSenderID());
		logger.log(Level.FINEST, "This message is : [" + message + "] " + content);
		return sendMessage(warMessage.getSenderID(), message, content);
	}

	@Override
	public ArrayList<WarMessage> getMessages() {
        if(thisTickMessages == null) {
            thisTickMessages = new ArrayList<>();
            Message msg;
            while ((msg = nextMessage()) != null) {
                if (msg instanceof WarKernelMessage) {
                    WarMessage warMsg = new WarMessage((WarKernelMessage) msg, this);
                    logger.log(Level.FINER, this.toString() + " received message from " + warMsg.getSenderID());
                    logger.log(Level.FINEST, "This message is : " + warMsg.toString());
                    thisTickMessages.add(warMsg);
                }
            }
        }
		return thisTickMessages;
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

	@Override
	public int getHealth() {
		return _health;
	}

	@Override
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

	@Override
	public int getNbElementsInBag() {
		return _nbElementsInBag;
	}

	@Override
	public int getBagSize() {
		return _bagSize;
	}
	
	@Override
	public boolean isBagEmpty() {
		return getNbElementsInBag() == 0;
	}
	
	@Override
	public boolean isBagFull() {
		return getNbElementsInBag() == getBagSize();
	}

	public void addElementInBag() {
		_nbElementsInBag++;
	}

	@Override
	public void setIdNextAgentToGive(int idNextAgentToGive) {
		_idNextAgentToGive = idNextAgentToGive;
	}

	@Override
	public void setViewDirection(double newViewDirection) {
		this._viewDirection = newViewDirection;
	}

	@Override
	public double getViewDirection() {
		return _viewDirection;
	}

	@Override
	public ArrayList<WarPercept> getPercepts() {
		return _perceptsGetter.getPercepts();
	}

	public ArrayList<WarPercept> getPercepts(boolean ally) {
		return _perceptsGetter.getWarAgentsPercepts(ally);
	}
	
	@Override
	public ArrayList<WarPercept> getPerceptsAllies() {
		return getPercepts(true);
	}
	
	@Override
	public ArrayList<WarPercept> getPerceptsEnemies() {
		return getPercepts(false);
	}
	
	@Override
	public ArrayList<WarPercept> getPerceptsResources() {
		return _perceptsGetter.getResourcesPercepts();
	}
	
	public ArrayList<WarPercept> getPerceptsByAgentType(WarAgentType agentType, boolean ally) {
		return _perceptsGetter.getPerceptsByType(agentType, ally);
	}
	
	@Override
	public ArrayList<WarPercept> getPerceptsAlliesByType(WarAgentType agentType) {
		return getPerceptsByAgentType(agentType, true);
	}
	
	@Override
	public ArrayList<WarPercept> getPerceptsEnemiesByType(WarAgentType agentType) {
		return getPerceptsByAgentType(agentType, false);
	}

    public Shape getPerceptionArea() {
        return _perceptsGetter.getPerceptionArea();
    }

	/**
	 * Renvoi la position d'un agent qui n'est pas dans le percept mais qui est
	 * vu par un autre agent allié. L'agent allié doit envoyer un message aux
	 * unités qui vont pouvoir connaître la postion de l'agent ennemi.
	 *
	 * @param message
	 *            Le message reçu par l'agent qui voit l'enemi dans son
	 *            percept. Le message doit OBLIGATOIRETMENT contenir un tableau
	 *            de DEUX string : un pour la distance et l'autre pour l'angle
	 *            correspondant à la distance et l'angle que l'agent allié voit l'ennemi.
	 * @return Coordonnee polaire de l'agent ennemi perçu indirectement
	 */
	@Override
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
	@Override
	public CoordPolar getAveragePositionOfUnitInPercept(WarAgentType agentType, boolean ally) {
		ArrayList<WarPercept> listPercept = this.getPerceptsByAgentType(agentType, ally);

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
	
	@Override
	public CoordPolar getTargetedAgentPosition(double angleToAlly, double distanceFromAlly, double angleFromAllyToTarget, double distanceBetweenAllyAndTarget) {
		return WarMathTools.addTwoPoints(new CoordPolar(distanceFromAlly, angleToAlly),
				new CoordPolar(distanceBetweenAllyAndTarget, angleFromAllyToTarget));
	}
	
	@Override
	public String getDebugString() {
		return _debugString;
	}
	
	@Override
	public void setDebugString(String debugString) {
		_debugString = debugString;
	}
	
	@Override
	public Color getDebugStringColor() {
		return _debugStringColor;
	}

	@Override
	public void setDebugStringColor(Color color) {
		_debugStringColor = color;
	}
	
	public void init(int health, int nbElementsInBag) {
		_health = health;
		_nbElementsInBag = nbElementsInBag;
	}
}
