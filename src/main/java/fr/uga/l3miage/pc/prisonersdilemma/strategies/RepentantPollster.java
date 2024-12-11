package fr.uga.l3miage.pc.prisonersdilemma.strategies;

import contract.*;
import fr.uga.l3miage.pc.prisonersdilemma.Utils;
import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RepentantPollster extends Strategy{

    private final Random random;


    public RepentantPollster(Random random) {
        this.random = random;

    }
    @Override
    public Action play(Game game, PlayerNumber opponent){

        if (isOpponentHistoryEmpty(game)){
            return Action.COOPERATE;
        }
        if (opponentHasBetrayedAfterRandomBetray(game , opponent)){
            return Action.COOPERATE;
        }
        if (isNextActionRandom() ){
            return Action.BETRAY;
        }
        return getPlayerLastAction(game,opponent);
    }
    private boolean isNextActionRandom() {
        int randomInt = random.nextInt(2);
        return randomInt == 1;
    }

    private boolean opponentHasBetrayedAfterRandomBetray(Game game, PlayerNumber opponent){
        if(last2Turns(game).size() < 2){
            return false;
        }
        Choice opponentReaction = last2Turns(game).get(1).getChoiceByPlayerNumber(opponent);
        Action opponentReactionAction = Utils.convertChoiceToAction(opponentReaction);
        Choice strategyTurn = last2Turns(game).get(0).getChoiceByPlayerNumber(getStrategyPlayerNumber(opponent));
        Action strategyTurnAction = Utils.convertChoiceToAction(strategyTurn);
        return  opponentReactionAction == Action.BETRAY && strategyTurnAction == Action.BETRAY;
    }

    private List<Turn> last2Turns(Game game){
        List<Turn> lastTwoTurns = new ArrayList<>();
        int currentTurn = game.getCurrentTurn();
        if (currentTurn == 0) {
            return lastTwoTurns;
        }
        int start = Math.max(0, currentTurn - 2);
        lastTwoTurns.addAll(Arrays.asList(game.getTurns()).subList(start, currentTurn));

        return lastTwoTurns;
    }

    private Action getPlayerLastAction(Game game, PlayerNumber opponent){
        Choice choice = game.getTurnThatJustEnded().getChoiceByPlayerNumber(opponent);
        Action action = Utils.convertChoiceToAction(choice);
        return action;
    }

    public PlayerNumber getStrategyPlayerNumber(PlayerNumber opponent){
        if(opponent == PlayerNumber.PLAYER_ONE){
            return PlayerNumber.PLAYER_TWO;
        }
        return PlayerNumber.PLAYER_ONE;
    }

    private boolean isOpponentHistoryEmpty(Game game){
        return game.getTurnThatJustEnded() == null;
    }

}
