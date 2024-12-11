package fr.uga.l3miage.pc.prisonersdilemma.strategies;

import contract.*;
import fr.uga.l3miage.pc.prisonersdilemma.Utils;
import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;

public class GradualStrategy extends Strategy {
    private int numberOfOpponentBetrays = 0;
    private int betrayCount = 0;
    private int cooperateCount = 2;
    @Override
    public Action play(Game game, PlayerNumber opponent) {
        if (isOpponentHistoryEmpty(game)) {
            return Action.COOPERATE;
        }

        if (betrayCount > 0) {
            betrayCount--;
            return Action.BETRAY;
        }

        if (cooperateCount > 0) {
            cooperateCount--;
            return Action.COOPERATE;         }

        if (hasOpponentBetrayed(game, opponent)) {
            numberOfOpponentBetrays++;
            betrayCount = numberOfOpponentBetrays;
            cooperateCount = 2;
            return Action.BETRAY;
        }

        return Action.COOPERATE;
    }

    private boolean isOpponentHistoryEmpty(Game game) {
        return game.getTurnThatJustEnded() == null;
    }

    private Action opponentLastAction(Game game, PlayerNumber opponent) {
        Choice choice = game.getTurnThatJustEnded().getChoiceByPlayerNumber(opponent);
        Action action = Utils.convertChoiceToAction(choice);
        return action;
    }

    private boolean hasOpponentBetrayed(Game game, PlayerNumber opponent) {
        return opponentLastAction(game, opponent) == Action.BETRAY;
    }
}
