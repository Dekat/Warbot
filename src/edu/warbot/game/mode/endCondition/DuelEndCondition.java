package edu.warbot.game.mode.endCondition;

import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.game.Team;
import edu.warbot.game.WarGame;

public class DuelEndCondition extends AbstractEndCondition {

    public DuelEndCondition(WarGame game) {
        super(game);
    }

    @Override
    public void doOnEachTick() {
        for (Team t : getGame().getPlayerTeams()) {
            if (t.getNbUnitsLeftOfType(WarAgentType.WarBase) == 0) {
                getGame().removePlayerTeam(t);
            }
        }
    }

    @Override
    public boolean isGameEnded() {
        return getGame().getPlayerTeams().size() <= 1;
    }
}
