package edu.warbot.agents;

import java.util.ArrayList;
import java.util.logging.Level;

import edu.warbot.agents.capacities.Movable;
import edu.warbot.agents.capacities.Picker;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.brains.WarBrainController;
import edu.warbot.communications.WarMessage;
import edu.warbot.game.Game;
import edu.warbot.game.Team;
import edu.warbot.tools.CoordPolar;

@SuppressWarnings("serial")
public abstract class MovableWarAgent extends ControllableWarAgent implements Picker, Movable {

	private double _speed;

	public MovableWarAgent(String firstActionToDo, Team team, double hitboxRadius, WarBrainController brainController, double distanceOfView, double angleOfView, int cost, int maxHealth, int bagSize, double speed) {
		super(firstActionToDo, team, hitboxRadius, brainController, distanceOfView, angleOfView, cost, maxHealth, bagSize);

		this._speed = speed;
	}

	public double getSpeed() {
		return _speed;
	}

	@Override
	public String move() {
		logger.log(Level.FINEST, this.toString() + " moving...");
		if (!isBlocked()) {
			logger.log(Level.FINER, this.toString() + " moved of " + getSpeed());
			fd(getSpeed());
		}
		return getBrainController().action();
	}

	@Override
	public String take() {
		logger.log(Level.FINEST, this.toString() + " taking...");
		if (getNbElementsInBag() < getBagSize()) {
			ArrayList<WarResource> reachableResources = Game.getInstance().getMotherNatureTeam().getAccessibleResourcesFor(this);
			if (reachableResources.size() > 0) {
				killAgent(reachableResources.get(0));
				addElementInBag();
				logger.log(Level.FINER, this.toString() + " take " + reachableResources.get(0).getClass().getSimpleName());
			}
		}
		return getBrainController().action();
	}

	@Override
	public boolean isBlocked() {
		return isGoingToBeOutOfMap() || isGoingToBeOnAnOtherAgent();
	}

	// TODO à quoi sert cette fonction ?
	public CoordPolar getPositonforGroupeOfTank(ArrayList<Integer> listId) {
		final int distanceFormation = (int) super.getDistanceOfView();

		this.broadcastMessage("tankFormation1", null);

		ArrayList<WarMessage> messageFormation = new ArrayList<>();
		WarMessage farMessage = null;
		WarMessage nearMEssage = null;
		ArrayList<Double> angleToSeloigner = new ArrayList<>();

		for (WarMessage m : this.getMessages()) {
			if (m.getMessage().equals("tankFormation1")) {
				messageFormation.add(m);
				if (farMessage != null && m.getDistance() > farMessage.getDistance())
					farMessage = m;
				if (nearMEssage != null && m.getDistance() < nearMEssage.getDistance())
					nearMEssage = m;

				if (m.getDistance() < this.getDistanceOfView() - this._speed)
					angleToSeloigner.add(m.getAngle());
			}
		}

		CoordPolar resultatPolaire;

		// Si le message le plus proche est trop loin on se rapproche du plus
		// proche
		if (nearMEssage.getDistance() - this._speed > distanceFormation)
			resultatPolaire = new CoordPolar(this._speed, nearMEssage.getAngle());

		// Si il y a plusieurs messages trop proches on s'eloigne
		if (angleToSeloigner.size() > 0) {
			double angleMoyenne = 0;
			for (Double angle : angleToSeloigner) {
				angleMoyenne += (angle + 180);
			}
			angleMoyenne /= angleToSeloigner.size();
			resultatPolaire = new CoordPolar(this._speed, angleMoyenne);
		}

		// sinon c'est qu'on est bien placé
		resultatPolaire = new CoordPolar(0, 0);

		return resultatPolaire;
	}

	/**
	 * Voir circle JD des fonctions poly
	 * */
	public CoordPolar circleThisEnemie(WarPercept tank, ArrayList<Integer> listAgent, int rayon) {

		return this.circleThisEnemie(new CoordPolar(tank.getDistance(), tank.getAngle()), listAgent, rayon);
	}

	/**
	 * Voir circle JD des fonctions poly
	 * */
	public CoordPolar circleThisEnemie(WarMessage mess, ArrayList<Integer> listAgent, int rayon) {

		if (mess == null || mess.getContent().length != 2) {
			System.out.println("Envoyez un message correct");
			return null;
		}

		double distance = Double.valueOf(mess.getContent()[0]);
		double angle = Double.valueOf(mess.getContent()[1]);

		return this.circleThisEnemie(new CoordPolar(distance, angle), listAgent, rayon);
	}

	/**
	 * 
	 * @param unité
	 *            qui l'on veut encercler
	 * @param listAgent
	 *            Liste des ID des agents qui vont encercler l'unitée
	 * @param rayon
	 *            Rayon du l'encerclement
	 * @return Les coordonées polaire où doit se placer l'unité courante pour
	 *         encercler l'unté passée en parametre. ATTENTION : si les
	 *         cooredonnées polaire sont ceux où se trouve déjà l'unité ça ne
	 *         sert à rien de se déplacer.
	 * 
	 */
	public CoordPolar circleThisEnemie(CoordPolar tank, ArrayList<Integer> listAgent, int rayon) {

		if (listAgent == null || listAgent.size() == 0) {
			return null;
		}

		CoordPolar polaireRes;

		// Si je suis a distance du reyon du cercle
		if (tank.getDistance() < rayon) {
			// Mon positionement dans la liste des agents me permet de savoir où
			// je vais me placer
			int positionementListe = listAgent.indexOf(this.getID());
			// Je trouve mon angle de positionement
			int myAngle = (360 / listAgent.size()) * positionementListe;
			// Je doit etre a cette angle la pour l'enemie donc langle pour
			// lequel je vais voir l'enemi est myAngle + 180
			myAngle += 180;
			double deltaAngle = 5; // Pour eviter les tranblements des unitées
			deltaAngle = (this._speed / rayon) * 1.1;
			// * 1.1 pour que la valeur soient un peu supperieure sinon il y
			// aura trop de va et viens pou ce positionner

			double angleRes;
			if (myAngle - deltaAngle > tank.getAngle()) {
				angleRes = (tank.getAngle() + 90) % 360;

			} else if (myAngle + deltaAngle < tank.getAngle()) {
				angleRes = (tank.getAngle() - 90) % 360;
			} else {
				// Mon angle est bon
				angleRes = tank.getAngle();
			}

			double distanceRes = this._speed;

			polaireRes = new CoordPolar(distanceRes, angleRes);

		} else if (tank.getDistance() - this._speed > rayon) {
			// Je suis trop loin je m'approche dans la direction de l'enemie
			polaireRes = new CoordPolar(tank.getDistance(), tank.getAngle());
		} else {
			// Je suis bien placé
			polaireRes = new CoordPolar(0, tank.getAngle());
		}

		return polaireRes;
	}
}
