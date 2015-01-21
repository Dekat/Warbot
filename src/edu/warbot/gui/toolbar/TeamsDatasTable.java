package edu.warbot.gui.toolbar;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JTable;

import edu.warbot.game.Game;

@SuppressWarnings("serial")
public class TeamsDatasTable extends JTable implements Observer {

	private TeamsDatasTableModel _model;
	
	public TeamsDatasTable() {
		super();
		
		setDefaultRenderer(Object.class, new DefaultCellRenderer());
		getTableHeader().setVisible(false);
		setRowHeight(20);
		
		_model = new TeamsDatasTableModel();
		setModel(_model);
		getColumnModel().getColumn(0).setCellRenderer(new HeaderCellRenderer());
		
		Game.getInstance().addObserver(this);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Integer) {
			Integer cause = (Integer) arg;
			if (cause == Game.UPDATE_TEAM_ADDED || cause == Game.UPDATE_TEAM_REMOVED) {
				_model = new TeamsDatasTableModel();
				setModel(_model);
				getColumnModel().getColumn(0).setCellRenderer(new HeaderCellRenderer());
			}
		}
	}
	
}
