package edu.warbot.gui.debug.infos;

import edu.warbot.agents.AliveWarAgent;
import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.gui.debug.DebugModeToolBar;

import javax.swing.*;

@SuppressWarnings("serial")
public class AliveWarAgentInformationsPnl extends JPanel implements IWarAgentInformationsPnl {

	private DebugModeToolBar _debugToolBar;

	private InfoLabel _health;

	public AliveWarAgentInformationsPnl(DebugModeToolBar debugToolBar) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		_debugToolBar = debugToolBar;

		add(new JLabel(" "));
		_health = new InfoLabel("Santé");
		add(_health);
	}

	@Override
	public void update() {
		if (_debugToolBar.getSelectedAgent() instanceof AliveWarAgent) {
			setVisible(true);
            AliveWarAgent a = (AliveWarAgent) _debugToolBar.getSelectedAgent();

			_health.setValue(a.getHealth() + " / " + a.getMaxHealth());
		} else {
			setVisible(false);
		}
	}

	@Override
	public void resetInfos() {
		_health.setValue("");
	}

}
