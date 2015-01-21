package edu.warbot.communications;

import madkit.kernel.Message;
import edu.warbot.agents.WarAgent;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.game.Team;

/**
 * Contient toutes les donn�es sur l'�metteur d'un message et sur le contenu du message
 */
@SuppressWarnings("serial")
public class WarKernelMessage extends Message {
	
	private double _xSender;
	private double _ySender;
	private int	_idSender;
	private Team _senderTeam;
	private WarAgentType _senderType;
	private String _message;
	private String[] _content;
	private double _senderHitboxRadius;
	
	public WarKernelMessage(WarAgent sender, String message, String[] content) {
		super();
		_message = message;
		_content = content;
		_xSender = sender.getX();
		_ySender = sender.getY();
		_idSender = sender.getID();
		_senderTeam = sender.getTeam();
		_senderType = WarAgentType.valueOf(sender.getClass().getSimpleName());
		_senderHitboxRadius = sender.getHitboxRadius();
	}
		
	/**
	 * M�thode renvoyant la coordonn�e X o� se trouve l'agent �metteur du message.
	 * @return {@code int} - la coordonn�e X o� se trouve l'agent �metteur du message.
	 */
	public double getXSender() {
		return _xSender;
	}
	
	/**
	 * M�thode renvoyant la coordon�e Y o� se trouve l'agent �metteur du message.
	 * @return {@code int} - la coordon�e Y o� se trouve l'agent �metteur du message.
	 */
	public double getYsender() {
		return _ySender;
	}
	
	/**
	 * M�thode renvoyant l'identifiant de l'agent �metteur du message.
	 * @return {@code int} - l'identifiant de l'agent �metteur du message.
	 */
	public int getSenderID() {
		return _idSender;
	}
	
	/**
	 * M�thode renvoyant l'�quipe de l'agent �metteur du message.
	 * @return {@code Team} - l'�quipe de l'agent �metteur du message.
	 */
	public Team getSenderTeam() {
		return _senderTeam;
	}
	
	/**
	 * M�thode renvoyant le type de l'agent �metteur du message.
	 * @return {@code WarAgentType} - le type de l'agent �metteur du message.
	 */
	public WarAgentType getType() {
		return _senderType;
	}
	
	/**
	 * M�thode renvoyant le message envoy�.
	 * @return {@code String} - le message envoy�.
	 */
	public String getMessage() {
		return _message;
	}
	
	/**
	 * M�thode renvoyant le contenu du message.
	 * @return {@code String[]} - contenu du message.
	 */
	public String[] getContent() {
		return _content;
	}
	
	/**
	 * Renvoie un adaptateur de WarKernelMessage.
	 * @return {@code WarMessage} - Adaptateur du WarKernelMessage
	 */
	public WarMessage getAdapter(WarAgent receiver) {
		return new WarMessage(this, receiver);
	}
	
	public double getSenderHitboxRadius() {
		return _senderHitboxRadius;
	}
}
