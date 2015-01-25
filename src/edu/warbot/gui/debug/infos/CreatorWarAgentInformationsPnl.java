package edu.warbot.gui.debug.infos;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.warbot.agents.CreatorWarAgent;
import edu.warbot.gui.debug.DebugModeToolBar;

@SuppressWarnings("serial")
public class CreatorWarAgentInformationsPnl extends JPanel  implements IWarAgentInformationsPnl{
	private DebugModeToolBar _debugToolBar;
	
	private InfoLabel _nextAgentToCreate;
	
	public CreatorWarAgentInformationsPnl(DebugModeToolBar debugToolBar) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		_debugToolBar = debugToolBar;
		
		add(new JLabel(" "));
		_nextAgentToCreate = new InfoLabel("Prochain agent à créer");
		add(_nextAgentToCreate);
	}
	
	@Override
	public void update() {
		if (_debugToolBar.getSelectedAgent() instanceof CreatorWarAgent) {
			setVisible(true);
		CreatorWarAgent a = (CreatorWarAgent) _debugToolBar.getSelectedAgent();

		_nextAgentToCreate.setValue(a.getNextAgentToCreate().toString());
		} else {
			setVisible(false);
		}
	}
	
	@Override
	public void resetInfos() {
		_nextAgentToCreate.setValue("");
	}

}