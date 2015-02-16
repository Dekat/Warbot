package edu.warbot.gui.debug;

import edu.warbot.agents.enums.WarAgentCategory;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.gui.debug.eventlisteners.AddToolMouseListener;
import edu.warbot.gui.debug.eventlisteners.DeleteToolMouseListener;
import edu.warbot.gui.debug.eventlisteners.InfosToolMouseListener;
import edu.warbot.gui.debug.eventlisteners.MoveToolMouseListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.HashMap;

@SuppressWarnings("serial")
public class DebugToolsPnl extends JPanel {

	private DebugModeToolBar _debugToolBar;
	
	private HashMap<String, WarAgentSelectButton> _agentSelectButtons;
	private WarAgentType _selectedWarAgentTypeToCreate;
	
	private DebugToolOnOffBtn _infoTool;
	private DebugToolOnOffBtn _addTool;
	private DebugToolOnOffBtn _deleteTool;
	private DebugToolOnOffBtn _moveTool;
	
	private JPanel _pnlSelectAgent;
	
	public DebugToolsPnl(DebugModeToolBar debugToolBar) {
		super();
		_selectedWarAgentTypeToCreate = null;
		_debugToolBar = debugToolBar;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel pnlBattlegroundTools = new JPanel();
		pnlBattlegroundTools.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		ButtonGroup groupBattleGroundTools = new ButtonGroup();
		
		_infoTool = new DebugToolOnOffBtn("infos.png", "infos_s.png",
				"Obtenir des informations sur un agent",
				'i', _debugToolBar, new InfosToolMouseListener(_debugToolBar));
		pnlBattlegroundTools.add(_infoTool);
		groupBattleGroundTools.add(_infoTool);
		
		_addTool = new DebugToolOnOffBtn("add.png", "add_s.png",
				"Ajouter un agent sur le champ de bataille",
				'a', _debugToolBar, new AddToolMouseListener(_debugToolBar, this));
		pnlBattlegroundTools.add(_addTool);
		groupBattleGroundTools.add(_addTool);
		
		_deleteTool = new DebugToolOnOffBtn("delete.png", "delete_s.png",
				"Suprimer un agent du champ de bataille",
				'd', _debugToolBar, new DeleteToolMouseListener(_debugToolBar));
		pnlBattlegroundTools.add(_deleteTool);
		groupBattleGroundTools.add(_deleteTool);
		
		_moveTool = new DebugToolOnOffBtn("move.png", "move_s.png",
				"Déplacer un agent du champ de bataille",
				'm', _debugToolBar, new MoveToolMouseListener(_debugToolBar));
		pnlBattlegroundTools.add(_moveTool);
		groupBattleGroundTools.add(_moveTool);
		
		add(pnlBattlegroundTools);
		
		
		_pnlSelectAgent = new JPanel();
		_pnlSelectAgent.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		_pnlSelectAgent.setLayout(new FlowLayout());
		_pnlSelectAgent.setPreferredSize(new Dimension(200, 300));
		_agentSelectButtons = new HashMap<>();
		ButtonGroup groupSelectAgentToCreate = new ButtonGroup();
		WarAgentType[] typesWhichCanBeCreate = WarAgentType.getAgentsOfCategories(WarAgentCategory.Building,
				WarAgentCategory.Soldier, WarAgentCategory.Worker, WarAgentCategory.Resource); 
		for (WarAgentType type : typesWhichCanBeCreate) {
			WarAgentSelectButton btn = new WarAgentSelectButton(type, _debugToolBar);
			_agentSelectButtons.put(type.toString(), btn);
			_pnlSelectAgent.add(btn);
			groupSelectAgentToCreate.add(btn);
		}
		_pnlSelectAgent.setVisible(false);
		add(_pnlSelectAgent);
		
		loadEvents();
	}
	
	private void loadEvents() {
		_addTool.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				_pnlSelectAgent.setVisible(_addTool.isSelected());
			}
		});
	}
	
	public WarAgentType getSelectedWarAgentTypeToCreate() {
		return _selectedWarAgentTypeToCreate;
	}

	public void setSelectedWarAgentTypeToCreate(WarAgentType agentType) {
		_selectedWarAgentTypeToCreate = agentType;
	}
	
	public DebugToolOnOffBtn getInfoToolBtn() {
		return _infoTool;
	}
}
