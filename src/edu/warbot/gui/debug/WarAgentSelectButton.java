package edu.warbot.gui.debug;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JToggleButton;

import edu.warbot.agents.enums.WarAgentType;

@SuppressWarnings("serial")
public class WarAgentSelectButton extends JToggleButton implements ActionListener {

	private WarAgentType _warAgentTypeToSelect;
	private DebugModeToolBar _toolBar;
	
	// TODO Ajout d'une image des agents
	public WarAgentSelectButton(WarAgentType warType, DebugModeToolBar toolBar) {
		super();
		
		_warAgentTypeToSelect = warType;
		_toolBar = toolBar;
		
		setText(_warAgentTypeToSelect.toString());
		setFont(new Font("Arial", Font.PLAIN, 10));
		setPreferredSize(new Dimension(125, 50));
		
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		_toolBar.getDebugTools().setSelectedWarAgentTypeToCreate(_warAgentTypeToSelect);
	}
}
