package edu.warbot.gui.debug.infos;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.gui.debug.DebugModeToolBar;

import javax.swing.*;

@SuppressWarnings("serial")
public class ControllableWarAgentInformationsPnl extends JPanel implements IWarAgentInformationsPnl {

	private DebugModeToolBar _debugToolBar;

	private InfoLabel _distanceOfView;
	private InfoLabel _angleOfView;
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
		_bag.setValue("");
	}

}
