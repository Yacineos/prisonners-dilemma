package fr.uga.l3miage.pc.prisonersdilemma.strategies;

import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;
import fr.uga.l3miage.pc.prisonersdilemma.enums.PlayerNumber;
import fr.uga.l3miage.pc.prisonersdilemma.game.Game;

public class Pavlov implements Strategy{
    @Override
    public Action play(Game game, PlayerNumber opponent) {
        if(isOpponentHistoryEmpty(game)){
            return Action.COOPERATE;
        }
        if(isLastScoreSufficient(game, opponent)){
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
        return game.getTurnThatJustEnded().getActionByPlayerNumber(getStrategyPlayerNumber(opponent));
    }

}
