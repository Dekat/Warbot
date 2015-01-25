package edu.warbot.gui.debug.infos;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.gui.debug.DebugModeToolBar;

@SuppressWarnings("serial")
public class ControllableWarAgentInformationsPnl extends JPanel implements IWarAgentInformationsPnl {

	private DebugModeToolBar _debugToolBar;

	private InfoLabel _distanceOfView;
	private InfoLabel _angleOfView;
	private InfoLabel _health;
	private InfoLabel _bag;
	private InfoLabel _viewDirection;

	public ControllableWarAgentInformationsPnl(DebugModeToolBar debugToolBar) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		_debugToolBar = debugToolBar;

		add(new JLabel(" "));
		_viewDirection = new InfoLabel("Direction du regard");
		add(_viewDirection);
		_distanceOfView = new InfoLabel("Distance de vue");
		add(_distanceOfView);
		_angleOfView = new InfoLabel("Angle de vue");
		add(_angleOfView);
		_health = new InfoLabel("Santé");
		add(_health);
		_bag = new InfoLabel("Sac");
		add(_bag);
	}

	@Override
	public void update() {
		if (_debugToolBar.getSelectedAgent() instanceof ControllableWarAgent) {
			setVisible(true);
			ControllableWarAgent a = (ControllableWarAgent) _debugToolBar.getSelectedAgent();

			_viewDirection.setValue(WarAgentInformationsPnl.doubleFormatter.format(a.getViewDirection()));
			_distanceOfView.setValue(WarAgentInformationsPnl.doubleFormatter.format(a.getDistanceOfView()));
			_angleOfView.setValue(WarAgentInformationsPnl.doubleFormatter.format(a.getAngleOfView()));
			_health.setValue(a.getHealth() + " / " + a.getMaxHealth());
			_bag.setValue(a.getNbElementsInBag() + " / " + a.getBagSize());
		} else {
			setVisible(false);
		}
	}

	@Override
	public void resetInfos() {
		_viewDirection.setValue("");
		_distanceOfView.setValue("");
		_angleOfView.setValue("");
		_health.setValue("");
		_bag.setValue("");
	}

}
