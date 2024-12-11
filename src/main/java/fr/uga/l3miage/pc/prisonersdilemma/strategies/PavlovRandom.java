package fr.uga.l3miage.pc.prisonersdilemma.strategies;

import fr.uga.l3miage.pc.prisonersdilemma.Utils;
import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;
import contract.*;

import java.util.Random;

public class PavlovRandom extends Strategy{

    private final Random random;

    public PavlovRandom(Random random) {
        this.random = random;
    }


    @Override
    public Action play(Game game, PlayerNumber opponent) {
        if(isOpponentHistoryEmpty(game)){
            return Action.COOPERATE;
        }
        if(isLastScoreSufficient(game, opponent)){
            if(isNextActionRandom()){
                return playNextTurnRandom();
            }
            return getStrategyLastAction(game, opponent);
        }
        return Action.COOPERATE;
    }

    private boolean isOpponentHistoryEmpty(Game game){
        return game.getTurnThatJustEnded() == null;
    }

    private boolean isLastScoreSufficient(Game game, PlayerNumber opponent){
        PlayerNumber strategyPlayerNumber = getStrategyPlayerNumber(opponent);
        return game.getScoreByTurnNumberAndByPlayerNumber(game.getCurrentTurn()-1,strategyPlayerNumber) >= 3;

    }
    private PlayerNumber getStrategyPlayerNumber(PlayerNumber opponent){
        if(opponent == PlayerNumber.PLAYER_ONE){
            return PlayerNumber.PLAYER_TWO;
        }
        return PlayerNumber.PLAYER_ONE;
    }

    private Action getStrategyLastAction(Game game, PlayerNumber opponent){
        Choice choice = game.getTurnThatJustEnded().getChoiceByPlayerNumber(getStrategyPlayerNumber(opponent));
        Action res = Utils.convertChoiceToAction(choice);
        return res;
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
}
