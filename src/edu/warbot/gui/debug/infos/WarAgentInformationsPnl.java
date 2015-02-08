package edu.warbot.gui.debug.infos;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.warbot.agents.WarAgent;
import edu.warbot.gui.debug.DebugModeToolBar;

@SuppressWarnings("serial")
public class WarAgentInformationsPnl extends JPanel implements IWarAgentInformationsPnl {

	private DebugModeToolBar _debugToolBar;

	private InfoLabel _id;
	private InfoLabel _type;
	private InfoLabel _position;
	private InfoLabel _team;
	private InfoLabel _heading;

	private ControllableWarAgentInformationsPnl _controllableAgent;
	private MovableWarAgentInformationsPnl _movableAgent;
	private CreatorWarAgentInformationsPnl _creatorAgent;
	private WarProjectileInformationsPnl _projectile;

	public WarAgentInformationsPnl(DebugModeToolBar debugToolBar) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createLineBorder(Color.GRAY));

		_debugToolBar = debugToolBar;

		add(new JLabel("Informations sur l'agent sélectionné : "));

		_id = new InfoLabel("ID");
		add(_id);
		_type = new InfoLabel("Type");
		add(_type);
		_position = new InfoLabel("Position");
		add(_position);
		_team = new InfoLabel("Equipe");
		add(_team);
		_heading = new InfoLabel("Heading");
		add(_heading);

		_controllableAgent = new ControllableWarAgentInformationsPnl(_debugToolBar);
		add(_controllableAgent);
		_movableAgent = new MovableWarAgentInformationsPnl(_debugToolBar);
		add(_movableAgent);
		_creatorAgent = new CreatorWarAgentInformationsPnl(_debugToolBar);
		add(_creatorAgent);
		_projectile = new WarProjectileInformationsPnl(_debugToolBar);
		add(_projectile);
	}

	@Override
	public void update() {
		WarAgent a = _debugToolBar.getSelectedAgent();

		if (a == null) {
			setVisible(false);
		} else {
			setVisible(true);
			_id.setValue(String.valueOf(a.getID()));
			_type.setValue(a.getClass().getSimpleName());
			_position.setValue("(" + doubleFormatter.format(a.getX()) + "; " + doubleFormatter.format(a.getY()) + ")");
			_team.setValue(a.getTeam().getName());
			_heading.setValue(doubleFormatter.format(a.getHeading()));

			_controllableAgent.update();
			_movableAgent.update();
			_creatorAgent.update();
			_projectile.update();
		}
	}

	@Override
	public void resetInfos() {
		_id.setValue("");
		_type.setValue("");
		_position.setValue("");
		_team.setValue("");
		_heading.setValue("");
	}

}
