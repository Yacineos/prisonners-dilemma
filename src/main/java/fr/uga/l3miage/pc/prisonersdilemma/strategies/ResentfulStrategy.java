package fr.uga.l3miage.pc.prisonersdilemma.strategies;

import contract.*;
import fr.uga.l3miage.pc.prisonersdilemma.Utils;
import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;


public class ResentfulStrategy extends Strategy{

    private boolean hasBetrayed = false;
    @Override
    public Action play(Game game, PlayerNumber opponent) {
        if(isOpponentHistoryEmpty(game)){
            return Action.COOPERATE;
        }
        if(hasOpponentBetrayed(game, opponent)){
            return Action.BETRAY;
        }
        return Action.COOPERATE;
    }


    private boolean isOpponentHistoryEmpty(Game game){
        return game.getTurnThatJustEnded() == null;
    }

    private boolean hasOpponentBetrayed(Game game, PlayerNumber opponent){
        if (!hasBetrayed) {
            Choice choice = game.getTurnThatJustEnded().getChoiceByPlayerNumber(opponent);
            Action action = Utils.convertChoiceToAction(choice);
            this.hasBetrayed = action == Action.BETRAY;
        }
        return hasBetrayed;
    }
}
