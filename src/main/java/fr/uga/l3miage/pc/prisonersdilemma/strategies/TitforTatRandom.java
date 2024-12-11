package fr.uga.l3miage.pc.prisonersdilemma.strategies;

import contract.Choice;
import contract.Game;
import contract.PlayerNumber;
import fr.uga.l3miage.pc.prisonersdilemma.Utils;
import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;

import java.util.Random;

public class TitforTatRandom extends Strategy{

    private final Random random;

    public TitforTatRandom(Random random) {
        this.random = random;
    }
    @Override
    public Action play(Game game, PlayerNumber opponent){
        if (isOpponentHistoryEmpty(game)) {
            return Action.COOPERATE;
        }
        if (isNextActionRandom()) {
            return playNextTurnRandom();
        }

        return getOpponentLastAction(game, opponent);
    }

    private boolean isNextActionRandom() {
        int randomInt = random.nextInt(2);
        return randomInt == 1;
    }

    private Action playNextTurnRandom(){
        int randomInt = random.nextInt(2);
        if (randomInt == 1){
            return Action.COOPERATE;
        }
        return Action.BETRAY;
    }

    private boolean isOpponentHistoryEmpty(Game game){
        return game.getTurnThatJustEnded() == null;
    }

    private Action getOpponentLastAction(Game game, PlayerNumber opponent){
        Choice choice = game.getTurnThatJustEnded().getChoiceByPlayerNumber(opponent) ;
        Action action = Utils.convertChoiceToAction(choice);

        return action;
    }
}
