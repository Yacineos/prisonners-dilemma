package fr.uga.l3miage.pc.prisonersdilemma.strategies;

import contract.*;
import fr.uga.l3miage.pc.prisonersdilemma.Utils;
import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;

public class TitForTatSuspicious extends Strategy{
    @Override
    public Action play(Game game, PlayerNumber opponent){
        if (isOpponentHistoryEmpty(game)) {
            return Action.BETRAY;
        }
        return opponentLastAction(game, opponent);
    }


    private boolean isOpponentHistoryEmpty(Game game){
        return game.getTurnThatJustEnded() == null;
    }

    private Action opponentLastAction(Game game, PlayerNumber opponent){
        Choice choice = game.getTurnThatJustEnded().getChoiceByPlayerNumber(opponent) ;
        Action action = Utils.convertChoiceToAction(choice);

        return action;
    }
}
