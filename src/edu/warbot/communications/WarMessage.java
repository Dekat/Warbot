package edu.warbot.communications;

import edu.warbot.agents.WarAgent;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.tools.CoordCartesian;
import edu.warbot.tools.WarMathTools;

/**
 * Est un adaptateur pour un WarKernelMessage. Permet � un utilisateur de ne pas acc�der � toutes les m�thodes de madkit.kernel.Message
 */
public class WarMessage {

	private double _angle;
	private double _distance;
	private WarKernelMessage _kernelMessage;

	public WarMessage(WarKernelMessage kernelMsg, WarAgent receiver) {
		_kernelMessage = kernelMsg;
		//_angle = WarMathTools.getAngleBetweenTwoPoints(receiver.getX(), receiver.getY(), kernelMsg.getXSender(), kernelMsg.getYsender());
		_angle = receiver.getPosition().getAngleToPoint(new CoordCartesian(kernelMsg.getXSender(), kernelMsg.getYsender()));
		_distance = WarMathTools.getDistanceBetweenTwoPoints(receiver.getX(), receiver.getY(), kernelMsg.getXSender(), kernelMsg.getYsender())
				- receiver.getHitboxRadius() - kernelMsg.getSenderHitboxRadius();
	}
	
	/**
	 * Retourne l'angle depuis lequel le message a �t� re�u. 
	 * @return {@code double} - l'angle depuis lequel le message a �t� re�u.
	 */
	public double getAngle() {
		return _angle;
	}
	
	/**
	 * Retourne la distance entre l'�metteur et le r�cepteur du message.
	 * @return {@code double} - la distance entre l'�metteur et le r�cepteur du message.
	 */
	public double getDistance() {
		return _distance;
	}
	
	/**
	 * Retourne l'ID de l'agent �metteur.
	 * @return {@code int} - l'ID de l'agent �metteur.
	 */
	public int getSenderID() {
		return _kernelMessage.getSenderID();
	}
	
	/**
	 * Retourne le nom de l'�quipe de l'agent �metteur.
	 * @return {@code String} - le nom de l'�quipe de l'agent �metteur.
	 */
	public String getSenderTeamName() {
		return _kernelMessage.getSenderTeam().getName();
	}
	
	/**
	 * Retourne le type de l'agent �metteur.
	 * @return {@code WarAgentType} - le type de l'agent �metteur.
	 */
	public WarAgentType getSenderType() {
		return _kernelMessage.getType();
	}
		
	/**
	 * Retourne le message envoy�.
	 * @return {@code String} - le message envoy�.
	 */
	public String getMessage() {
		return _kernelMessage.getMessage();
	}
	
	/**
	 * Retourne le contenu du message.
	 * @return {@code String[]} - contenu du message.
	 */
	public String[] getContent() {
		return _kernelMessage.getContent();
	}
	
	
	/**
	 * Affiche des informations sur le message
	 */
	public String toString(){
		return "--- Message : " + getSenderType() + " " + getSenderID() +
				" - Equipe : " + getSenderTeamName() + " - " + getAngle() + "� " + getDistance() + " => " + getMessage();
	}
}
