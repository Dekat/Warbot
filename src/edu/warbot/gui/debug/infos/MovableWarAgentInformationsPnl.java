package edu.warbot.gui.debug.infos;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.gui.debug.DebugModeToolBar;

@SuppressWarnings("serial")
public class MovableWarAgentInformationsPnl extends JPanel implements IWarAgentInformationsPnl {
	private DebugModeToolBar _debugToolBar;
	
	private InfoLabel _speed;
	
	public MovableWarAgentInformationsPnl(DebugModeToolBar debugToolBar) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		_debugToolBar = debugToolBar;
		
		add(new JLabel(" "));
		_speed = new InfoLabel("Vitesse");
		add(_speed);
	}
	
	@Override
	public void update() {
		if (_debugToolBar.getSelectedAgent() instanceof MovableWarAgent) {
			setVisible(true);
			MovableWarAgent a = (MovableWarAgent) _debugToolBar.getSelectedAgent();

			_speed.setValue(doubleFormatter.format(a.getSpeed()));
		} else {
			setVisible(false);
		}
	}
	
	@Override
	public void resetInfos() {
		_speed.setValue("");
	}

}