package fr.uga.l3miage.pc.prisonersdilemma.strategies;

import contract.*;
import fr.uga.l3miage.pc.prisonersdilemma.Utils;
import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;


public class SoftResentful extends Strategy {
    private int resentfulRoundCounter = 0;

    @Override
    public Action play(Game game, PlayerNumber opponent) {
        if (isOpponentHistoryEmpty(game)) {
            return Action.COOPERATE;
        }

        if (isResentfulActive()) {
            return handleResentfulRounds();
        }

        if (hasOpponentBetrayed(game, opponent)) {
            activateResentfulRounds();
            return handleResentfulRounds();
        }

        return Action.COOPERATE;
    }

    private boolean isOpponentHistoryEmpty(Game game) {
        return game.getTurnThatJustEnded() == null;
    }

    private boolean hasOpponentBetrayed(Game game, PlayerNumber opponent) {
        Choice choice = game.getTurnThatJustEnded().getChoiceByPlayerNumber(opponent) ;
        Action action = Utils.convertChoiceToAction(choice);
        return  action == Action.BETRAY;
    }

    private boolean isResentfulActive() {
        return resentfulRoundCounter > 0;
    }

    private void activateResentfulRounds() {
        resentfulRoundCounter = 6;
    }

    private Action handleResentfulRounds() {
        if (resentfulRoundCounter > 2) {
            resentfulRoundCounter--;
            return Action.BETRAY;
        } else {
            resentfulRoundCounter--;
            return Action.COOPERATE;
        }
    }
}
