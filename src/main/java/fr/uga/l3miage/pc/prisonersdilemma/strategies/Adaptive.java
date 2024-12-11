package fr.uga.l3miage.pc.prisonersdilemma.strategies;

import contract.*;
import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;
public class Adaptive extends Strategy{
    private boolean hasSequenceEnded = false;

    @Override
    public Action play(Game game, PlayerNumber opponent) {
        if(!hasSequenceEnded){
            updateHasSequenceEnded(game);
            if(isCooperateSequence(game)){
                return Action.COOPERATE;
            }
            return Action.BETRAY;
        }
        return actionWithTheBestMean(game, opponent);
    }

    private boolean isCooperateSequence(Game game){
        return game.getCurrentTurn()<6;
    }

    private double getMeanCooperateSequence(Game game, PlayerNumber opponent){
        int totalScore = 0;
        for(int i = 0; i < 6; i++){
            totalScore += game.getScoreByTurnNumberAndByPlayerNumber(i,getStrategyPlayerNumber(opponent));
        }
        return (double) totalScore /6;
    }

    private PlayerNumber getStrategyPlayerNumber(PlayerNumber opponent){
        if(opponent == PlayerNumber.PLAYER_ONE){
            return PlayerNumber.PLAYER_TWO;
        }
        return PlayerNumber.PLAYER_ONE;
    }

    private double getMeanBetraySequence(Game game, PlayerNumber opponent){
        int totalScore = 0;
        for(int i = 5; i < 10; i++){
            totalScore += game.getScoreByTurnNumberAndByPlayerNumber(i,getStrategyPlayerNumber(opponent));
        }
        return (double) totalScore /4;
    }

    private void updateHasSequenceEnded(Game game){
        this.hasSequenceEnded = game.getCurrentTurn() > 9;
    }

    private Action actionWithTheBestMean(Game game, PlayerNumber opponent){
        if(getMeanBetraySequence(game,opponent)> getMeanCooperateSequence(game, opponent)){
            return Action.BETRAY;
        }
        return Action.COOPERATE;
    }

}
