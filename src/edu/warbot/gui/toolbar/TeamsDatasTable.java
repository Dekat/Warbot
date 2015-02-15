package edu.warbot.gui.toolbar;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JTable;

import edu.warbot.game.Team;
import edu.warbot.game.WarGame;
import edu.warbot.game.WarGameListener;

@SuppressWarnings("serial")
public class TeamsDatasTable extends JTable implements WarGameListener {

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
		
		game.addWarGameListener(this);
	}
	
    @Override
    public void onNewTeamAdded(Team newTeam) {
        updateTeamsDataTable();
    }

    @Override
    public void onTeamRemoved(Team removedTeam) {
//        updateTeamsDataTable();
    }

    @Override
    public void onGameOver() {}

    @Override
    public void onGameStopped() {}

    @Override
    public void onGameStarted() {}

    private void updateTeamsDataTable() {
        _model = new TeamsDatasTableModel(game);
        setModel(_model);
        getColumnModel().getColumn(0).setCellRenderer(new HeaderCellRenderer());
    }
}
