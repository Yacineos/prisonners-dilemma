package fr.uga.l3miage.pc.prisonersdilemma.strategies;

import contract.Choice;
import contract.Game;
import contract.PlayerNumber;
import fr.uga.l3miage.pc.prisonersdilemma.Utils;
import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;

import java.util.Random;

public class PollsterRandomBetray extends Strategy{

    private final Random random;

    public PollsterRandomBetray(Random random) {
        this.random = random;
    }

    @Override
    public Action play(Game game, PlayerNumber opponent){
        if (isOpponentHistoryEmpty(game)) {
            return Action.COOPERATE;
        }
        if (isNextActionBetray()){
            return Action.BETRAY;
        }
        return opponentLastAction(game,opponent);
    }
    private boolean isNextActionBetray() {
        int randomInt = random.nextInt(2);
        return randomInt == 1;
    }

    private boolean isOpponentHistoryEmpty(Game game){
        return game.getTurnThatJustEnded() == null;
    }
    private Action opponentLastAction(Game game, PlayerNumber opponent){
        Choice choice = game.getTurnThatJustEnded().getChoiceByPlayerNumber(opponent);
        Action action = Utils.convertChoiceToAction(choice);
        return action;
    }

}
