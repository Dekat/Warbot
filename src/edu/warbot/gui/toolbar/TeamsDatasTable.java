package edu.warbot.gui.toolbar;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JTable;

import edu.warbot.game.WarGame;

@SuppressWarnings("serial")
public class TeamsDatasTable extends JTable implements Observer {

	private TeamsDatasTableModel _model;
	private WarGame game;
	
	public TeamsDatasTable(WarGame game) {
		super();
		this.game = game;
		
		setDefaultRenderer(Object.class, new DefaultCellRenderer());
		getTableHeader().setVisible(false);
		setRowHeight(20);
		
		_model = new TeamsDatasTableModel(game);
		setModel(_model);
		getColumnModel().getColumn(0).setCellRenderer(new HeaderCellRenderer());
		
		game.addObserver(this);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Integer) {
			Integer cause = (Integer) arg;
			if (cause == WarGame.UPDATE_TEAM_ADDED || cause == WarGame.UPDATE_TEAM_REMOVED) {
				_model = new TeamsDatasTableModel(game);
				setModel(_model);
				getColumnModel().getColumn(0).setCellRenderer(new HeaderCellRenderer());
			}
		}
	}
	
}
