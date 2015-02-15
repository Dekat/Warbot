package edu.warbot.game;

public interface WarGameListener {

    public void onNewTeamAdded(Team newTeam);
    public void onTeamRemoved(Team removedTeam);
    public void onGameOver();
    public void onGameStopped();
    public void onGameStarted();
}
