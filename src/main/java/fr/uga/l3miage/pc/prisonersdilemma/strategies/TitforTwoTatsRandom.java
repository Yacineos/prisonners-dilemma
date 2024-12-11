package fr.uga.l3miage.pc.prisonersdilemma.strategies;

import contract.*;
import fr.uga.l3miage.pc.prisonersdilemma.Utils;
import fr.uga.l3miage.pc.prisonersdilemma.enums.Action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TitforTwoTatsRandom extends Strategy{
    private final Random random;
    private boolean startReciprocity = false;

    public TitforTwoTatsRandom(Random random) {
        this.random = random;
    }
    @Override
    public Action play(Game game, PlayerNumber opponent){
        if (isOpponentHistoryEmpty(game)) {
            return Action.COOPERATE;
        }
        boolean nextPlayIsRandom = isNextPlayRandom();
        if(startReciprocity && !nextPlayIsRandom){
            return getOpponentLastAction(game,opponent);
        }
        if (isBothLastOpponentActionsSame(game, opponent)){
            if(nextPlayIsRandom){
                return playNextTurnRandom();
            }
            startReciprocity = true;
            return getOpponentLastAction(game, opponent);
        }
        if(nextPlayIsRandom){
            return playNextTurnRandom();
        }
        return Action.BETRAY;
    }


    private Action playNextTurnRandom(){

        int randomInt = random.nextInt(2);
        if (randomInt == 1){
            return Action.COOPERATE;
        }
        return Action.BETRAY;
    }

    private boolean isBothLastOpponentActionsSame(Game game, PlayerNumber opponent){
        if(last2Turns(game).size() < 2){
            return false;
        }
        return last2Turns(game).get(0).getChoiceByPlayerNumber(opponent) == last2Turns(game).get(1).getChoiceByPlayerNumber(opponent);
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


    private boolean isNextPlayRandom(){
        int randomInt = random.nextInt(2);
        return randomInt == 1;
    }

    private boolean isOpponentHistoryEmpty(Game game){
        return game.getTurnThatJustEnded() == null;
    }

    private Action getOpponentLastAction(Game game, PlayerNumber opponent){
        Choice choice = game.getTurnThatJustEnded().getChoiceByPlayerNumber(opponent) ;
        Action action = Utils.convertChoiceToAction(choice);

        return action;}
}
